package com.liyz.cloud.service.third.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 19:54
 */
@RefreshScope
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "third.qcc")
public class QccProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 域名
     */
    private String domain;

    /**
     * key
     */
    private String key;

    /**
     * secret
     */
    private String secret;

    /**
     * 过期时间
     */
    private int expirePeriod = 365;
}
