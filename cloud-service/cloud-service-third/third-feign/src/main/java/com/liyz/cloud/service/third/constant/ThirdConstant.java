package com.liyz.cloud.service.third.constant;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 20:09
 */
public interface ThirdConstant {

    /**
     * 应用名称
     */
    String APPLICATION_NAME = "cloud-service-third";

    /**
     * 根路劲
     */
    String CONTEXT_PATH = "/third";

    /**
     * 三方数据url
     */
    String THIRD_DATA_URL = "/data";

    /**
     * 企查查成功状态码
     */
    String QCC_SUCCESS_STATUS = "200";

    /**
     * 宇视成功状态码
     */
    String UNIVIEW_SUCCESS_STATUS = "200";

    /**
     * 宇视token错误状态码
     */
    String UNIVIEW_TOKEN_FAIL = "1001";

    /**
     * 宇视token过期状态码
     */
    String UNIVIEW_TOKEN_EXPIRED = "1002";
}
