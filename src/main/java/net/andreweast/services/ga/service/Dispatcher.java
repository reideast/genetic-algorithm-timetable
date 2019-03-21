package net.andreweast.services.ga.service;

import net.andreweast.exception.DataNotFoundException;
import net.andreweast.services.data.model.Job;
import net.andreweast.services.ga.dataaccess.JobDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Dispatcher {
//    private final static GeneticAlgorithmDeserializer geneticAlgorithmDeserializer = new GeneticAlgorithmDeserializer();
//    private final static GeneticAlgorithmSerializer geneticAlgorithmSerializer = new GeneticAlgorithmSerializer();
    // TODO: Perhaps Dispatcher should be doing this work! (That way, GADeserializer can correctly have responsibility for creating a new job)
    // TODO: ...that would require Dispatcher to run in THIS thread, therefore it could throw HTTP errors to disrupt this REST call
    // TODO: And that's OK, that's a more logical separation of concerns
    public static Job dispatchNewJobForSchedule(Long scheduleId) throws DataNotFoundException, ResponseStatusException {
        // Save the to the database that we are starting a new job. Throws HTTP errors if such a job is already running
        Job job = new GeneticAlgorithmDeserializer().createJobForSchedule(scheduleId);

        // Get all the data the job will need from the database
        JobDao allGAJobData = new GeneticAlgorithmDeserializer().getScheduleData(scheduleId);

        // TODO: save a handle to this thread to database (Job entity) s.t. the thread can be cancelled later

        Thread runner = new Thread(() -> {
            try {
                // TODO: Actually run the job!
                Thread.sleep(10 * 1000);
                System.out.println("Job's done, m'lord!"); // DEBUG

                // TODO: This should be done by the class which runs the job, in its thread
                // TODO: Should be in its own method in that class, such that it can be called when job is killed OR when job finishes normally
                new GeneticAlgorithmSerializer().writeScheduleData(allGAJobData, scheduleId);
                new GeneticAlgorithmSerializer().deleteJobForSchedule(scheduleId);
            } catch (InterruptedException e) {
                // There's no way to deal this anymore...the REST call has already returned!
                e.printStackTrace();
            }
        });
        runner.run();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("yourfile.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(runner);
            objectOutputStream.flush();
            objectOutputStream.close();

            FileInputStream fileInputStream = new FileInputStream("yourfile.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Thread deserialized = (Thread) objectInputStream.readObject();
            objectInputStream.close();

            deserialized.stop();
        } catch (Exception e) {
            System.out.println("Foo!");
        }

        return job;
    }

    public static Job getStatusForJob(Long jobId) {
        // TODO: This method should ask the running Job thread for the current generation number it is on
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Checking for job status not implemented", new UnsupportedOperationException());
    }

    public static void stopJobForSchedule(Long jobId) {
        // TODO: This method should stop the Job thread, and then write its results to the DB
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, "Deleting in-progress job not implemented", new UnsupportedOperationException());
    }
}
