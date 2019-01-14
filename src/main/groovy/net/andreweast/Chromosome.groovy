package net.andreweast

class Chromosome implements Comparable<Chromosome> {
//class Chromosome {
    ScheduledCourse[] courses
    int storedFitness = -1
    boolean isValidSolution

    static Random random = new Random()

    Chromosome() {
        courses = new ScheduledCourse[Course.allCourses.size()]
        Course.allCourses.size().times { i->
            courses[i] = new ScheduledCourse(Course.allCourses[i])
        }

        storedFitness = calculateFitness()
    }

    Chromosome(Chromosome toClone) {
        courses = new ScheduledCourse[Course.allCourses.size()]
        Course.allCourses.size().times { i->
            courses[i] = new ScheduledCourse(
                toClone.courses[i].getCourse(),
                toClone.courses[i].getVenue(),
                toClone.courses[i].getTimeSlot()
            )
        }

        storedFitness = calculateFitness()
    }

    void crossover(Chromosome toCrossWith) {
        int crossoverPoint = random.nextInt(courses.length)
        if (Schedule.DEBUG) {
            println "Before cross: " + this.toString()
            println "Crossing w/:  " + toCrossWith.toString()
            println "CrossoverPoint=${crossoverPoint}"
        }
        for (int i = 0; i <= crossoverPoint; ++i) {
            courses[i] = toCrossWith.courses[i].clone()
        }
        storedFitness = calculateFitness()
        if (Schedule.DEBUG) {
            println "After cross:  " + this.toString()
        }
    }

    void mutate() {
        // randomise one of the scheduled courses
        if (Schedule.DEBUG) {
            println "Before mutate: " + this.toString()
        }
        int mutateGene = random.nextInt(courses.length)
        courses[mutateGene] = new ScheduledCourse(courses[mutateGene].course)
        storedFitness = calculateFitness()
        if (Schedule.DEBUG) {
            println "@${mutateGene} After mutate:  " + this.toString()
        }
    }

    int calculateFitness() {
        // Weight of any "required" fitness is 100, such as two courses overlapping
        // Weight of any "preferable" fitness is 1, such as keeping a 2-hour lecture as one block vs. two blocks or lecture not being at 8am
        // TODO: Tweak these fitness weights

        isValidSolution = true

        int fitnessFromOverlappingClasses = Course.allCourses.size() * 100
        // TODO: O(n^2) iterative search. Needs improvement. Ideas: Hash array, make sure no collisions. "Sort" array and then compare linearly.
        for (int i = 0; i < courses.size(); ++i) {
            for (int j = i + 1; j < courses.size(); ++j) {
                if (i != j) {
                    if (courses[i].equals(courses[j])) {
                        fitnessFromOverlappingClasses -= 100
                        isValidSolution = false
                    }
                }
            }
        }

        int sumFitness = fitnessFromOverlappingClasses

        sumFitness
    }

    String toString() {
        StringBuilder s = new StringBuilder()
        s.append(storedFitness).append(': ')
        courses.each {
            s.append(it.toString()).append(', ')
        }
        s.toString()
    }

    int compareTo(Chromosome o) {
        o.storedFitness - storedFitness
    }
}
