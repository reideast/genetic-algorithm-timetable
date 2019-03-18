package net.andreweast.provider;

// See: https://www.nabisoft.com/tutorials/java-ee/producing-and-consuming-json-or-xml-in-java-rest-services-with-jersey-and-jackson

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonJacksonConfigContextResolver implements ContextResolver<ObjectMapper> {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
    }

    public JsonJacksonConfigContextResolver() {
        System.out.println("Starting JsonJacksonConfigContextResolver");
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        System.out.println("Returning a JSON Jackson ContextResolver");
        return mapper;
    }
}
