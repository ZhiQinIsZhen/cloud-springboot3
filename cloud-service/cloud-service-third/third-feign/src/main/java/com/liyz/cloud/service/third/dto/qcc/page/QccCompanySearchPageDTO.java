package com.liyz.cloud.service.third.dto.qcc.page;

import com.liyz.cloud.service.third.dto.ThirdBasePageDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:03
 */
@Getter
@Setter
public class QccCompanySearchPageDTO extends ThirdBasePageDTO {

    @NotBlank(message = "搜索关键字不能为空")
    private String searchKey;

    /**
     * 省份Code(6位行政区划代码)
     */
    private String provinceCode;

    /**
     * 城市Code(6位行政区划代码)
     */
    private String cityCode;
}
