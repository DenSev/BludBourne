package com.packtpub.libgdx.bludbourne.utility;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created on 30.10.2016.
 */
public enum MapperProvider {
    INSTANCE;

    private ObjectMapper mapper;

    MapperProvider() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //mapper.registerModule(new JodaModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setVisibility(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public ObjectMapper mapper() {
        return mapper;
    }

}
