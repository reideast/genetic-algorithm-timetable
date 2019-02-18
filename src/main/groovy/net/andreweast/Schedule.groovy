package net.andreweast

class Schedule {
    static final int GENERATION_LIMIT = 300

    static final boolean DEBUG = true

    Population population

//    static void main(String[] args) {
////        Schedule schedule = new Schedule()
////        schedule.run() // TODO: threaded?
//
//        Thread t = new Thread(new Schedule())
//        t.start()
//    }

    Schedule() {
        population = new Population()
    }

    String schedule() {
        StringBuilder s = new StringBuilder()
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
                s.append("${population.toFitnessList()}<br>")
//            }

            if (i == 0) {
                runningAverage = System.nanoTime() - startTime
            } else {
                runningAverage = runningAverage + (((System.nanoTime()  - startTime) - runningAverage) / (i + 1 as double))
            }
        }

        s.append("Complexity of dataset:<br>")
        s.append("Courses: ${Course.allCourses.length}<br>")
        s.append("Venues x TimeSlots: ${Venue.Room.values().length * TimeSlot.DayOfWeek.values().length * TimeSlot.StartTime.values().length}<br>")

        s.append("\nRunning time stats:<br>")
        s.append("Time init: ${initTime * 1.0E-6} ms<br>")
        s.append("Average generation time: ${runningAverage * 1E-6} ms<br>")

        return s.toString()
    }
}
