package net.andreweast

class Chromosome implements Comparable<Chromosome> {
//class Chromosome {
    ScheduledCourse[] courses
    int storedFitness

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
        println "Before cross: " + this.toString()
        println "Crossing w/:  " + toCrossWith.toString()
        int crossoverPoint = random.nextInt(courses.length)
        println "CrossoverPoint=${crossoverPoint}"
        for (int i = 0; i <= crossoverPoint; ++i) {
            courses[i] = toCrossWith.courses[i].clone()
        }
        storedFitness = calculateFitness()
        println "After cross:  " + this.toString()
    }

    void mutate() {
        // randomise one of the scheduled courses
        println "Before mutate: " + this.toString()
        int mutateGene = random.nextInt(courses.length)
        courses[mutateGene] = new ScheduledCourse(courses[mutateGene].course)
        storedFitness = calculateFitness()
        println "@${mutateGene} After mutate:  " + this.toString()
    }

    int calculateFitness() {
        int fitness = 0
        // TODO: O(n^2) iterative search. Needs improvement. Ideas: Hash array, make sure no collisions. "Sort" array and then compare linearly.
        for (int i = 0; i < courses.size(); ++i) {
            for (int j = i + 1; j < courses.size(); ++j) {
                if (i != j) {
                    if (courses[i].equals(courses[j])) {
                        fitness -= 1
                    }
                }
            }
        }

        fitness
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
