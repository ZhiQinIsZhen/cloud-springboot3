package com.liyz.cloud.service.third.vo.uniview.one;

import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 17:43
 */
@Getter
@Setter
public class UniViewLoginVO {

    /**
     * 登录token
     */
    private String accessToken;

    /**
     * 过期时间
     */
    private Long expireTime;
}
