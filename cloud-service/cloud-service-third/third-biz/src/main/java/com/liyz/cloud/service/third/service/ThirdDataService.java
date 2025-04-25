package com.liyz.cloud.service.third.service;

import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;

import java.util.List;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-25 10:28
 */
public interface ThirdDataService<Q extends ThirdBaseDTO, T> {

    /**
     * 查询单条
     *
     * @param req 请求参数
     * @return T
     */
    T getOne(Q req);

    /**
     * 查询列表
     *
     * @param req 请求参数
     * @return List
     */
    List<T> getList(Q req);

    /**
     * 查询分页
     *
     * @param req 请求参数
     * @return RemotePage
     */
    RemotePage<T> getPage(Q req);
}
