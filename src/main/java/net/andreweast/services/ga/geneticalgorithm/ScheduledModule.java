package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;

public class ScheduledModule implements Cloneable, Serializable {
    private GeneticAlgorithmJobData data;

    Module module;
    Venue venue;
    Timeslot timeslot;

    @Override
    public String toString() {
        return "ScheduledModule{" + module + "," + venue + "," + timeslot + '}';
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
     * If they overlap in JUST timeslot but the two modules are offered BY THE SAME COURSE, return true
     * If they overlap in JUST timeslot and the two modules are taught by the same lecturer, return true
     *
     * @return true if these two modules should not both be taught in the same timeslot. False means no conflicts
     */
    public boolean conflictsWithTimeOrPlaceOrLecturerOf(ScheduledModule that) {
        if (this == that) return true;

        if (timeslot == that.timeslot) {
            // Check if scheduled at same time AND place
            if (venue == that.venue) {
                return true;
            }

            // Check if the lecturer is already busy this hour
            if (module.taughtByTheSameLecturer(that.module)) {
                return true;
            }

            // Check if a time conflict is also for two modules within the same course. If not, the overlap doesn't matter
            // Done last, since it is most likely the worst performance test
            if (module.offeredBySameCourse(that.module)) {
                return true;
            }
        }

        return false; // No conflict!
    }

    /**
     * For this gene, has it been assigned to a lecture/lab venue that is proper?
     *
     * @return true if the module can be successfully taught within this venue
     */
    public boolean isInValidVenue() {
        if (this.module.isLab != this.venue.isLab) return false;
        if (this.venue.capacity < this.module.numEnrolled) return false;
        return true;
    }

    public static final int MAX_BUILDING_PREF_SCORE = 20;
    public float getDepartmentsBuildingPreferenceAverage() {
        int scoreSum = 0;
        for (Long departamentId : module.getDepartmentIds()) {
            scoreSum += venue.getDepartmentsScores().getOrDefault(departamentId, 10); // If the key does not exist, then count it as 10, or "half"
        }
        float avg = (float) scoreSum / module.getDepartmentIds().size();
        if (avg > 20) {
            System.out.println("An average score was over 20: module=" + module.getId() + ",venue=" + venue.getId());
        }
        return avg;
    }

    public static final int MAX_TIMESLOT_PREF_SCORE = 20;
    public int getLecturerTimeslotPreference() {
        return timeslot.getLecturerPreferences().getOrDefault(module.lecturerId, 10); // If the database field of this lecturer in this timeslot does not exist, then count it as 10, or "half"
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }
}
