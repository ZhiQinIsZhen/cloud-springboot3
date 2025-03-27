package com.liyz.cloud.api.staff.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/4/30 10:17
 */
@Configuration
public class ApiSwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring-cloud系统API")
                        .version("1.0")
                        .description( "这是一个基于Springboot3等框架的脚手架")
                        .termsOfService("http://127.0.0.1:7072/")
                        .license(new License().name("MIT License").url("http://127.0.0.1:7072/"))
                        .contact(new Contact()
                                .name("ZhiQinIsZhen")
                                .email("liyangzhen0114@foxmail.com")
                                .url("https://github.com/ZhiQinIsZhen/springcloud-liyz")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(new Components().addSecuritySchemes(
                                HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer ")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("鉴权Token")
                        )
                )
                ;
    }

}
