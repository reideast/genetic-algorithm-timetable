package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;

public class ScheduledModule implements Cloneable, Serializable {
    Module module;
    Venue venue;
    Timeslot timeSlot;

    public ScheduledModule(Module module, Venue venue, Timeslot timeSlot) {
        this.module = module;
        this.venue = venue;
        this.timeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return "ScheduledModule{" +
                "module=" + module +
                ", venue=" + venue +
                ", timeSlot=" + timeSlot +
                '}';
    }

    public ScheduledModule(Module module) {
        this.module = module;
        this.venue = Venue.getRandomVenue();
        this.timeSlot = Timeslot.getRandomTimeSlot();
    }

    public ScheduledModule clone() {
        return new ScheduledModule(this.module, this.venue, this.timeSlot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduledModule that = (ScheduledModule) o;
        return venue.equals(that.venue) &&
                timeSlot.equals(that.timeSlot);
//        return module.equals(that.module) &&
//                venue.equals(that.venue) &&
//                timeSlot.equals(that.timeSlot);
    }

//    public String toString() {
//        return module.toString() + ":" + venue.toString() + "-" + timeSlot.toString();
//    }

    public Module getModule() {
        return module;
    }

    public Venue getVenue() {
        return venue;
    }

    public Timeslot getTimeSlot() {
        return timeSlot;
    }
}
