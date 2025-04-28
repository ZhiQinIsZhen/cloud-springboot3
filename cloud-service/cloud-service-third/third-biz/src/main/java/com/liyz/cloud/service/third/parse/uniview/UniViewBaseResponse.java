package com.liyz.cloud.service.third.parse.uniview;

import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-28 10:38
 */
@Getter
@Setter
public class UniViewBaseResponse<T> {

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据体
     */
    private T data;
}
