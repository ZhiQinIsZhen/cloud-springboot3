package com.liyz.cloud.common.base.mapping;

import com.liyz.cloud.common.base.util.ApiVersionUtil;
import com.liyz.cloud.common.util.constant.CommonConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * Desc:API版本条件比较器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/2/27 16:53
 */
@Getter
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

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
        String appVersion = request.getHeader(CommonConstant.APP_VERSION_KEY);
        return StringUtils.isNotBlank(appVersion) && ApiVersionUtil.compareVersion(appVersion, this.apiVersion) >= 0 ? this : null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        int compare = ApiVersionUtil.compareVersion(other.getApiVersion(), this.apiVersion);
        String appVersion = request.getHeader(CommonConstant.APP_VERSION_KEY);
        if (StringUtils.isNotBlank(appVersion)) {
            int thisCompare = ApiVersionUtil.compareVersion(appVersion, this.apiVersion);
            int otherCompare = ApiVersionUtil.compareVersion(appVersion, other.apiVersion);
            return thisCompare != otherCompare ? Integer.compare(thisCompare, otherCompare) : compare;
        }
        return compare;
    }
}
