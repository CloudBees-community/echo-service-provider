package com.cloudbees.community.services.provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Vivek Pandey
 */
public final class JsonObjectMapper {
    private static final ObjectMapper om = createObjectMapper();

    public static ObjectMapper getObjectMapper(){
        return om;
    }

    private static ObjectMapper createObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig serializationConfig = mapper.getSerializationConfig();

        serializationConfig.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        deserializationConfig.set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return mapper;
    }
}
