package io.github.zoowayss.starter.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.*;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper om = new ObjectMapper();

    static {
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static Map<String, Object> toMap(String json) {
        return Try.of(() -> om.readValue(json, new TypeReference<Map<String, Object>>() {})).get();
    }

    public static <T> T toBean(String json, Class<T> clazz) {
        return Try.of(() -> om.readValue(json, clazz)).get();
    }


    public static <T> T toBean(String json, TypeReference<T> typeReference) {
        return Try.of(() -> om.<T>readValue(json, typeReference)).get();
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        return Try
                .of(() -> om.<List<T>>readValue(json, om.getTypeFactory().constructCollectionLikeType(List.class, clazz))).get();
    }

    public static String toJson(Object obj) {
        return Try.of(() -> om.writeValueAsString(obj)).get();
    }

    public static String checkNullAndToJson(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return Try.of(() -> om.writeValueAsString(obj)).get();
        }
    }

    public static class Try {

        static <T> Supplier<T> of(SupplierWithException<T> supplier) {
            return () -> {
                try {
                    return supplier.get();
                } catch (Exception e) {
                    log.error("error", e);
                    throw new RuntimeException(e);
                }
            };
        }

        @FunctionalInterface
        public interface SupplierWithException<T> {

            T get() throws Exception;
        }
    }
}