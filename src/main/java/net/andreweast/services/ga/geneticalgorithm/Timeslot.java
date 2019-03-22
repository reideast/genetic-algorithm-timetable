package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

public class Timeslot implements Serializable {
    // Database ID
    long id;

    // M-Su, 0-6
    int day;
    // Time slots, integer by the hour. Usually 8-18 (8:00 AM - 6:00 PM)
    int time;

    // TODO: store set of preferences for each lecturer for this timeslot. Fitness can then look up the score for a module's lecturer to teach in this timeslot
    HashMap<Long, Integer> lecturerPreferences;

    public Timeslot(long id, int day, int time, HashMap<Long, Integer> lecturerPreferences) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.lecturerPreferences = lecturerPreferences;
    }

    @Override
    public String toString() {
        return "Timeslot{" +
                day + ":" + time + "," +
                "prefs=" + lecturerPreferences.size() +
                '}';
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public HashMap<Long, Integer> getLecturerPreferences() {
        return lecturerPreferences;
    }

    public void setLecturerPreferences(HashMap<Long, Integer> lecturerPreferences) {
        this.lecturerPreferences = lecturerPreferences;
    }

    public static Timeslot getRandomTimeSlot() {
        Timeslot slot = new Timeslot();
        slot.setDayOfWeek(DayOfWeek.getRandomDayOfWeek());
        slot.setStartTime(StartTime.getRandomStartTime());
        return slot;
    }

    public Timeslot() {
    }

//    public String toString() {
//        return getDayOfWeek().name() + getStartTime().name();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeSlot = (Timeslot) o;
        return dayOfWeek == timeSlot.dayOfWeek &&
                startTime == timeSlot.startTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public StartTime getStartTime() {
        return startTime;
    }

    public void setStartTime(StartTime startTime) {
        this.startTime = startTime;
    }

    private DayOfWeek dayOfWeek;
    private StartTime startTime;
    private static Random random = new Random();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    enum DayOfWeek {
        M, T, W, R, F;

        public static DayOfWeek getRandomDayOfWeek() {
            return values()[Timeslot.random.nextInt(values().length)];
        }

    }

    enum StartTime {
        a09, a10, a11, p12, p13, p14, p15, p16, p17;

        public static StartTime getRandomStartTime() {
            return values()[Timeslot.random.nextInt(values().length)];
        }

    }
}
