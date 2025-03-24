package com.liyz.cloud.common.base.annotation;

import java.lang.annotation.*;

/**
 * Desc:接口版本控制注解
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/2/27 16:53
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {

    /**
     * 版本号
     *
     * @return 版本号
     */
    String value();
}
