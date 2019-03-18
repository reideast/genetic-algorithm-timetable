//package net.andreweast.entity;
//
//import net.andreweast.listener.LocalEntityManagerFactory;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//public class DAO<T> {
//    Class<T> clazz;
//
//    public DAO(Class<T> clazz) {
//        this.clazz = clazz;
//    }
//
//    public void create(T dataObject) {
//        throw new UnsupportedOperationException();
//    }
//
//    public void update(T dataObject) {
//        EntityManager em = LocalEntityManagerFactory.createEntityManager(); // DEBUG: this is repeated. Move to a class field?
////        Transaction tx = null;
//
//        try {
////            Session session = em.unwrap(Session.class); // Getting a session object using JTA api-compliant method: https://www.theserverside.com/tip/How-to-get-the-Hibernate-Session-from-the-JPA-20-EntityManager
////            tx = session.beginTransaction();
//            em.getTransaction().begin();
////            em.flush(); // Flush is required for PostgreSQL tables with SEQUENCE primary key types
//            em.persist(dataObject); // DEBUG: Looks like flush is not required when transactions are used
////            session.save(course); // DEBUG: Is update equivalent to persist?
////            tx.commit();
//            em.getTransaction().commit();
//        } catch (Exception e) {
////            if (tx != null) {
////                tx.rollback();
////            }
//            em.getTransaction().rollback();
//            throw new RuntimeException(e); // TODO
//        } finally {
//            em.close();
//        }
//    }
//
//    public List<T> fetchAll() {
//        throw new UnsupportedOperationException();
//    }
//
//    public T fetchById(int id) {
//        EntityManager em = LocalEntityManagerFactory.createEntityManager();
//        Session session = em.unwrap(Session.class); // Getting a session object using JTA api-compliant method: https://www.theserverside.com/tip/How-to-get-the-Hibernate-Session-from-the-JPA-20-EntityManager
//        Transaction tx = session.beginTransaction();
//        try {
////            T found = em.find(clazz, id);
//            T found = session.get(clazz, id);
//
//            tx.commit();
//            // TODO: How to catch not found? Doesn't look like there's an exception, but may just have to check for null
//            if (found == null) {
////                throw new NotFoundException // TODO
//                throw new RuntimeException();
//            } else {
//                return found;
//            }
//        } finally {
//            em.close();
//        }
//    }
//
//    public void delete(int id) {
//        throw new UnsupportedOperationException();
//    }
//}
