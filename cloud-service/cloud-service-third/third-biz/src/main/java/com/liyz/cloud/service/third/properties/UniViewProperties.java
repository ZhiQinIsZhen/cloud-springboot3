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
 * @date 2025-04-25 17:28
 */
@RefreshScope
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "third.uniview")
public class UniViewProperties {

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 域名
     */
    private String domain;

    /**
     * appId
     */
    private String appId;

    /**
     * appSecret
     */
    private String secretKey;

    /**
     * 签名key
     */
    private String signKey;

    /**
     * 过期时间
     */
    private int expirePeriod = 365;
}
