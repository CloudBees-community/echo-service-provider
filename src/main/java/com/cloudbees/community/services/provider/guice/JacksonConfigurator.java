package com.cloudbees.community.services.provider.guice;

import com.google.inject.Singleton;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * @author Vivek Pandey
 */
@Provider
@Singleton
@Produces("application/json")
public class JacksonConfigurator implements ContextResolver<ObjectMapper>, com.google.inject.Provider<ObjectMapper> {

    private ObjectMapper mapper = new ObjectMapper();

    public JacksonConfigurator() {
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ObjectMapper getContext(Class<?> arg0) {
        return mapper;
    }

    @Override
    public ObjectMapper get() {
        return mapper;
    }
}
