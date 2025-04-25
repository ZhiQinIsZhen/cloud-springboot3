package com.liyz.cloud.service.third.parse.qcc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 9:45
 */
@Getter
@Setter
public class QccPage {

    @JsonProperty("PageIndex")
    private Long pageIndex;

    @JsonProperty("PageSize")
    private Long pageSize;

    @JsonProperty("TotalRecords")
    private Long totalRecords;
}
