package com.liyz.cloud.service.third.core;

import com.liyz.cloud.service.third.dto.ThirdBaseDTO;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 9:54
 */
public interface ThirdService<Q extends ThirdBaseDTO> {

    /**
     * 查询第三方接口数据
     *
     * @param req 请求参数
     * @return response
     */
    String query(Q req);
}
