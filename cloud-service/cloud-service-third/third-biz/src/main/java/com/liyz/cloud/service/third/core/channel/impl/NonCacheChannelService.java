package com.liyz.cloud.service.third.core.channel.impl;

import com.liyz.cloud.service.third.constant.QueryStrategy;
import com.liyz.cloud.service.third.core.CacheService;
import com.liyz.cloud.service.third.core.channel.abs.AbstractChannelService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import org.springframework.stereotype.Service;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 17:15
 */
@Service
public class NonCacheChannelService<Q extends ThirdBaseDTO, T> extends AbstractChannelService<Q, T> {

    @Override
    public QueryStrategy getQueryStrategy() {
        return QueryStrategy.NON_CACHE;
    }

    @Override
    public ParseResult<T> queryChannel(Q req, CacheService<Q, T> cacheService) {
        ParseResult<T> parseResult = new ParseResult<>();
        parseResult.setCallThird(true);
        return parseResult;
    }
}
