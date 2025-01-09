package com.liyz.cloud.common.base.advice;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.result.PageResult;
import com.liyz.cloud.common.feign.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * Desc:全局result编码处理器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/5/9 15:18
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> declaringClass = returnType.getDeclaringClass();
        return declaringClass.getName().startsWith("com.liyz.cloud");
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (MediaType.APPLICATION_JSON != selectedContentType && MediaType.APPLICATION_JSON_UTF8 != selectedContentType) {
            return body;
        }
        if (Objects.isNull(body)) {
            return Result.success(body);
        }
        if (body instanceof Result || body instanceof PageResult) {
            return body;
        }
        if (body instanceof RemotePage<?> remotePage) {
            return PageResult.success(remotePage);
        }
        return Result.success(body);
    }
}
