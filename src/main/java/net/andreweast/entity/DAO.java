package net.andreweast.entity;

import net.andreweast.listener.LocalEntityManagerFactory;

import javax.persistence.EntityManager;

public class DAO<T> {
    public void save(T dataObject) {
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
//        Transaction tx = null;

        try {
//            Session session = em.unwrap(Session.class); // Getting a session object using JTA api-compliant method: https://www.theserverside.com/tip/How-to-get-the-Hibernate-Session-from-the-JPA-20-EntityManager
//            tx = session.beginTransaction();
            em.getTransaction().begin();
//            em.flush(); // Flush is required for PostgreSQL tables with SEQUENCE primary key types
            em.persist(dataObject); // DEBUG: Looks like flush is not required when transactions are used
//            session.save(course); // DEBUG: Is save equivalent to persist?
//            tx.commit();
            em.getTransaction().commit();
        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback();
//            }
            em.getTransaction().rollback();
            throw new RuntimeException(e); // TODO
        } finally {
            em.close();
        }
    }
}
