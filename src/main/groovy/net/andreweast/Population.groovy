package net.andreweast

class Population {
    static final int NUM_INDIVIDUALS = 20
    static final Random random = new Random()

    Chromosome[] individuals

    Population() {
        individuals = new Chromosome[NUM_INDIVIDUALS]
        NUM_INDIVIDUALS.times { i ->
            individuals[i] = new Chromosome()
        }
    }

    /**
     * Select a new population with the "roulette wheel" method
     * The fittest individuals will tend to be selected more often
     */
    void select() {
        Chromosome[] nextPopulation = new Chromosome[NUM_INDIVIDUALS]

        // Determine sum total of all individuals' fitness s.t. roulette wheel can select from them
//        int totalFitness = individuals.sum {
//            it.getStoredFitness()
//        }
        int totalFitness = 0
        for (int i = 0; i < NUM_INDIVIDUALS; ++i) {
            totalFitness += individuals[i].getStoredFitness()
        }

        // Select a whole new population
        if (Schedule.DEBUG) {
            print "Building new population: "
        }
        NUM_INDIVIDUALS.times { i ->
            // "Spin the roulette wheel". Based on https://en.wikipedia.org/wiki/Fitness_proportionate_selection
            int randomSelected = random.nextInt(totalFitness)
            if (Schedule.DEBUG) {
                print "$i($randomSelected:"
            }

            // For each individual, starting with first, if random number was less than its proportion, then roulette wheel "landed" on this item
            for (int individual = 0; individual < NUM_INDIVIDUALS; ++individual) {
                // Subtract this individual's fitness, so the next individual's fitness will be the closest to zero
                randomSelected -= individuals[individual].getStoredFitness()
                if (randomSelected < 0) {
                    nextPopulation[i] = new Chromosome(individuals[individual]) // deep clone individual
                    if (Schedule.DEBUG) {
                        print "$individual) "
                    }
                    break
                }
            }
        }
        if (Schedule.DEBUG) {
            println()
        }

        // DEBUG:
        for (int i = 0; i < NUM_INDIVIDUALS; ++i) {
            if (nextPopulation[i] == null) {
                throw new Exception("ERROR: individual #$i was null!")
            }
        }

        individuals = nextPopulation
    }

    /**
     * Simply select by keeping the same population and drop the lowest (replaced by a crossover offspring of the two fittest)
     */
    void selectWithSamePopulation() {
        // find highest and second highest
        // Done via a sort. This should only be especially costly on the first generation, since Arrays.sort does an in-place sort
        Arrays.sort(individuals)
//        individuals.sort{ a, b ->
//            b.storedFitness - a.storedFitness
//        }

        Chromosome first = individuals[0]
        Chromosome second = individuals[1]

        // create a new one cloned from highest
        Chromosome offspring = new Chromosome(first)

        // swap some genes with second highest
        // use random crossover point
        offspring.crossover(second)

        // find lowest
        // covered by sort

        // replace lowest with new offspring
        individuals[individuals.length - 1] = offspring
    }

    void crossover() {
        // Crossover with p = 0.6
        if (random.nextInt(100) < 60) {
            // TODO: Rather naive method of choosing best: Just sort the population by fitness
            Arrays.sort(individuals)
            Chromosome first = individuals[0]
            Chromosome second = individuals[1]

            // create a new one cloned from highest
            Chromosome offspring = new Chromosome(first)

            // swap some genes with second highest
            offspring.crossover(second)

            // replace lowest individual with new offspring
            individuals[individuals.length - 1] = offspring
        }
    }

    void mutate() {
//        // Mutate a random individual with p = 0.01
//        if (random.nextInt(100) == 0) {
        // Mutate a random individual with p = 0.2
        if (random.nextInt(100) < 90) {
            individuals[random.nextInt(NUM_INDIVIDUALS)].mutate()
        }
    }

    String toString() {
        StringBuilder s = new StringBuilder()
        individuals.each {
            s.append(it.toString()).append('\n')
        }
        s.toString()
    }

    List<Integer> toFitnessList() {
        individuals.collect {
            it.getStoredFitness()
        }
    }
}
