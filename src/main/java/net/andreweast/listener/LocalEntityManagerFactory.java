package net.andreweast.listener;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Wiki article on how this is done
 * https://wiki.eclipse.org/EclipseLink/Examples/JPA/Tomcat_Web_Tutorial
 *
 * Also, https://blogs.sap.com/2012/12/11/put-jpa-in-your-web-app-tomcat-eclipselink/
 * Also, https://stackoverflow.com/a/7862798/5271224
 */

@WebListener
public class LocalEntityManagerFactory implements ServletContextListener {
    private static EntityManagerFactory factory;

    public static EntityManager createEntityManager() {
        if (factory == null) {
            throw new IllegalStateException("Context has not been created yet");
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
