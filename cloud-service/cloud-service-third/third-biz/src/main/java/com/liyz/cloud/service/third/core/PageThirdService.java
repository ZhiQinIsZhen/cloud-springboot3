package com.liyz.cloud.service.third.core;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.service.third.dto.ThirdBasePageDTO;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:15
 */
public interface PageThirdService<Q extends ThirdBasePageDTO, T> extends ThirdService<Q> {

    /**
     * 分页查询
     *
     * @param req 请求
     * @return remotePage
     */
    RemotePage<T> queryPage(Q req);
}
