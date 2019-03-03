package net.andreweast.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class LocalEntityManagerFactory implements ServletContextListener {
    private static EntityManagerFactory factory;

    public static EntityManager createEntityManager() {
        if (factory == null) {
//            throw new IllegalStateException("Context has not been created yet");
            throw new IllegalStateException("Context has not been created yet; need to create a Persistence entity manager; " + LocalEntityManagerFactory.class.getName()); // DEBUG
        } else {
            return factory.createEntityManager();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        factory = Persistence.createEntityManagerFactory("PostgresPersistenceUnit");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        factory.close();
    }
}
