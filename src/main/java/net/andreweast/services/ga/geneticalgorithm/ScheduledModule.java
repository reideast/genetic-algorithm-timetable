package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;

public class ScheduledModule implements Cloneable, Serializable {
    private GeneticAlgorithmJobData data;

    Module module;
    Venue venue;
    Timeslot timeslot;

    @Override
    public String toString() {
        return "ScheduledModule{" + module + venue + timeslot + '}';
    }

    /**
     * Randomising constructor
     */
    public ScheduledModule(Module module, GeneticAlgorithmJobData masterData) {
        data = masterData;

        this.module = module;
        this.venue = data.getRandomVenue();
        this.timeslot = data.getRandomTimeslot();
    }

    /**
     * Cloning constructor
     */
    public ScheduledModule(Module module, Venue venue, Timeslot timeslot, GeneticAlgorithmJobData masterData) {
        data = masterData;

        this.module = module;
        this.venue = venue;
        this.timeslot = timeslot;
    }

    public ScheduledModule clone() {
        return new ScheduledModule(this.module, this.venue, this.timeslot, this.data);
    }

    /**
     * Compare two genes. If they overlap in both venue and timeslot, there is a conflict! Return true
     * If they overlap in JUST timeslot and the two modules are offered BY THE SAME COURSE, return true
     */
    public boolean conflictsWith(ScheduledModule that) {
        if (this == that) return true;

        if (timeslot == that.timeslot) {
            // Check if scheduled at same time AND place
            if (venue == that.venue) {
                return true;
            } else {
                // Check if a time conflict is also for two modules within the same course. If not, the overlap doesn't matter
                if (module.offeredBySameCourse(that.module)) {
                    return true;
                }
            }
        }


        return false;
    }

    public Module getModule() {
        return module;
    }

    public Venue getVenue() {
        return venue;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }
}
