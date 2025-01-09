package com.liyz.cloud.gateway.filter;

import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.gateway.constant.GatewayConstant;
import com.liyz.cloud.gateway.properties.AnonymousMappingProperties;
import com.liyz.cloud.gateway.util.ResponseUtil;
import com.liyz.cloud.service.auth.feign.JwtParseFeignService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Desc:Jwt全局过滤器
 * <p>
 *     gateway的filter中的顺序是由接口{@link Ordered}来决定的
 * </p>
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/11/27 16:44
 */
@Slf4j
@Component
@RefreshScope
@EnableConfigurationProperties(AnonymousMappingProperties.class)
public class GlobalJWTFilter implements GlobalFilter, GatewayConstant, Ordered {

    private final AnonymousMappingProperties properties;

    public GlobalJWTFilter(AnonymousMappingProperties properties) {
        this.properties = properties;
    }

    @Resource
    @Lazy
    private JwtParseFeignService jwtParseFeignService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route == null) {
            return ResponseUtil.response(response, "404", "NOT_FOUND");
        }
        String path = request.getURI().getPath();
        String clientId = route.getId();
        if (this.pathMatch(path, properties.getServer().get(GatewayConstant.DEFAULT_ANONYMOUS_MAPPING))
                || this.pathMatch(path, properties.getServer().get(clientId))) {
            return chain.filter(exchange);
        }
        String jwt = Objects.requireNonNullElse(request.getCookies().getFirst(GatewayConstant.AUTHORIZATION),
                new HttpCookie(GatewayConstant.AUTHORIZATION, null)).getValue();
        if (StringUtils.isBlank(jwt)) {
            jwt = request.getHeaders().getFirst(GatewayConstant.AUTHORIZATION);
        }
        if (StringUtils.isNotBlank(jwt)) {
            jwt = UriUtils.decode(jwt, StandardCharsets.UTF_8);
        } else {
            return ResponseUtil.response(response, CommonExceptionCodeEnum.AUTHORIZATION_FAIL);
        }
        try {
            AuthUserBO authUserBO = jwtParseFeignService.parseToken(jwt, clientId);
            exchange.getAttributes().put(GatewayConstant.AUTH_INFO, authUserBO);
        } catch (RemoteServiceException e) {
            return ResponseUtil.response(response, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("parse token error", e);
            return ResponseUtil.response(response, CommonExceptionCodeEnum.AUTHORIZATION_FAIL);
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
