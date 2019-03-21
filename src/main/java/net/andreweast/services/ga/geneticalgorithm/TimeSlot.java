package net.andreweast.services.ga.geneticalgorithm;

import java.io.Serializable;
import java.util.Random;

public class TimeSlot implements Serializable {
    public static TimeSlot getRandomTimeSlot() {
        TimeSlot slot = new TimeSlot();
        slot.setDayOfWeek(DayOfWeek.getRandomDayOfWeek());
        slot.setStartTime(StartTime.getRandomStartTime());
        return slot;
    }

    public String toString() {
        return getDayOfWeek().name() + getStartTime().name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
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

    enum DayOfWeek {
        M, T, W, R, F;

        public static DayOfWeek getRandomDayOfWeek() {
            return values()[TimeSlot.random.nextInt(values().length)];
        }

    }

    enum StartTime {
        a09, a10, a11, p12, p13, p14, p15, p16, p17;

        public static StartTime getRandomStartTime() {
            return values()[TimeSlot.random.nextInt(values().length)];
        }

    }
}
