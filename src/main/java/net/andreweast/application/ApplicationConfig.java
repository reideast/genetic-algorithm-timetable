package net.andreweast.application;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://docs.oracle.com/javaee/6/api/javax/ws/rs/core/Application.html
 * Class Application
 * "Defines the components of a JAX-RS application and supplies additional metadata. A JAX-RS application or implementation supplies a concrete subclass of this abstract class."
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

//        // Registery Moxy JSON providers
//        resources.add(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);

        resources.add(org.glassfish.jersey.jackson.JacksonFeature.class);

//        resources.add(net.andreweast.provider.JsonMoxyConfigContextResolver.class);
        resources.add(net.andreweast.provider.JsonJacksonConfigContextResolver.class);
        resources.add(net.andreweast.api.CourseEndpoint.class);
//        resources.add(net.andreweast.api.DepartmentEndpoint.class);
        resources.add(net.andreweast.api.ScheduleEndpoint.class);

        return resources;
    }

    @Override
    public Map<String, Object> getProperties() {
        // TODO: from https://memorynotfound.com/configure-jersey-with-annotations-only/
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("jersey.config.server.wadl.disableWadl", true); // For security, not to expose a management interface
//        properties.put("jersey.config.server.provider.packages", "net.andreweast.api");
        return properties;
    }
}
