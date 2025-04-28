package com.liyz.cloud.service.third.core.channel;

import com.liyz.cloud.service.third.core.CacheService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.parse.ParseResult;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 16:57
 */
public interface ChannelService<Q extends ThirdBaseDTO, T> {

    /**
     * 查询通道策略
     *
     * @param req 请求参数
     * @param cacheService 缓存服务
     * @return 解析结果
     */
    ParseResult<T> queryChannel(Q req, CacheService<Q, T> cacheService);
}
