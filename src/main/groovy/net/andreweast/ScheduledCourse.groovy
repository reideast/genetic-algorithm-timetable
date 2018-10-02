package net.andreweast

class ScheduledCourse {
    Course course

    Venue venue
    TimeSlot timeSlot

    ScheduledCourse(Course course) {
        this.course = course
        this.venue = Venue.getRandomVenue()
        this.timeSlot = TimeSlot.getRandomTimeSlot()
    }

    ScheduledCourse(Course course, Venue venue, TimeSlot timeSlot) {
        this.course = course
        this.venue = venue
        this.timeSlot = timeSlot
    }

    ScheduledCourse clone() {
        new ScheduledCourse(this.course, this.venue, this.timeSlot)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ScheduledCourse that = (ScheduledCourse) o

        if (timeSlot != that.timeSlot) return false
        if (venue != that.venue) return false

        return true
    }

    String toString() {
        "${course}:${venue}-${timeSlot}"
    }
}
