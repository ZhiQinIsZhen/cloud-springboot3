package com.liyz.cloud.service.third.core.abs.qcc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.util.AssertUtil;
import com.liyz.cloud.common.util.JsonUtil;
import com.liyz.cloud.common.util.constant.CommonConstant;
import com.liyz.cloud.service.third.constant.ThirdConstant;
import com.liyz.cloud.service.third.constant.ThirdExceptionCodeEnum;
import com.liyz.cloud.service.third.core.abs.AbstractCacheThirdService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import com.liyz.cloud.service.third.parse.qcc.QccBaseResponse;
import com.liyz.cloud.service.third.properties.QccProperties;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 19:41
 */
public abstract class AbstractQccCacheThirdService<Q extends ThirdBaseDTO, T> extends AbstractCacheThirdService<Q, T> {

    @Resource
    private QccProperties qccProperties;

    @Override
    protected void validateParam(Q req) {
        super.validateParam(req);
        AssertUtil.isTure(qccProperties.isEnable(), ThirdExceptionCodeEnum.THIRD_CLOSED);
    }

    @Override
    protected String getDomain() {
        return qccProperties.getDomain();
    }

    @Override
    protected void addHttpHeaders(Q req, HttpHeaders headers) {
        String timestamp = String.valueOf((System.currentTimeMillis() / 1000));
        String token = DigestUtils.md5DigestAsHex(qccProperties.getKey().concat(timestamp)
                .concat(qccProperties.getSecret()).getBytes()).toUpperCase();
        headers.add("Token", token);
        headers.add("Timespan", timestamp);
    }

    @Override
    protected MultiValueMap<String, String> addQueryParams(Q req) {
        MultiValueMap<String, String> queryParams = super.addQueryParams(req);
        queryParams.add("key", qccProperties.getKey());
        return queryParams;
    }

    @Override
    protected ParseResult<T> parseThirdResponse(String response) {
        ParseResult<T> parseResult = new ParseResult<>();
        parseResult.setDataList(Lists.newArrayList());
        if (isOneService()) {
            QccBaseResponse<T> qccBaseResponse = JsonUtil.readValue(response, new TypeReference<>() {});
            this.checkResponseStatus(qccBaseResponse);
            if (Objects.nonNull(qccBaseResponse.getResult())) {
                parseResult.getDataList().add(qccBaseResponse.getResult());
            }
        } else {
            QccBaseResponse<List<T>> qccBaseResponse = JsonUtil.readValue(response, new TypeReference<>() {});
            this.checkResponseStatus(qccBaseResponse);
            if (!CollectionUtils.isEmpty(qccBaseResponse.getResult())) {
                parseResult.getDataList().addAll(qccBaseResponse.getResult());
            }
            if (Objects.nonNull(qccBaseResponse.getPaging())) {
                parseResult.setPage(new RemotePage<>(qccBaseResponse.getResult(),
                        qccBaseResponse.getPaging().getTotalRecords(),
                        qccBaseResponse.getPaging().getPageIndex(),
                        qccBaseResponse.getPaging().getPageSize()));
            }
        }
        return parseResult;
    }

    /**
     * 检查响应状态
     *
     * @param qccBaseResponse 响应对象
     */
    private void checkResponseStatus(QccBaseResponse<?> qccBaseResponse) {
        AssertUtil.isTure(Objects.nonNull(qccBaseResponse), ThirdExceptionCodeEnum.THIRD_CALL_FAIL);
        if (!ThirdConstant.QCC_SUCCESS_STATUS.equals(qccBaseResponse.getStatus())) {
            throw new RemoteServiceException(ThirdExceptionCodeEnum.THIRD_CALL_FAIL.getCode() + CommonConstant.DEFAULT_JOINER + qccBaseResponse.getStatus(),
                    qccBaseResponse.getMessage());
        }
    }
}
