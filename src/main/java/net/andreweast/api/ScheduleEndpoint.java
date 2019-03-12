package net.andreweast.api;

import net.andreweast.geneticalgorithm.Schedule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/schedule")
public class ScheduleEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getSchedule() {
        Schedule scheduler = new Schedule();
        return scheduler.schedule();
        // TODO: can return a POJO, and all getField() methods will expand into the JSON
    }
}
