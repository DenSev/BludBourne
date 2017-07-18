package com.packtpub.libgdx.bludbourne.utility;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 30.10.2016.
 */
public enum MapperProvider {

    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(MapperProvider.class);

    private final ObjectMapper objectMapper;
    private final Json mapper;
    private boolean useObjectMapper = false;

    MapperProvider() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        //mapper.registerModule(new JodaModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        this.mapper = new Json();
        this.mapper.setOutputType(OutputType.json);
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }

    public Json mapper() {
        return this.mapper;
    }


    public <T> T parse(Class<T> clazz, InputStream inputStream) throws IOException {
        return this.mapper.fromJson(clazz, inputStream);
    }

    public <T> T parse(Class<T> clazz, String path) {
        try {
            return parse(clazz, this.getClass().getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            LOG.error("Error reading file: {}", path);
            throw new RuntimeException(e);
        }
    }


    public <T> T parse(Class<T> clazz, FileHandle fileHandle) {
        try {
            return parse(clazz, fileHandle.read());
        } catch (IOException e) {
            LOG.error("Error reading file: {}", fileHandle.path());
            throw new RuntimeException(e);
        }
    }


    public <T> T parse(Class<T> clazz, JsonValue json) {
        return this.mapper.readValue(clazz, json);
    }


    public String writeValueAsString(Object value) {
        return this.mapper.prettyPrint(value);
    }
}
