package com.liyz.cloud.common.base.mapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc:API版本条件比较器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/2/27 16:53
 */
@Getter
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private static final Pattern VERSION_PATTERN = Pattern.compile("/v(\\d+)/");

    private final String apiVersion;

    public ApiVersionCondition(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String appVersion = request.getHeader("appVersion");
        if (StringUtils.isBlank(appVersion)) {
            return null;
        }

        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return 0;
    }
}
