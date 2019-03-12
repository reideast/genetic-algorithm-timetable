package net.andreweast.bean;

import net.andreweast.geneticalgorithm.Schedule;

public class ScheduleBean {
    public ScheduleBean() {

    }

    public String getSchedule() {
//        Thread t = new Thread(new Schedule());
//        t.start();
//        t.join();
        Schedule scheduler = new Schedule();
        return scheduler.schedule();
//        return "foo";
    }
}
