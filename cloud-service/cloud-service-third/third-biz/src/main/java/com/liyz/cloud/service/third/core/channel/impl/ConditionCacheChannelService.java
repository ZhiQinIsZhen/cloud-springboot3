package com.liyz.cloud.service.third.core.channel.impl;

import com.liyz.cloud.service.third.constant.QueryStrategy;
import com.liyz.cloud.service.third.core.CacheService;
import com.liyz.cloud.service.third.core.channel.abs.AbstractChannelService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 17:15
 */
@Service
public class ConditionCacheChannelService<Q extends ThirdBaseDTO, T> extends AbstractChannelService<Q, T> {

    @Override
    public QueryStrategy getQueryStrategy() {
        return QueryStrategy.CONDITION_CACHE;
    }

    @Override
    public ParseResult<T> queryChannel(Q req, CacheService<Q, T> cacheService) {
        ParseResult<T> parseResult = cacheService.queryCache(req);
        if (Objects.isNull(parseResult)) {
            parseResult = new ParseResult<>();
        }
        if (CollectionUtils.isEmpty(parseResult.getDataList()) || parseResult.getDataList().size() <= 3) {
            parseResult.setCallThird(true);
        }
        return parseResult;
    }
}
