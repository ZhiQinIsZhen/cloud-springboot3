package com.liyz.cloud.service.third.core;

import com.liyz.cloud.service.third.dto.ThirdBaseDTO;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:13
 */
public interface ListThirdService<Q extends ThirdBaseDTO, T> extends ThirdService<Q> {

    /**
     * 查询列表
     *
     * @param req 请求
     * @return list
     */
    List<T> queryList(Q req);
}
