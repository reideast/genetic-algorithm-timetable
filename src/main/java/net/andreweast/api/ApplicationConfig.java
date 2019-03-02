package net.andreweast.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.Map;

/**
 * https://docs.oracle.com/javaee/6/api/javax/ws/rs/core/Application.html
 * Class Application
 * "Defines the components of a JAX-RS application and supplies additional metadata. A JAX-RS application or implementation supplies a concrete subclass of this abstract class."
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    @Override
    public Map<String, Object> getProperties() {
        // TODO: from https://memorynotfound.com/configure-jersey-with-annotations-only/
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("jersey.config.server.provider.packages", "net.andreweast.api");
        return properties;
    }
}
