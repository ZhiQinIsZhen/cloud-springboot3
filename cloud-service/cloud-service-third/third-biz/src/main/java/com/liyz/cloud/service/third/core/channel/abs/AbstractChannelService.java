package com.liyz.cloud.service.third.core.channel.abs;

import com.liyz.cloud.service.third.constant.QueryStrategy;
import com.liyz.cloud.service.third.core.channel.ChannelService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;

import java.util.EnumMap;
import java.util.Map;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 17:12
 */
public abstract class AbstractChannelService<Q extends ThirdBaseDTO, T> implements ChannelService<Q, T> {

    private static final Map<QueryStrategy, ChannelService<? extends ThirdBaseDTO, ?>> CHANNEL_SERVICE_MAP = new EnumMap<>(QueryStrategy.class);

    public static ChannelService<? extends ThirdBaseDTO, ?> getChannelService(QueryStrategy queryStrategy) {
        return CHANNEL_SERVICE_MAP.get(queryStrategy);
    }

    public AbstractChannelService() {
        CHANNEL_SERVICE_MAP.put(this.getQueryStrategy(), this);
    }

    /**
     * 获取查询策略类型
     *
     * @return 查询策略类型
     */
    public abstract QueryStrategy getQueryStrategy();
}
