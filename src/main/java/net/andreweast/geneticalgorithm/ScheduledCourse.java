package net.andreweast.geneticalgorithm;

public class ScheduledCourse implements Cloneable {
    private Course course;
    private Venue venue;
    private TimeSlot timeSlot;

    public ScheduledCourse(Course course) {
        this.course = course;
        this.venue = Venue.getRandomVenue();
        this.timeSlot = TimeSlot.getRandomTimeSlot();
    }

    public ScheduledCourse(Course course, Venue venue, TimeSlot timeSlot) {
        this.course = course;
        this.venue = venue;
        this.timeSlot = timeSlot;
    }

    public ScheduledCourse clone() {
        return new ScheduledCourse(this.course, this.venue, this.timeSlot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledCourse that = (ScheduledCourse) o;
        return venue.equals(that.venue) &&
                timeSlot.equals(that.timeSlot);
//        return course.equals(that.course) &&
//                venue.equals(that.venue) &&
//                timeSlot.equals(that.timeSlot);
    }

    public String toString() {
        return course.toString() + ":" + venue.toString() + "-" + timeSlot.toString();
    }

    public Course getCourse() {
        return course;
    }

    public Venue getVenue() {
        return venue;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
}
