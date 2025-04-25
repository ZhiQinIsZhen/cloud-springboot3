package com.liyz.cloud.service.third.core;

import com.liyz.cloud.service.third.dto.ThirdBaseDTO;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:11
 */
public interface OneThirdService<Q extends ThirdBaseDTO, T> extends ThirdService<Q> {

    /**
     * 查询单个
     *
     * @param req 请求
     * @return T
     */
    T queryOne(Q req);
}
