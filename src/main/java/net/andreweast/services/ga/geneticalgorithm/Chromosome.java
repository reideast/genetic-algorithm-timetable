package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>, Serializable {
    private static Random random = new Random();

    private static boolean havePrintedCrossoverDebug = false; // DEBUG
    private static boolean havePrintedMutateDebug = false; // DEBUG

    private ScheduledModule[] genes;

    private GeneticAlgorithmJobData data;

    private int cachedFitness;

    // TODO: Do something with this. Maybe stop job early?
    private boolean isValidSolution;

    /**
     * Randomising constructor
     */
    public Chromosome(GeneticAlgorithmJobData masterData) {
        data = masterData;

        genes = new ScheduledModule[data.getChromosomeSize()];
        for (int i = 0; i < data.getChromosomeSize(); ++i) {
            genes[i] = new ScheduledModule(data.getIndexedModule(i), data);
        }

        cachedFitness = calculateFitness(); // Also sets isValidSolution
    }

    /**
     * Cloning constructor
     */
    public Chromosome(Chromosome toClone) {
        data = toClone.data;

        genes = new ScheduledModule[data.getChromosomeSize()];
        for (int i = 0; i < data.getChromosomeSize(); ++i) {
            genes[i] = toClone.genes[i].clone();
        }

        cachedFitness = toClone.getCachedFitness();
        isValidSolution = toClone.isValidSolution();
    }

    public void crossover(Chromosome toCrossWith) {
        final int crossoverPoint = random.nextInt(genes.length);
//        if (!havePrintedCrossoverDebug && GeneticAlgorithmJob.DEBUG) { // DEBUG
//            System.out.println("Before cross: " + this.toString());
//            System.out.println("Crossing w/:  " + toCrossWith.toString());
//            System.out.println("CrossoverPoint=" + crossoverPoint);
//        }

        for (int i = 0; i <= crossoverPoint; ++i) {
            genes[i] = toCrossWith.genes[i].clone();
        }
        cachedFitness = calculateFitness();
//        if (!havePrintedCrossoverDebug && GeneticAlgorithmJob.DEBUG) { // DEBUG
//            System.out.println("After cross:  " + this.toString());
//
//            havePrintedCrossoverDebug = true;
//        }
    }

    public void mutate() {
        // Randomise one of the scheduled modules
//        if (!havePrintedMutateDebug && GeneticAlgorithmJob.DEBUG) {
//            System.out.println("Before mutate: " + this.toString());
//        }

        final int mutateGene = random.nextInt(genes.length);
        genes[mutateGene] = new ScheduledModule(genes[mutateGene].getModule(), data);
        cachedFitness = calculateFitness();

//        if (!havePrintedMutateDebug && GeneticAlgorithmJob.DEBUG) {
//            System.out.println("@" + mutateGene + " After mutate:  " + this.toString());
//
//            havePrintedMutateDebug = true;
//        }
    }

    private int calculateFitness() {
        // TODO: act differently based on masterData.isModifyExistingJob

        // Weight of any "required" fitness is 100, such as two modules overlapping
        // Weight of any "preferable" fitness is 1, such as keeping a 2-hour lecture as one block vs. two blocks or lecture not being at 8am
        // TODO: Tweak these fitness weights

        // Calculate minimal fitness of any chromosome which has all HARD constraints met
        final int ONE_HARD_CONSTRAINT = 100;
        // TODO: soft constraints should be able to add up to just below that
        /*
          If there are 5 modules, and each violated hard constraint takes away 100 = ONE_HARD_CONSTRAINT fitness,
          then if no modules overlap, then Fitness would be 500.

          Almost all hard constraints met
          If just one hard constraint isn't met, then hard-fitness is 400.
          Let's say its soft constraints add up to soft-fitness 450. Then Fitness would be 950
          That's too much. A valid solution with really bad soft-fitness could be Fitness 501: much, much less, likely to be culled
          Let's say its soft constraints add up to soft-fitness 90. Then fitness would be 490
          A valid solution with good soft constraints could be 590. One with bad soft constraints could be 510.
          Seems good!

          Moderate amount of hard constraints met
          A solution with 3 violated hard constraints would be 200. Let's say it has really good soft: Fitness 290
          A solution with 2 violated hard constraints would be 300. Really bad soft: 310
          Very close! Either would have about the same probability of being selected for

          Most hard constraints violated vs. moderate
          2 violated hard, really bad soft: 310
          4 violated hard, really good soft: 190. Really bad soft: 110
          Either one has a good bit smaller probability of being selected for

          OK, so conclusion: Total of all POSSIBLE soft constraints should sum to 99 (99 = ONE_HARD_CONSTRAINT minus one)
         */

        /*
          Problem with that: When there's a LOT of hard constraints
          Let's say there are 300 modules (chromosome size). There's 5 hard constraints: conflict, course conflict, lab conflict, venue size conflict, {one more}. That's 1500
          If ONE_HARD_CONSTRAINT is 100, then total fitness for a valid solution is 15,000
          And any soft constraints could be at most 100.
          A solution with GREAT soft constraints but bad hard could be 299
          Vs. one with really good hard constraints: 10,009
          There's VERY little chance the good soft constraints one would be selected for!
          (That might be OK: those "good" soft constraints being so good PROBABLY originates from a bunch of hard constraints being violated.)

          Maybe the behaviour we'll expect to see is more "stepwise"
          1. Low hard-fitness, some improvement in soft-fitness develops over generations
          2. Then, an individual with much better hard-fitness comes along!
          3. Sudden jump as whole population adopts those changes. SOME good soft constraints might be retained
          4. Repeat.
          It seems this behaviour will be highly dependent on selection rates vs crossover rate.
          i.e. the roulette wheel selection: one individual with 10,000 fitness and 10 individuals with 1,000 selection
          according to probability, fully ONE HALF of the next generation will be CLONES of the 10,000 individual
          That will quickly lead to lack of genetic diversity
          Perhaps the solution to this is lots of crossover/mutation?
          Perhaps the solution is to make sure that hard-fitness values aren't TOO LARGE?
         */

        // Start with the max possible hard-fitness value; subtract has violations are found
        int fitnessFromOverlappingClasses = data.getChromosomeSize() * ONE_HARD_CONSTRAINT;
        int fitnessFromInvalidVenues = data.getChromosomeSize() * ONE_HARD_CONSTRAINT;
        isValidSolution = true;

        // TODO: O(n^2) iterative search. Needs improvement. Ideas: Hash array, make sure no collisions. "Sort" array and then compare linearly.
        // TODO: ...but, may not be able to make those improvements! Because fitness is now comparing across several axes
        for (int i = 0; i < genes.length; ++i) {
            // ******************** Fitness calculations that only calculated based on ONE gene ********************

            // Combines hard constraints related to an invalid venue:
            // 1. Cannot schedule a module in a venue smaller than the total number of enrolled students (in all the courses that offer this module)
            // 2. Classroom type must be correct: classroom, computer lab, FUTURE: chemistry lab, physics lab, conference room, etc. Room with projector, with chalkboard, etc.
            if (!genes[i].isInValidVenue()) {
                fitnessFromInvalidVenues -= ONE_HARD_CONSTRAINT;
                isValidSolution = false;
            }

            // *****************************************************************************************************

            for (int j = i + 1; j < genes.length; ++j) {
                if (i != j) {
                    // ******************** Fitness calculations that require comparing EVERY OTHER gene ********************

                    // Combines hard constraints related to conflicting timeslots:
                    // 1. Cannot schedule any module in the same time and place (timeslot + venue)
                    // 2. Cannot schedule modules within one course for the same time (timeslot + course)
                    if (genes[i].conflictsWithTimeOrPlaceOf(genes[j])) {
                        fitnessFromOverlappingClasses -= ONE_HARD_CONSTRAINT;
                        isValidSolution = false;
                    }

                    // ******************************************************************************************************
                }
            }
        }

        return fitnessFromOverlappingClasses
                + fitnessFromInvalidVenues;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(cachedFitness).append(": ");
        for (ScheduledModule course : genes) {
            s.append(course.toString()).append(", ");
        }

        return s.toString();
    }

    public int compareTo(Chromosome o) {
        return o.getCachedFitness() - cachedFitness;
    }

    public int getCachedFitness() {
        return cachedFitness;
    }

    public boolean isValidSolution() {
        return isValidSolution;
    }

    public ScheduledModule[] getGenes() {
        return genes;
    }
}
