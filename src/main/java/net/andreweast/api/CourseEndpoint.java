package net.andreweast.api;

import net.andreweast.entity.CoursesEntity;
import net.andreweast.entity.VenuesEntity;
import net.andreweast.listener.LocalEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/course")
public class CourseEndpoint {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{course_id}")
    public String getVenue(@PathParam("course_id") int id) {
        System.out.println("Attempting to access a course");
        System.out.println("ID=" + id);
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        System.out.println("Got an entity manager from factory");
        try {
            System.out.println("Finding");
            CoursesEntity course = em.find(CoursesEntity.class, id);
            // TODO: Catch not found. Doesn't look like there's an exception, but may just have to check for null
            return "Found!: " + course.getName();
        } finally {
            System.out.println("Closing");
            em.close();
        }
    }
    /*

        VenuesEntity venue = new VenuesEntity();
        venue.setName("FromJava" + System.currentTimeMillis());
        venue.setCapacity(20);
        venue.setLab(false);
     */
}

