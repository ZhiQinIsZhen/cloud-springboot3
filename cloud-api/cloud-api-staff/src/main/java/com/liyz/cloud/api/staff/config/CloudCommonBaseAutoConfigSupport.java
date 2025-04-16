package com.liyz.cloud.api.staff.config;

import com.liyz.cloud.common.base.mapping.VersionRequestMappingHandlerMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Desc:CloudCommonBaseAutoConfigSupport
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/2/27 16:53
 */
//@Configuration
public class CloudCommonBaseAutoConfigSupport extends WebMvcConfigurationSupport {

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new VersionRequestMappingHandlerMapping();
    }
}
