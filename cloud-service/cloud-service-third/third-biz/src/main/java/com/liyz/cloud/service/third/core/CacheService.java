package com.liyz.cloud.service.third.core;

import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.parse.ParseResult;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 17:07
 */
public interface CacheService<Q extends ThirdBaseDTO, T> {

    /**
     * 查询缓存
     *
     * @param req 请求参数
     * @return 解析后的数据
     */
    ParseResult<T> queryCache(Q req);

    /**
     * 保存缓存
     *
     * @param parseResult 解析后的数据
     * @return bool
     */
    Boolean saveCache(ParseResult<T> parseResult);
}
