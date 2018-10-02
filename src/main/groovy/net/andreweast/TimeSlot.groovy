package net.andreweast

class TimeSlot {
    DayOfWeek dayOfWeek
    StartTime startTime

    static Random random = new Random()

    static TimeSlot getRandomTimeSlot() {
        new TimeSlot(
            dayOfWeek: DayOfWeek.getRandomDayOfWeek(),
            startTime: StartTime.getRandomStartTime()
        )
    }

    String toString() {
        "${dayOfWeek.name()}${startTime.name()}"
    }

    enum DayOfWeek {
        M, T, W, R, F

        static DayOfWeek getRandomDayOfWeek() {
            values()[TimeSlot.random.nextInt(values().length)]
        }
    }

    enum StartTime {
//        Nine, Ten, Eleven, Twelve, One, Two, Three, Four, Five
        a09, a10, a11, p12, p13, p14, p15, p16, p17

        static StartTime getRandomStartTime() {
            values()[TimeSlot.random.nextInt(values().length)]
        }
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        TimeSlot timeSlot = (TimeSlot) o

        if (dayOfWeek != timeSlot.dayOfWeek) return false
        if (startTime != timeSlot.startTime) return false

        return true
    }
}
