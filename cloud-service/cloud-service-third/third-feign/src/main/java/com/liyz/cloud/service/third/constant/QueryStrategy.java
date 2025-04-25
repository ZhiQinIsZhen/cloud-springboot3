package com.liyz.cloud.service.third.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 16:46
 */
@Getter
@AllArgsConstructor
public enum QueryStrategy {

    NON_CACHE("不走缓存"),
    FIRST_CACHE("先走缓存，没有再调用第三方"),
    ONLY_CACHE("只走缓存"),
    CONDITION_CACHE("走缓存，如果缓存达到条件，则不再调用第三方");

    private final String desc;
}
