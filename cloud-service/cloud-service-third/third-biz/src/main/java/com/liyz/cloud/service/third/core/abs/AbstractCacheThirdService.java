package com.liyz.cloud.service.third.core.abs;

import com.google.common.collect.Lists;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.util.AssertUtil;
import com.liyz.cloud.service.third.constant.ThirdExceptionCodeEnum;
import com.liyz.cloud.service.third.core.CacheEsService;
import com.liyz.cloud.service.third.core.channel.ChannelService;
import com.liyz.cloud.service.third.core.channel.abs.AbstractChannelService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import feign.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 15:59
 */
public abstract class AbstractCacheThirdService<Q extends ThirdBaseDTO, T> extends AbstractThirdService<Q>
        implements CacheEsService<Q, T> {

    private final Class<Q> reqClass;
    private final Class<T> resClass;

    public AbstractCacheThirdService() {
        Type genType = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genType;
        this.reqClass = (Class<Q>) parameterizedType.getActualTypeArguments()[0];
        this.resClass = (Class<T>) parameterizedType.getActualTypeArguments()[1];
    }

    /**
     * 查询数据
     *
     * @param req 请求参数
     * @return 解析后的数据
     */
    private ParseResult<T> queryData(Q req) {
        //校验参数
        this.validateParam(req);
        //查询策略
        ChannelService channelService = AbstractChannelService.getChannelService(req.getQueryStrategy());
        AssertUtil.notNull(channelService, ThirdExceptionCodeEnum.QUERY_STRATEGY_NOT_EXIST);
        ParseResult<T> parseResult = channelService.queryChannel(req, this);
        if (!parseResult.isCallThird()) {
            return parseResult;
        }
        //调用第三方
        String response = this.query(req);
        if (StringUtils.isBlank(response)) {
            return new ParseResult<>();
        }
        //todo 记入日志
        //解析第三方返回数据
        parseResult = parseThirdResponse(response);
        //todo 存入缓存
        return parseResult;
    }

    /**
     * 校验参数
     *
     * @param req 请求参数
     */
    protected void validateParam(Q req) {
        AssertUtil.isTure(this.getThirdType() == req.getThirdType(), ThirdExceptionCodeEnum.THIRD_TYPE_NOT_SAME);
    }

    /**
     * 解析第三方返回数据
     *
     * @param response 响应
     * @return ParseResult
     */
    protected abstract ParseResult<T> parseThirdResponse(String response);


    /**
     * 查询单个数据
     *
     * @param req 请求参数
     * @return T
     */
    protected T one(Q req) {
        AssertUtil.isTure(this.isOneService(), ThirdExceptionCodeEnum.ONE_NOT_SUPPORT);
        ParseResult<T> parseResult = this.queryData(req);
        if (Objects.nonNull(parseResult) && !CollectionUtils.isEmpty(parseResult.getDataList())) {
            return parseResult.getDataList().getFirst();
        }
        return null;
    }

    /**
     * 查询列表数据
     *
     * @param req 请求参数
     * @return List
     */
    protected List<T> list(Q req) {
        AssertUtil.isTure(this.isListService(), ThirdExceptionCodeEnum.LIST_NOT_SUPPORT);
        ParseResult<T> parseResult = this.queryData(req);
        if (Objects.nonNull(parseResult) && !CollectionUtils.isEmpty(parseResult.getDataList())) {
            return parseResult.getDataList();
        }
        return Lists.newArrayList();
    }

    /**
     * 查询分页数据
     *
     * @param req 请求参数
     * @return RemotePage
     */
    protected RemotePage<T> page(Q req) {
        AssertUtil.isTure(this.isPageService(), ThirdExceptionCodeEnum.PAGE_NOT_SUPPORT);
        ParseResult<T> parseResult = this.queryData(req);
        if (Objects.isNull(parseResult) || Objects.isNull(parseResult.getPage())) {
            return null;
        }
        List<T> dataList = parseResult.getDataList();
        if (CollectionUtils.isEmpty(dataList)) {
            dataList = Lists.newArrayList();
        }
        return RemotePage.of(dataList, parseResult.getPage().getTotal(), parseResult.getPage().getPageNum(),
                parseResult.getPage().getPageSize());
    }

    @Override
    protected Response getResponse(Q req) {
        return null;
    }

    @Override
    public ParseResult<T> queryCache(Q req) {
        //todo 缓存查询
        return null;
    }
}
