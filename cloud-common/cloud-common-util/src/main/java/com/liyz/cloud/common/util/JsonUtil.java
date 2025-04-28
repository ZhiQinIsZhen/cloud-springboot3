package com.liyz.cloud.common.util;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.liyz.cloud.common.util.serializer.DoubleSerializer;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Desc:json工具栏(jackson)
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/29 17:43
 */
@UtilityClass
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder
            .json()
            .createXmlMapper(false)
            .dateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN))
            .timeZone(TimeZone.getTimeZone(DateUtil.TIME_ZONE_GMT8))
            .build()
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)
            .registerModule(new SimpleModule()
                    .addSerializer(Long.class, ToStringSerializer.instance)
                    .addSerializer(Long.TYPE, ToStringSerializer.instance)
                    .addSerializer(Double.class, new DoubleSerializer())
                    .addSerializer(Double.TYPE, new DoubleSerializer())
            );

    @SneakyThrows
    public static String toJSONString(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T readValue(String content, Class<T> clazz) {
        if (StringUtils.isBlank(content) || Objects.isNull(clazz)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(content, clazz);
    }

    @SneakyThrows
    public static <T> T readValue(InputStream inputStream, Class<T> clazz) {
        if (ObjectUtils.anyNull(inputStream, clazz)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(inputStream, clazz);
    }

    @SneakyThrows
    public static <T> T readValue(JsonNode jsonNode, Class<T> clazz) {
        if (ObjectUtils.anyNull(jsonNode, clazz)) {
            return null;
        }
        return OBJECT_MAPPER.treeToValue(jsonNode, clazz);
    }

    @SneakyThrows
    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        if (StringUtils.isBlank(content) || Objects.isNull(valueTypeRef)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(content, valueTypeRef);
    }

    @SneakyThrows
    public static void writeValue(OutputStream out, Object value) {
        OBJECT_MAPPER.writeValue(out, value);
    }

    @SneakyThrows
    public <T> T readValue(String content, JavaType valueType) {
        if (StringUtils.isBlank(content) || Objects.isNull(valueType)) {
            return null;
        }
        return OBJECT_MAPPER.readValue(content, valueType);
    }

    @SneakyThrows
    public static JsonNode readTree(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return OBJECT_MAPPER.readTree((String) obj);
        }
        return OBJECT_MAPPER.readTree(OBJECT_MAPPER.writeValueAsString(obj));
    }

    public static JavaType getJavaType(Class<?> parametrized, Class<?>... parameterClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }
}
