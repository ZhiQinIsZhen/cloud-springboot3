package com.liyz.cloud.common.search.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-07-13 16:00
 */
@Getter
@Setter
public class BaseProperties {

    /**
     * 索引
     */
    private List<String> indices;

    /**
     * 超时时间
     */
    private long timeout = 3000;

    /**
     * 统计数量
     */
    private Boolean trackTotalHits;
}
