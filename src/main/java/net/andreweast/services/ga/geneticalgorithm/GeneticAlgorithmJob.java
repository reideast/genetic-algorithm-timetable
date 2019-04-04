package net.andreweast.services.ga.geneticalgorithm;

import net.andreweast.BeanUtil;
import net.andreweast.services.ga.service.Dispatcher;
import net.andreweast.services.ga.service.GaToDbSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.andreweast.WebSocketConfiguration.MESSAGE_PREFIX;


public class GeneticAlgorithmJob implements Runnable {
    // To control the thread, change this atomic (i.e. thread-safe) boolean. The thread will stop itself after the next generation
    private final AtomicBoolean isRunning;

    // A thread pool that can be used to execute any subtasks of the GA that can run in parallel
    private final ExecutorService threadPool;

    // How often to send reports back to the database, in percentage of job done
    private final float queryRate;

    // Spring @Service bean classes
    // Can't use @Autowired here because that would require letting Spring manage the lifecycle of this class,
    // but the lifecycle of this class is managed as a Thread
    private final GaToDbSerializer gaToDbSerializer;
    private final Dispatcher dispatcher;
    private SimpMessagingTemplate websocket;

    // Data on this job:
    // Master data is all the metadata the GA needs to run
    private GeneticAlgorithmJobData masterData;

    // Parameters of the GA
    // These are to tweak the algorithm. All params are passed as input from the REST interface
    // Number of generations to stop after
    private final int numGenerationsMaximum;
    // How many "extra" generations to run after a valid (no violated hard constraints) solution has emerged
    // Expressed in the proportion of already-ran generations. E.g. If this is 20, and 1000 generations have run when a valid solution is found, then 1000 * 0.20 = 200 more generations will run
    private final float proportionRunDownGenerations;
    // Crossover with p = 0.6
    private final float crossoverProbability;
    // Mutate each individual with p = 0.9
    private final float mutateProbability;
    // How many genes can be mutated within a chromosome if it is mutated: between 1..mutatedGenesMax
    private final int mutatedGenesMax;
    // How many of the very best in a population are guaranteed to survive
    private final int eliteSurvivors;

    // Generation counter; Atomic so that it can be read by outside services querying this GA job's progress
    private AtomicInteger currentGeneration;

    // Data structures being used by the job in action
    private Population population;
    static final boolean DEBUG = true; // DEBUG

    public GeneticAlgorithmJob(GeneticAlgorithmJobData geneticAlgorithmJobData, ExecutorService threadPool) {
        // Services
        // Directly get Spring @Services via a context-aware utility class
        gaToDbSerializer = BeanUtil.getBean(GaToDbSerializer.class);
        dispatcher = BeanUtil.getBean(Dispatcher.class);
        websocket = BeanUtil.getBean(SimpMessagingTemplate.class);
        this.threadPool = threadPool;

        // Genetic algorithm data
        masterData = geneticAlgorithmJobData;
        numGenerationsMaximum = masterData.getNumGenerations();
        proportionRunDownGenerations = masterData.getProportionRunDownGenerations();
        crossoverProbability = masterData.getCrossoverProbability();
        mutateProbability = masterData.getMutateProbability();
        mutatedGenesMax = masterData.getMutatedGenesMax();
        eliteSurvivors = masterData.getNumEliteSurvivors();

        // Thread control
        currentGeneration = new AtomicInteger(0);
        isRunning = new AtomicBoolean(false);
        queryRate = masterData.getQueryRate();
    }

    @Override
    public void run() {
        System.out.println("************* GENETIC ALGORITHM INITIALISATION jobId=" + masterData.getJobId() + ", schedId=" + masterData.getScheduleId() + " *************"); // DEBUG
        createInitialPopulation();
        System.out.println("************* GENETIC ALGORITHM POPULATION CREATED jobId=" + masterData.getJobId() + ", schedId=" + masterData.getScheduleId() + " *************"); // DEBUG
        runAllGenerations();
        saveBestIndividualToMasterData();
        writeBackToDatabase();
        finaliseJob();
        System.out.println("************* JOB DONE jobId=" + masterData.getJobId() + ", schedId=" + masterData.getScheduleId() + " *************"); // DEBUG
    }

    private void createInitialPopulation() {
        population = new Population(masterData, threadPool);
    }

