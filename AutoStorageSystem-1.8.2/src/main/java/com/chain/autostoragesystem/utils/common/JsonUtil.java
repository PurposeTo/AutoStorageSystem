package com.chain.autostoragesystem.utils.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;

import java.util.Objects;

public class JsonUtil {

    @SneakyThrows
    public static String toJson(Object obj) {
        Objects.requireNonNull(obj);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    @SneakyThrows
    public static String toPrettyJson(String json) {
        StringUtils.requiredNonBlank(json);
        assertJsonValidity(json);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = parseJsonNode(json);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }

    @SneakyThrows
    public static JsonNode parseJsonNode(String json) {
        StringUtils.requiredNonBlank(json);
        assertJsonValidity(json);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

    @SneakyThrows
    public static boolean isValid(String json) {
        Gson gson = new Gson();

        try {
            gson.fromJson(json, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static void assertJsonValidity(String json) {
        StringUtils.requiredNonBlank(json);

        if (!isValid(json)) {
            throw new IllegalArgumentException("[" + json + "] isn't json");
        }
    }

    public static void assertJsonValidity(String json, String logName) {
        StringUtils.requiredNonBlank(json);
        StringUtils.requiredNonBlank(logName);

        if (!isValid(json)) {
            throw new IllegalArgumentException("\"" + logName + "\" isn't json");
        }
    }
}
