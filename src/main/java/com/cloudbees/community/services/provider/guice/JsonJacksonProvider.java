package com.cloudbees.community.services.provider.guice;

import com.google.inject.Provider;
import com.sun.jersey.spi.resource.Singleton;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Vivek Pandey
 */
@Singleton
public class JsonJacksonProvider implements Provider<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return om;
    }

    private static final ObjectMapper om = createObjectMapper();

    private static ObjectMapper createObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig serializationConfig = mapper.getSerializationConfig();

        //don't write null values
        serializationConfig.withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        deserializationConfig.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

}
