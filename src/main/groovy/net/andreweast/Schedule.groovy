package net.andreweast

class Schedule implements Runnable {
    static final int GENERATION_LIMIT = 100

    Population population

    static void main(String[] args) {
        Schedule schedule = new Schedule()
        schedule.run() // TODO: threaded?
    }

    Schedule() {
        population = new Population()
    }

    void run() {
        long startTime = System.currentTimeMillis()

        println "************* GEN init *************"
        println population
        long initTime = System.currentTimeMillis() - startTime

        double runningAverage = -1
        def averages = []
        GENERATION_LIMIT.times { i ->
            startTime = System.currentTimeMillis()
            println "************* GEN $i *************"
            population.select()
            population.mutate()
            println population
            if (i == 0) {
                runningAverage = System.currentTimeMillis() - startTime
            } else {
                runningAverage = runningAverage + (((System.currentTimeMillis() - startTime) - runningAverage) / (i + 1 as double))
            }
            averages.add(System.currentTimeMillis() - startTime)
        }

        println "Courses: ${Course.allCourses.length}"
        println "Venues x TimeSlots: ${Venue.Room.values().length * TimeSlot.DayOfWeek.values().length * TimeSlot.StartTime.values().length}"

        println "Time init: ${initTime} ms"
        println "Average generation time: ${runningAverage} ms"

        println "Avg from array=${(averages.sum() / (averages.size() as double))}"
        println averages
    }
}
