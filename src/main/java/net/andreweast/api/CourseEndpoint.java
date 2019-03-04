package net.andreweast.api;

import net.andreweast.entity.CoursesEntity;
import net.andreweast.listener.LocalEntityManagerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Path("/course")
public class CourseEndpoint {

// DEBUG: Injection CANNOT be used in Tomcat. Not supported since no full JavaEE, EJB support.
//    @PersistenceContext(unitName = "java:comp/env/jdbc/postgres")
//    @PersistenceContext(unitName = "PostgresPersistenceUnit")
//    private EntityManager em;
//    private EntityManager injectedEntityManager;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String testWrite() {
        CoursesEntity course = new CoursesEntity();
        course.setName("4BA");
        course.setDepartmentId(1);
        System.out.println("Made a course object: " + course);

        EntityManager em = LocalEntityManagerFactory.createEntityManager();
//        Transaction tx = null;

        try {
//            Session session = em.unwrap(Session.class);
//            tx = session.beginTransaction();
            em.getTransaction().begin();
//            em.flush(); // Flush is required for PostgreSQL tables with SEQUENCE primary key types
            em.persist(course); // DEBUG: Looks like flush is not required when transactions are used
//            session.save(course); // DEBUG: Is save equivalent to persist?
//            tx.commit();
            em.getTransaction().commit();
        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
            em.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            em.close();
        }

        return "Created";
    }

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("{course_id}")
//    public String getVenue(@PathParam("course_id") int id) {
//        System.out.println("Attempting to access a course");
//        System.out.println("ID=" + id);
//        EntityManager em = LocalEntityManagerFactory.createEntityManager();
//        System.out.println("Got an entity manager from factory");
//        try {
//            System.out.println("Finding");
//            CoursesEntity course = em.find(CoursesEntity.class, id);
//            // TODO: Catch not found. Doesn't look like there's an exception, but may just have to check for null
//            return "Found!: " + course.getName();
//        } finally {
//            System.out.println("Closing");
//            em.close();
//        }
//    }




//    @Resource(name = "java:comp/env/jdbc/postgres", type = DataSource.class)
//    private DataSource ds;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{course_id}")
    public String getVenue(@PathParam("course_id") int id) {

//        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
//        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.catalina.util.naming");
//        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        System.out.println("Attempting to access a course");
        System.out.println("ID=" + id);
        try
        {
            String rtn;

            Context ctx = new InitialContext();
//            Context env = (Context) ctx.lookup("java:comp/env");

            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/postgres");
//            DataSource ds = (DataSource) ctx.lookup("jdbc/postgres");

            Connection conn = ds.getConnection();

            rtn = "Got Connection " + conn.toString() + "<br>";
            Statement stmt = conn.createStatement();
            ResultSet rst = stmt.executeQuery("select name from courses where course_id = " + id);

            if (rst.next()) {
                rtn += "Found!: " + rst.getString(1);
            }

            conn.close();

            return rtn;
        } catch (SQLException  e) {
            throw new RuntimeException(e);
        } catch (NamingException  e) {
            throw new RuntimeException(e);
        }
    }

    /*

        VenuesEntity venue = new VenuesEntity();
        venue.setName("FromJava" + System.currentTimeMillis());
        venue.setCapacity(20);
        venue.setLab(false);
     */
}

