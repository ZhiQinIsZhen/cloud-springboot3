package com.liyz.cloud.service.third.constant;

import com.liyz.cloud.common.exception.IExceptionService;
import lombok.AllArgsConstructor;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 14:02
 */
@AllArgsConstructor
public enum ThirdExceptionCodeEnum implements IExceptionService {
    THIRD_CALL_FAIL("30001", "三方接口调用失败"),
    ONE_NOT_SUPPORT("30002", "当前接口不支持查询单个数据"),
    LIST_NOT_SUPPORT("30003", "当前接口不支持查询列表数据"),
    PAGE_NOT_SUPPORT("30004", "当前接口不支持查询分页数据"),
    THIRD_TYPE_NOT_SAME("30005", "三方类型不一致"),
    QUERY_STRATEGY_NOT_EXIST("30006", "查询策略不存在"),
    THIRD_CLOSED("30007", "该三方接口已关闭"),
    THIRD_TYPE_NOT_EXIST("30008", "该三方接口类型的服务不存在"),
    ;

    private final String code;

    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
