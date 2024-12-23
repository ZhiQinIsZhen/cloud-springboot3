package com.liyz.cloud.gateway.constant;

import com.liyz.cloud.common.util.PatternUtil;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * Desc:常量池
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/1/27 14:47
 */
public interface GatewayConstant {

    /**
     * 默认通用免认证key
     */
    String DEFAULT_ANONYMOUS_MAPPING = "common-api";

    /**
     * 默认认证header key
     */
    String AUTHORIZATION = "Authorization";

    /**
     * 默认gateway认证数据header key
     */
    String AUTH_INFO = "AUTH_INFO";

    /**
     * 是否匹配到目标路劲
     *
     * @param path 路劲
     * @param mappingSet 集合
     * @return 是否匹配
     */
    default boolean pathMatch(String path, Set<String> mappingSet) {
        if (CollectionUtils.isEmpty(mappingSet)) {
            return false;
        }
        return mappingSet.contains(path) || PatternUtil.pathMatch(path, mappingSet);
    }
}
