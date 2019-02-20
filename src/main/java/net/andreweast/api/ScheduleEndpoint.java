package net.andreweast.api;

import net.andreweast.Schedule;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/schedule")
public class ScheduleEndpoint {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getSchedule() {
        List<String> data = new ArrayList<>();
        data.add("Hello");
        data.add("World");
        data.add("schedule API");
        return data;
    }
}
