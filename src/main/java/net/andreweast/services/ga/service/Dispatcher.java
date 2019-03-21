package net.andreweast.services.ga.service;

import net.andreweast.services.ga.dataaccess.JobDao;

public class Dispatcher implements Runnable {
    private Long scheduleId;
    private Long jobId;

    public Dispatcher(Long scheduleId, Long jobId) {
        this.scheduleId = scheduleId;
        this.jobId = jobId;
    }

    @Override
    public void run() {
        JobDao allGAJobData = GeneticAlgorithmDeserializer.getScheduleData(this.scheduleId);

        try {
            // TODO: Actually run the job!
            Thread.sleep(10 * 1000);
            System.out.println("Job's done, m'lord!"); // DEBUG
        } catch (InterruptedException e) {
            // There's no way to deal this anymore...the REST call has already returned!
            e.printStackTrace();
        }

        GeneticAlgorithmSerializer.writeScheduleData(allGAJobData, scheduleId);
    }
}
