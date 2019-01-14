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

    void select() {
        // find highest and second highest
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
        // Mutate a random individual with p = 0.01
        if (random.nextInt(100) == 0) {
            if (Schedule.DEBUG) {
                println "Mutating!"
            }
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
