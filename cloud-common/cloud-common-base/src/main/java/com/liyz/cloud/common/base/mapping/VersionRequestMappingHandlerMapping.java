//package com.liyz.cloud.common.base.mapping;
//
//import com.liyz.cloud.common.base.annotation.ApiVersion;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.web.servlet.mvc.condition.RequestCondition;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import java.lang.reflect.Method;
//import java.util.Objects;
//
///**
// * Desc:接口版本控制注解
// *
// * @author lyz
// * @version 1.0.0
// * @date 2025/2/27 16:53
// */
//public class VersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
//
//    @Override
//    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
//        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
//        if (Objects.nonNull(apiVersion)) {
//            RequestCondition<?> customCondition = new ApiVersionCondition(apiVersion.value());
//            mapping = new RequestMappingInfo(mapping.getName(), mapping.getPatternsCondition(),
//                    mapping.getMethodsCondition(), mapping.getParamsCondition(), mapping.getHeadersCondition(),
//                    mapping.getConsumesCondition(), mapping.getProducesCondition(), mapping.getCustomCondition()
//                    .combine(customCondition));
//        }
//        super.registerHandlerMethod(handler, method, mapping);
//    }
//}
