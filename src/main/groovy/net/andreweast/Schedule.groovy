package net.andreweast

class Schedule implements Runnable {
    static final int GENERATION_LIMIT = 300

    static final boolean DEBUG = true

    Population population

    static void main(String[] args) {
//        Schedule schedule = new Schedule()
//        schedule.run() // TODO: threaded?

        Thread t = new Thread(new Schedule())
        t.start()
    }

    Schedule() {
        population = new Population()
    }

    void run() {
        long startTime = System.nanoTime()

        println "************* GEN init *************"
        println population
        long initTime = System.nanoTime() - startTime

        double runningAverage = -1
        // TODO: Run with GENERATION_LIMIT as a maxium, but stop if an acceptable solution is found
        GENERATION_LIMIT.times { i ->
            startTime = System.nanoTime()
//            println "************* GEN $i *************"

            population.select()

            population.crossover()
//            population.selectWithSamePopulation()

            population.mutate()

//            if (DEBUG) {
                // println population
                println population.toFitnessList()
//            }

            if (i == 0) {
                runningAverage = System.nanoTime() - startTime
            } else {
                runningAverage = runningAverage + (((System.nanoTime()  - startTime) - runningAverage) / (i + 1 as double))
            }
        }

        println()
        println "Complexity of dataset:"
        println "Courses: ${Course.allCourses.length}"
        println "Venues x TimeSlots: ${Venue.Room.values().length * TimeSlot.DayOfWeek.values().length * TimeSlot.StartTime.values().length}"

        println()
        println "Running time stats:"
        println "Time init: ${initTime * 1.0E-6} ms"
        println "Average generation time: ${runningAverage * 1E-6} ms"
    }
}
