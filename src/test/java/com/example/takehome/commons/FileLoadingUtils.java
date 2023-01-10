package com.example.takehome.commons;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

public abstract class FileLoadingUtils {

    protected final String COUNTRIES_CLIENT_RESPONSE = "countriesClientResponse.json";

    protected final ObjectMapper objectMapper = JsonMapper.builder()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addModule(new JavaTimeModule()).build();


    protected JSONArray getJsonArrayMock(
            final String resourceName) throws Exception {

        return new JSONArray(
                Files.readString(
                        ResourceUtils.getFile("classpath:data/" + resourceName).toPath()));
    }
}
