package com.liyz.cloud.api.staff.properties;

import io.swagger.v3.oas.models.info.Info;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/12/21 15:35
 */
@Getter
@Setter
@Component
@ConfigurationProperties("swagger.open")
public class SwaggerOpenApiProperties {

    private Info info;
}
