package com.liyz.cloud.common.api.constant;

import org.springframework.http.HttpHeaders;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/11/24 11:17
 */
public interface SecurityClientConstant {

    /**
     * 免登录资源
     */
    String[] KNIFE4J_IGNORE_RESOURCES = new String[] {
            "/liyz/error",
            "/doc.html",
            "/favicon.ico",
            "/webjars/**",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources"
    };

    /**
     * 监控资源
     */
    String[] ACTUATOR_RESOURCES = new String[] {"/actuator/**"};

    String OPTIONS_PATTERNS = "/**";

    String DEFAULT_TOKEN_HEADER_KEY = HttpHeaders.AUTHORIZATION;

    String DEFAULT_SECURITY_SESSION_KEY = "JSESSIONID";

    String AUTH_MANAGER_BEAN_NAME = "authenticationManager";

    String CLIENT_ID = "spring.application.name";

    Integer DEFAULT_SESSION_TIMEOUT = 30 * 60;
}
