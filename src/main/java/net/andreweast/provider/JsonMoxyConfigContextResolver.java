//package net.andreweast.provider;
//
//// See: https://www.nabisoft.com/tutorials/java-ee/producing-and-consuming-json-or-xml-in-java-rest-services-with-jersey-and-moxy
//
//import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.ext.ContextResolver;
//import javax.ws.rs.ext.Provider;
//import java.util.HashMap;
//import java.util.Map;
//
//@Provider
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class JsonMoxyConfigContextResolver implements ContextResolver<MoxyJsonConfig> {
//    private final MoxyJsonConfig moxyJsonConfig;
//
//    public JsonMoxyConfigContextResolver() {
//        final Map<String, String> namespacePrefixMapper = new HashMap<>();
//        namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
//
//        moxyJsonConfig = new MoxyJsonConfig()
//                .setNamespacePrefixMapper(namespacePrefixMapper)
//                .setNamespaceSeparator(':')
//                .setFormattedOutput(false)
//                .setIncludeRoot(false)
//                .setMarshalEmptyCollections(false);
//    }
//
//    @Override
//    public MoxyJsonConfig getContext(Class<?> type) {
//        return moxyJsonConfig;
//    }
//}
