package net.andreweast.api;

import net.andreweast.entity.Course;
//import net.andreweast.entity.DAO;
import net.andreweast.entity.Department;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/course")
public class CourseEndpoint {

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response create(Course course) {
//        System.out.println("CREATE A USER MOCK");
//        System.out.println(course);
//        if (course.getDepartment() != null) {
//            System.out.println(course.getDepartment()); // DEBUG: this is an object, but has deptId=0 when it should be 2. Might be an aspect of lazy loading?
//        }
//
//        // DEBUG: Theory: this is not needed. What i've got is a problem for how JSON is coming in
//        // DEBUG: Evidence: above sout says that deptId == 0
////        DAO<Department> deptDao = new DAO<>(Department.class);
//////        Department dept = deptDao.fetchById(course.getDepartment());
//////        deptDao.update(course.getDepartment());
////        Department dept = deptDao.fetchById(course.getDepartment().getDepartmentId());
////        course.setDepartment(dept);
//
//        DAO<Course> dao = new DAO<>(Course.class);
//        dao.update(course);
//        // DEBUG: can't update because Dept isn't handled: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - update the transient instance before flushing : net.andreweast.entity.Course.department -> net.andreweast.entity.Department
//
//        return Response.status(Response.Status.CREATED).build();
//    }
//
//// DEBUG: Injection CANNOT be used in Tomcat. Not supported since no full JavaEE, EJB support. See: https://stackoverflow.com/a/11261867/5271224
////    @PersistenceContext(unitName = "java:comp/env/jdbc/postgres")
////    @PersistenceContext(unitName = "PostgresPersistenceUnit")
////    private EntityManager em;
////    private EntityManager injectedEntityManager;
//
////    @POST
////    @Produces(MediaType.TEXT_PLAIN)
////    public String testWrite() {
////        Course course = new Course();
////        course.setName("4BA");
////        Department dept1 = new Department();
////        dept1.setDepartmentId(1);
////        course.setDepartment(dept1);
////        System.out.println("Made a course object: " + course);
////
////        DAO<Course> dao = new DAO<>();
////        dao.update(course);
////
////        return "Created";
////    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
////    public Response getAll() {
//    public List<Integer> getAll() {
////        DAO<Course> dao = new DAO<>();
////        List<Course> allCourses = dao.fetchAll();
//
//        // Mock:
//        List<Integer> allCourses = new ArrayList<>();
//        allCourses.add(4);
//        allCourses.add(3);
//        allCourses.add(1);
////        return Response.ok().entity(allCourses).build();
//        return allCourses;
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{course_id}")
    public Course getVenue(@PathParam("course_id") int id) {
//        System.out.println("Looking for course id=" + id);
//        DAO<Course> dao = new DAO<>(Course.class);
//        Course course = dao.fetchById(id);
//        System.out.println("Found!: " + course.getName() + " in department " + course.getDepartment().getName());
////        return "Found!: " + course.getName() + " in department " + course.getDepartment().getName();
//
//        // For return type Response
////        return Response.ok(course).build();
////        return Response.ok().entity(course).build();
//
//        return course;
//
////        System.out.println("Attempting to access a course");
////        System.out.println("ID=" + id);
////        EntityManager em = LocalEntityManagerFactory.createEntityManager();
////        System.out.println("Got an entity manager from factory");
////        try {
////            System.out.println("Finding");
////            Course course = em.find(Course.class, id);
////            // TODO: Catch not found. Doesn't look like there's an exception, but may just have to check for null
////            return "Found!: " + course.getName() + " in department " + course.getDepartment().getName();
////        } finally {
////            System.out.println("Closing");
////            em.close();
////        }

//        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        SessionFactory factory = new Configuration().configure()
//                .addAnnotatedClass(Department.class)
//                .addAnnotatedClass(Course.class)
                .buildSessionFactory();
        Session session = factory.openSession();

//        EntityManager em = LocalEntityManagerFactory.createEntityManager();
//        Session session = em.unwrap(Session.class); // Getting a session object using JTA api-compliant method: https://www.theserverside.com/tip/How-to-get-the-Hibernate-Session-from-the-JPA-20-EntityManager

        Transaction tx = session.beginTransaction();
        try {
//            T found = em.find(clazz, id);
            Course course = session.get(Course.class, id);
            Hibernate.initialize(course.getDepartment());
            System.out.println("Found!: " + course.getName() + " in department " + course.getDepartment().getName());
//        return "Found!: " + course.getName() + " in department " + course.getDepartment().getName();
            // TODO: How to catch not found? Doesn't look like there's an exception, but may just have to check for null
            if (course == null) {
//                throw new NotFoundException // TODO
                throw new RuntimeException();
            } else {
                return course;
            }
        } finally {
//            tx.commit();
            session.getTransaction().commit();
            session.close();
//            em.close();
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

