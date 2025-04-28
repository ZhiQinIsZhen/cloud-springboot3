package com.liyz.cloud.service.third.core.impl.uniview.one;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.liyz.cloud.common.util.DateUtil;
import com.liyz.cloud.common.util.JsonUtil;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.core.OneThirdService;
import com.liyz.cloud.service.third.core.abs.uniview.AbstractUniViewCacheThirdService;
import com.liyz.cloud.service.third.dto.uniview.one.UniViewLoginDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import com.liyz.cloud.service.third.parse.uniview.UniViewBaseResponse;
import com.liyz.cloud.service.third.vo.uniview.one.UniViewLoginVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Desc:宇视登录
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-28 10:56
 */
@Service
public class UniViewLoginOneServiceImpl extends AbstractUniViewCacheThirdService<UniViewLoginDTO, UniViewLoginVO>
        implements OneThirdService<UniViewLoginDTO, UniViewLoginVO> {

    private final static String UNI_VIEW_LOGIN_TOKEN = "third:uniview:token";

    @Resource
    private RedissonClient redissonClient;

    @Override
    public UniViewLoginVO queryOne(UniViewLoginDTO req) {
        return one(req);
    }

    @Override
    public ThirdType getThirdType() {
        return ThirdType.UNIVIEW_ONE_LOGIN;
    }

    @Override
    protected Object getReqBody(UniViewLoginDTO req) {
        Map<String, String> map = new HashMap<>();
        map.put("appId", uniViewProperties.getAppId());
        map.put("secretKey", uniViewProperties.getSecretKey());
        return map;
    }

    @Override
    public ParseResult<UniViewLoginVO> queryCache(UniViewLoginDTO req) {
        RBucket<UniViewLoginVO> bucket = redissonClient.getBucket(UNI_VIEW_LOGIN_TOKEN, JsonJacksonCodec.INSTANCE);
        UniViewLoginVO uniViewLoginVO = bucket.get();
        ParseResult<UniViewLoginVO> parseResult = new ParseResult<>();
        if (Objects.isNull(uniViewLoginVO)
                || StringUtils.isBlank(uniViewLoginVO.getAccessToken())
                || Objects.isNull(uniViewLoginVO.getExpireTime())
                || uniViewLoginVO.getExpireTime() * 1000 < System.currentTimeMillis()) {
            parseResult.setCallThird(true);
        }
        parseResult.setDataList(Lists.newArrayList(uniViewLoginVO));
        return parseResult;
    }

    @Override
    protected Boolean doSaveCache(ParseResult<UniViewLoginVO> parseResult) {
        RBucket<UniViewLoginVO> bucket = redissonClient.getBucket(UNI_VIEW_LOGIN_TOKEN, JsonJacksonCodec.INSTANCE);
        UniViewLoginVO uniViewLoginVO = parseResult.getDataList().getFirst();
        bucket.set(uniViewLoginVO, Duration.ofSeconds(uniViewLoginVO.getExpireTime() - DateUtil.currentSeconds()));
        return Boolean.TRUE;
    }

    @Override
    protected boolean isLogin() {
        return true;
    }

    @Override
    protected UniViewBaseResponse<UniViewLoginVO> parseJson(String json) {
        return JsonUtil.readValue(json, new TypeReference<>() {});
    }
}
