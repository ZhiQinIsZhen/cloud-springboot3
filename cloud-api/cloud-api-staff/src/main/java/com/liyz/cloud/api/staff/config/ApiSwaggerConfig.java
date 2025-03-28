package com.liyz.cloud.api.staff.config;

import com.liyz.cloud.api.staff.properties.SwaggerOpenApiProperties;
import com.liyz.cloud.common.util.constant.CommonConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Desc:swagger config
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 10:17
 */
@Configuration
public class ApiSwaggerConfig {

    @Resource
    private SwaggerOpenApiProperties swaggerOpenApiProperties;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(swaggerOpenApiProperties.getInfo())
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(new Components().addSecuritySchemes(HttpHeaders.AUTHORIZATION, securityScheme()));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .scheme(CommonConstant.SECURITY_SCHEME)
                .in(SecurityScheme.In.HEADER)
                .description(CommonConstant.SECURITY_SCHEME_DESC);
    }
}
