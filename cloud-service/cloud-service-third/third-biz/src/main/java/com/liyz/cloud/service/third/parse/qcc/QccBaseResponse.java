package com.liyz.cloud.service.third.parse.qcc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 9:38
 */
@Getter
@Setter
public class QccBaseResponse<T> {

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("OrderNumber")
    private String orderNumber;

    @JsonProperty("Result")
    private T result;

    @JsonProperty("Paging")
    private QccPage paging;
}
