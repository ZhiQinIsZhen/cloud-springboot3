package com.liyz.cloud.service.third.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.liyz.cloud.common.feign.validation.EnumValue;
import com.liyz.cloud.service.third.constant.QueryStrategy;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.dto.qcc.page.QccCompanySearchPageDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 9:58
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "thirdType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QccCompanySearchPageDTO.class, name = "QCC_PAGE_COMPANY_SEARCH")
})
public class ThirdBaseDTO {

    @NotNull(message = "第三方类型不能为空")
    @EnumValue(target = {ThirdType.class}, message = "第三方类型错误")
    private ThirdType thirdType;

    /**
     * 查询策略
     * @see QueryStrategy
     */
    @NotNull(message = "查询策略不能为空")
    private QueryStrategy queryStrategy = QueryStrategy.FIRST_CACHE;
}
