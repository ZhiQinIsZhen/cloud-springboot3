package com.liyz.cloud.service.third.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:01
 */
@Getter
@Setter
public class ThirdBasePageDTO extends ThirdBaseDTO {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;
}
