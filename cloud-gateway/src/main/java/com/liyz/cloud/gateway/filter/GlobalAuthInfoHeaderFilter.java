package com.liyz.cloud.gateway.filter;

import com.liyz.cloud.common.feign.bo.auth.AuthUserBO;
import com.liyz.cloud.common.util.CryptoUtil;
import com.liyz.cloud.common.util.JsonUtil;
import com.liyz.cloud.gateway.constant.GatewayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AddRequestHeaderGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.GatewayToStringStyler.filterToStringCreator;

/**
 * Desc:往后传递header过滤器
 *
 * 注：这里其实需要密文传输，这里只做一个实例，实际开发中需要约定好加密方式，gateway进行加密->cloud-api-staff进行解密，
 * 同时可以配合jwt进行身份对比，这时就不需要再次查询service-staff了
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/1/30 9:57
 */
@Slf4j
@Component
public class GlobalAuthInfoHeaderFilter extends AddRequestHeaderGatewayFilterFactory implements Ordered {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                AuthUserBO authUserBO = exchange.getAttribute(GatewayConstant.AUTH_INFO);
                String authInfo = authUserBO == null ? null : CryptoUtil.Symmetric.encryptAES(JsonUtil.toJSONString(authUserBO), config.getValue());
                ServerHttpRequest request = exchange
                        .getRequest()
                        .mutate()
                        .headers(httpHeaders -> httpHeaders.add(config.getName(), authInfo))
                        .build();
                return chain.filter(exchange.mutate().request(request).build());
            }

            @Override
            public String toString() {
                return filterToStringCreator(GlobalAuthInfoHeaderFilter.this).append(config.getName(), config.getValue()).toString();
            }
        };
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
