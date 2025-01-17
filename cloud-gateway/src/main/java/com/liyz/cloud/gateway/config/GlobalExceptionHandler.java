package com.liyz.cloud.gateway.config;

import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.feign.result.Result;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/12/6 16:33
 */
@Configuration
@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse resp = exchange.getResponse();
        if (resp.isCommitted()) {
            return Mono.error(ex);
        }
        resp.setStatusCode(HttpStatus.OK);
        resp.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DataBuffer dataBuffer = resp.bufferFactory().wrap(ex.getMessage().getBytes());
        return resp.writeWith(Mono.just(dataBuffer));
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public Result<String> noResourceFoundException(NoResourceFoundException exception) {
        return Result.error(CommonExceptionCodeEnum.NOT_FOUND);
    }
}
