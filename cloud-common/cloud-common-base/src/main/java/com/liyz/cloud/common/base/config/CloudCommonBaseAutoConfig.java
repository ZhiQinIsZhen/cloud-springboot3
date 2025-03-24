package com.liyz.cloud.common.base.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.liyz.cloud.common.base.advice.GlobalControllerExceptionAdvice;
import com.liyz.cloud.common.base.advice.ResultFeignDecoderAdvice;
import com.liyz.cloud.common.base.advice.ResultResponseBodyAdvice;
import com.liyz.cloud.common.base.error.ErrorApiController;
import com.liyz.cloud.common.util.DateUtil;
import com.liyz.cloud.common.util.deserializer.DesensitizationSerializer;
import com.liyz.cloud.common.util.deserializer.TrimDeserializer;
import com.liyz.cloud.common.util.serializer.DoubleSerializer;
import feign.Feign;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/5/9 15:16
 */
@Configuration
@EnableConfigurationProperties({ServerProperties.class})
public class CloudCommonBaseAutoConfig implements WebMvcConfigurer {

    @Bean
    public GlobalControllerExceptionAdvice globalControllerExceptionAdvice() {
        return new GlobalControllerExceptionAdvice();
    }

    @Bean
    public ResultResponseBodyAdvice resultResponseBodyAdvice() {
        return new ResultResponseBodyAdvice();
    }

    @Bean
    public ErrorApiController errorApiController(ServerProperties serverProperties) {
        return new ErrorApiController(serverProperties);
    }

    @Bean
    public ResultFeignDecoderAdvice resultFeignDecoderAdvice(ObjectMapper objectMapper) {
        return new ResultFeignDecoderAdvice(objectMapper);
    }

    @Bean
    public Feign.Builder decodeVoidFeignBuilder() {
        return Feign.builder().decodeVoid();
    }

    /**
     * 扩展json
     *
     * @param converters http消息协商列表
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (CollectionUtils.isEmpty(converters)) {
            return;
        }
        //处理json转换器
        Optional<HttpMessageConverter<?>> optional = converters
                .stream()
                .filter(item -> item instanceof MappingJackson2HttpMessageConverter)
                .findFirst();
        optional.ifPresent(item -> {
            MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) item;
            ObjectMapper objectMapper = converter.getObjectMapper();
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            simpleModule.addSerializer(Double.class, new DoubleSerializer());
            simpleModule.addSerializer(Double.TYPE, new DoubleSerializer());
            simpleModule.addSerializer(String.class, new DesensitizationSerializer());
            simpleModule.addDeserializer(String.class, new TrimDeserializer());
            objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));
            objectMapper.setTimeZone(TimeZone.getTimeZone(DateUtil.TIME_ZONE_GMT8));
            objectMapper.registerModule(simpleModule);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
            objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        });
    }
}