    private void runAllGenerations() {
        currentGeneration.set(0);

        // Let the algorithm be quit earlier than the specified MAX generations
        // Such as, if all hard constraints are met, then let the algorithm run for several more generations, then quit
        float tentativeGenLimit = numGenerationsMaximum;
        // Is the algorithm currently running those "several more generations?"
        boolean isDoingFinalRunDown = false;

        if (masterData.isModifyExistingJob()) {
            // For an existing job, ALWAYS run for at least 20% of total length (or whatever percentage is in currentGeneration.get())
            // DEBUG: This is a hack. An existing job might be configured with how many minimum generations to run (defaulting to 20% max maybe)
            tentativeGenLimit = currentGeneration.get() + (proportionRunDownGenerations * numGenerationsMaximum); // DEBUG
            isDoingFinalRunDown = true; // DEBUG
        }

        int queryGenerationModulus = Math.max((int) (numGenerationsMaximum * queryRate), 1); // Max is to guard against when query rate has been set higher than numGenMax

        long startTime = System.nanoTime(); // DEBUG
        String csvHeader;
        List<String> debugOutputLines;
        if (DEBUG) {
//        System.out.println(population); // DEBUG
            // Generate a CSV header for the chromosomes in a population
            debugOutputLines = new ArrayList<>(1 + numGenerationsMaximum);
            csvHeader = "generation_num," + Stream.iterate(0, item -> item + 1).limit(masterData.getPopulationSize()).map(item -> "chromosome" + item).collect(Collectors.joining(","));
        }
        final long initTime = System.nanoTime() - startTime; // DEBUG
        double runningAverage = -1; // DEBUG

        isRunning.set(true);
        while (isRunning.get()) { // Use of AtomicBoolean to control a Thread see: https://www.baeldung.com/java-thread-stop
            long generationTime = System.nanoTime(); // DEBUG

            population.mutate(mutateProbability, mutatedGenesMax);

            population.crossover(crossoverProbability);

            population.select(eliteSurvivors); // Selection must be done after genetic crossover/mutate in order to find cached hasValidSolution

//            // DEBUG: Some delay needed to prevent frontend from breaking because it cannot update when the job ends too quickly. This is obviously a hack, but may be able to eliminate it once there's more load for the whole GA
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            if (DEBUG) { // DEBUG
                // Output CSV values for the population's fitness values
                debugOutputLines.add(currentGeneration.get() + "," + population.toFitnessList().stream().map(Object::toString).collect(Collectors.joining(",")));
            }

            // DEBUG
            if (currentGeneration.get() == 0) {
                runningAverage = System.nanoTime() - generationTime;
            } else {
                runningAverage += ((System.nanoTime() - generationTime) - runningAverage) / ((double) (currentGeneration.get() + 1));
            }

            // Every 5% of the way through the job (config by QUERY_RATE), inform the frontend that the job status should be updated
            if (currentGeneration.get() % queryGenerationModulus == 0) {
                // Send a WebSocket publication to subscribers on the frontend web app, notifying of progress of this job
                this.websocket.convertAndSend(MESSAGE_PREFIX + "/jobStatus",
                        "{\"jobId\":" + masterData.getJobId() +
                                ",\"scheduleId\":" + masterData.getScheduleId() +
                                ",\"progressPercent\":" +
                                ((float) currentGeneration.get() / tentativeGenLimit) + // TODO: try it with numGenerationsMaximum rather than max, see how it look
                                ",\"fitnessEstimate\":" +
                                population.getEstimatedFitness() +
                                ",\"isDone\": " + false + "}");
            }

            // Increment generation counter, and then check for exit conditions
            if (currentGeneration.incrementAndGet() > numGenerationsMaximum) {
                isRunning.set(false);
            } else if (currentGeneration.get() > tentativeGenLimit) {
                if (population.hasValidSolution()) {
                    isRunning.set(false); // Can quit early! We had found a solution, ran some more generations as a "run down", and now we still have a solution
                    System.out.println(currentGeneration.get() + "gen: Found a valid solution and ran for several more generations. Quitting early!"); // FUTURE: Logger
                } else {
                    tentativeGenLimit = numGenerationsMaximum; // Ran several more generations, but have now LOST the valid solution. Go some more
                    isDoingFinalRunDown = false;
                    System.out.println(currentGeneration.get() + "gen: During the run down, the valid solution was lost. Population needs more more!"); // FUTURE: Logger
                }
            } else if (!isDoingFinalRunDown) {
                if (population.hasValidSolution()) {
                    tentativeGenLimit = currentGeneration.get() + (proportionRunDownGenerations * currentGeneration.get()); // Add a number of generations that is the fraction (expressed in proportionRunDownGenerations) of the current generation
                    isDoingFinalRunDown = true;
                    System.out.println(currentGeneration.get() + "gen: Found a valid solution! Doing a final run down now for " + (tentativeGenLimit - currentGeneration.get()) + " generations"); // FUTURE: Logger
                }
                // else: There's no valid solution. Continue running the algorithm as normal
            } // else: Already doing a final run down, don't check if the valid solution still exists until we're done
        }
        // DEBUG
        if (DEBUG) {
            System.out.print("Complexity of dataset:"); // FUTURE: Logger
            System.out.print(" Num modules: " + masterData.getModules().size()); // FUTURE: Logger
            System.out.println(" Venues x Timeslots: " + (masterData.getVenues().size() * masterData.getTimeslots().size())); // FUTURE: Logger
            System.out.print("Running time stats:"); // FUTURE: Logger
            System.out.print(" Num gens: " + (currentGeneration.get() - 1));
            System.out.print(" Time init: " + (initTime * 1.0E-6) + " ms"); // FUTURE: Logger
            System.out.print(" Average generation time: " + (runningAverage * 1E-6) + " ms"); // FUTURE: Logger
            System.out.println(" Total time: " + ((System.nanoTime() - startTime) * 1E-9) + " s"); // FUTURE: Logger
        }

        System.out.println("GA generations have completed in " + (currentGeneration.get() - 1) + " generations, job=" + masterData.getJobId() + ", schedule=" + masterData.getScheduleId()); // FUTURE: Logger info


        if (DEBUG) {
            // Log which modules were not able to be scheduled
            population.logFailuresToSchedule();
        }

        // Generate a CSV file with the parameters and running time results for this GA run
        // FUTURE: File writing could be optimised
        if (DEBUG) {
            try {
                File statsCsv = new File("stats" + File.separator + "ga_stats.csv");
                File generations250File = new File("stats" + File.separator + "fitness_for_all_gen_job_" + masterData.getJobId() + "_pick_250_gens.csv");

                // Write header row ONLY if this is the first run
                if (!statsCsv.exists()) {
                    statsCsv.getParentFile().mkdirs();
                    try (Writer csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(statsCsv)))) {
                        csvWriter.write("timestamp,jobId,scheduleId,Modules,Venues,Timeslots," +
                                "Population Size,Crossover Probability,Mutate Probability,Elite Survivors,Run Down Num Generations," +
                                "Generation Max,Generation Ran,Found Valid Timetable?,Avg Generation Time (ms),Total Time (ms),Final generation's chromosomes' fitness...\n");
                    }
                }

                // Write stats row, appending to (now created) existing file
                try (Writer csvWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(statsCsv, true)))) {
                    csvWriter.write(String.format("%s,%d,%d,%d,%d,%d," +
                                    "%d,%f,%f,%d,%f," +
                                    "%d,%d,%s,%f,%f,%s%n",
                            new Date(), masterData.getJobId(), masterData.getScheduleId(), masterData.getModules().size(), masterData.getVenues().size(), masterData.getTimeslots().size(),
                            masterData.getPopulationSize(), crossoverProbability, mutateProbability, eliteSurvivors, proportionRunDownGenerations,
                            numGenerationsMaximum, (currentGeneration.get() - 1), population.hasValidSolution(), (runningAverage * 1.0E-6), ((System.nanoTime() - startTime) * 1.0E-6),
                            population.toFitnessList()
                    ));
                }

                // Print gen-over-gen CSV to a new file
                try (Writer csv250Writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(generations250File)))) {
                    csv250Writer.write(csvHeader + "\n");

                    int modValueToLimit250 = Math.max(currentGeneration.get() / 250, 1);
                    for (int i = 0; i < debugOutputLines.size(); ++i) {
                        if (i % modValueToLimit250 == 0) {
                            csv250Writer.write(debugOutputLines.get(i) + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("ERROR: Writing results CSV has failed"); // FUTURE: logger
                e.printStackTrace();
            }
        }

        // Send a WebSocket publication to subscribers on the frontend web app, notifying job progress == DONE
        try {
            // DEBUG: If the algorithm converged TOO FAST, then add an artificial delay. This is to make up for failings in the front end (breaks state if updates too fast).
            // DEBUG: ...this is definitely a hack
            final int MIN_EXECUTION_TIME = 10 * 1000; // milliseconds
            final long actualExecutionTime = (long) ((System.nanoTime() - startTime) * 1E-6);
            System.out.println("Need a delay? Sleeping for " + (MIN_EXECUTION_TIME - actualExecutionTime) + "ms"); // DEBUG
            Thread.sleep(Math.max(MIN_EXECUTION_TIME - actualExecutionTime, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/jobStatus",
                "{\"jobId\":" + masterData.getJobId() +
                        ",\"scheduleId\":" + masterData.getScheduleId() +
                        ",\"progressPercent\":" + 1.0 +
                        ",\"fitnessEstimate\":" +
                        population.getEstimatedFitness() +
                        ",\"isDone\": " + true + "}");
    }

    /**
     * Inspects Population, and choose a single Chromosome to write back into {@link this.masterData}
     */
    private void saveBestIndividualToMasterData() {
        // Get info from Population, and choose a Chromosome to write back to {@link masterData}
        Chromosome bestChromosome = population.getBestChromosome();
        masterData.setScheduledModules(Arrays.asList(bestChromosome.getGenes()));
        masterData.setFitness(bestChromosome.getCachedFitness());
    }

    private void writeBackToDatabase() {
        gaToDbSerializer.writeScheduleData(masterData, masterData.getScheduleId());

        // Send a WebSocket publication to subscribers on the frontend web app, notifying "A job has finished for this schedule and has been written into the DB"
        this.websocket.convertAndSend(MESSAGE_PREFIX + "/jobComplete",
                "{\"scheduleId\":" + masterData.getScheduleId() +
                        ",\"foundValidSolution\":" + population.hasValidSolution() +
                        ",\"finalGenerationNumber\":" + (currentGeneration.get() - 1) + "}");

    }

    private void finaliseJob() {
        dispatcher.jobCompleted(masterData.getJobId());
        gaToDbSerializer.deleteJobForSchedule(masterData.getScheduleId());
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }

    public int getNumGenerations() {
        return numGenerationsMaximum;
    }

    public int getCurrentGeneration() {
        return currentGeneration.get();
    }
}
