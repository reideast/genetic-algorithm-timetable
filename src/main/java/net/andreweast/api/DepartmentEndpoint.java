package net.andreweast.api;

import net.andreweast.entity.Course;
import net.andreweast.entity.Department;
import net.andreweast.listener.LocalEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/department")
public class DepartmentEndpoint {

// DEBUG: Injection CANNOT be used in Tomcat. Not supported since no full JavaEE, EJB support. See: https://stackoverflow.com/a/11261867/5271224
//    @PersistenceContext(unitName = "java:comp/env/jdbc/postgres")
//    @PersistenceContext(unitName = "PostgresPersistenceUnit")
//    private EntityManager em;
//    private EntityManager injectedEntityManager;

//    @POST
//    @Produces(MediaType.TEXT_PLAIN)
//    public String testWrite() {
//        Course course = new Course();
//        course.setName("4BA");
//        Department dept1 = new Department();
//        dept1.setDepartmentId(1);
//        course.setDepartment(dept1);
//        System.out.println("Made a course object: " + course);
//
//        DAO<Course> dao = new DAO<>();
//        dao.update(course);
//
//        return "Created";
//    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("{department_id}")
    public String getDepartment(@PathParam("department_id") int id) {
        System.out.println("Attempting to access a dept");
        System.out.println("ID=" + id);
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        System.out.println("Got an entity manager from factory");
        try {
            System.out.println("Finding");
            Department dept = em.find(Department.class, id);
            // TODO: Catch not found. Doesn't look like there's an exception, but may just have to check for null
            StringBuilder s = new StringBuilder();
            s.append("Found department: ").append(dept.getName()).append("\nWith courses:");
            for (Course course : dept.getCourses()) {
                s.append('\n').append(course.getName());
            }
            return s.toString();
        } finally {
            System.out.println("Closing");
            em.close();
        }
    }




////    @Resource(name = "java:comp/env/jdbc/postgres", type = DataSource.class)
////    private DataSource ds;
//
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("{course_id}")
//    public String getVenue(@PathParam("course_id") int id) {
//
////        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
////        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.catalina.util.naming");
////        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
//
//        System.out.println("Attempting to access a course");
//        System.out.println("ID=" + id);
//        try
//        {
//            String rtn;
//
//            Context ctx = new InitialContext();
////            Context env = (Context) ctx.lookup("java:comp/env");
//
//            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/postgres");
////            DataSource ds = (DataSource) ctx.lookup("jdbc/postgres");
//
//            Connection conn = ds.getConnection();
//
//            rtn = "Got Connection " + conn.toString() + "<br>";
//            Statement stmt = conn.createStatement();
//            ResultSet rst = stmt.executeQuery("select name, from courses where course_id = " + id);
//
//            if (rst.next()) {
//                rtn += "Found!: " + rst.getString(1);
//            }
//
//            conn.close();
//
//            return rtn;
//        } catch (SQLException  e) {
//            throw new RuntimeException(e);
//        } catch (NamingException  e) {
//            throw new RuntimeException(e);
//        }
//    }

    /*

        Venue venue = new Venue();
        venue.setName("FromJava" + System.currentTimeMillis());
        venue.setCapacity(20);
        venue.setLab(false);
     */
}

