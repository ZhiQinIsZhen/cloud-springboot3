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
import com.liyz.cloud.common.base.mapping.VersionRequestMappingHandlerMapping;
import com.liyz.cloud.common.util.DateUtil;
import com.liyz.cloud.common.util.deserializer.DesensitizationSerializer;
import com.liyz.cloud.common.util.deserializer.TrimDeserializer;
import com.liyz.cloud.common.util.serializer.DoubleSerializer;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Desc:CloudCommonBaseAutoConfig
 * 这里注意使用{@link WebMvcConfigurer}与{@link WebMvcConfigurationSupport}区别
 * 主要区别在于{@link org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration}中的
 * condition {ConditionalOnMissingBean({WebMvcConfigurationSupport.class})}，使用support则springboot原生的config则不会
 * 创建，并且在{@link org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration}
 * 的父类{@link DelegatingWebMvcConfiguration}中会找出所有的{@link WebMvcConfigurer}进行逐步配置
 * <p>
 *     静态资源默认四个位置{@link WebProperties}
 *     webjars资源默认两个位置 "/webjars/**", "classpath:/META-INF/resources/webjars/"
 * </p>
 *
 * 注: 所以建议大家使用{@link WebMvcConfigurer}来或者自己的mvc配置
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

    @Bean
    @ConditionalOnProperty(prefix = "api.version", name = "enabled", havingValue = "true")
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new VersionRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
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
