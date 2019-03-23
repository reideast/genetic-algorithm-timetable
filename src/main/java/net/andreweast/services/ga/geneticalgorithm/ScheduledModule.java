package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;

public class ScheduledModule implements Cloneable, Serializable {
    private GeneticAlgorithmJobData data;

    Module module;
    Venue venue;
    Timeslot timeSlot;

    @Override
    public String toString() {
        return "ScheduledModule{" +
                "module=" + module +
                ", venue=" + venue +
                ", timeSlot=" + timeSlot +
                '}';
    }

    /**
     * Randomising constructor
     */
    public ScheduledModule(Module module, GeneticAlgorithmJobData masterData) {
        data = masterData;

        this.module = module;
        this.venue = data.getRandomVenue();
        this.timeSlot = data.getRandomTimeslot();
    }

    /**
     * Cloning constructor
     */
    public ScheduledModule(Module module, Venue venue, Timeslot timeSlot, GeneticAlgorithmJobData masterData) {
        this.module = module;
        this.venue = venue;
        this.timeSlot = timeSlot;
    }

    public ScheduledModule clone() {
        return new ScheduledModule(this.module, this.venue, this.timeSlot, this.data);
    }

    /**
     * Compare two genes. If they overlap in both venue and timeslot, there is a conflict! Return true
     */
    public boolean conflict(ScheduledModule that) {
        if (this == that) return true;
        return venue == that.venue &&
                timeSlot == that.timeSlot;
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
