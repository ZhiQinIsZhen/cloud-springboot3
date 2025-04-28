package com.liyz.cloud.service.third.core.abs.uniview;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.util.AssertUtil;
import com.liyz.cloud.common.util.JsonUtil;
import com.liyz.cloud.common.util.constant.CommonConstant;
import com.liyz.cloud.service.third.constant.ThirdConstant;
import com.liyz.cloud.service.third.constant.ThirdExceptionCodeEnum;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.core.abs.AbstractCacheThirdService;
import com.liyz.cloud.service.third.core.impl.uniview.one.UniViewLoginOneServiceImpl;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import com.liyz.cloud.service.third.dto.uniview.one.UniViewLoginDTO;
import com.liyz.cloud.service.third.parse.ParseResult;
import com.liyz.cloud.service.third.parse.uniview.UniViewBaseResponse;
import com.liyz.cloud.service.third.properties.UniViewProperties;
import com.liyz.cloud.service.third.vo.uniview.one.UniViewLoginVO;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 19:41
 */
public abstract class AbstractUniViewCacheThirdService<Q extends ThirdBaseDTO, T> extends AbstractCacheThirdService<Q, T> {

    @Resource
    protected UniViewProperties uniViewProperties;
    @Resource
    private UniViewLoginOneServiceImpl uniViewLoginOneService;

    @Override
    protected void validateParam() {
        super.validateParam();
        AssertUtil.isTure(uniViewProperties.isEnable(), ThirdExceptionCodeEnum.THIRD_CLOSED);
    }

    @Override
    protected String getDomain() {
        return uniViewProperties.getDomain();
    }

    @Override
    protected void addHttpHeaders(Q req, HttpHeaders headers) {
        if (!isLogin()) {
            UniViewLoginDTO uniViewLoginDTO = new UniViewLoginDTO();
            uniViewLoginDTO.setThirdType(ThirdType.UNIVIEW_ONE_LOGIN);
            UniViewLoginVO uniViewLoginVO = uniViewLoginOneService.queryOne(uniViewLoginDTO);
            headers.add("Authorization", uniViewLoginVO.getAccessToken());
        }
    }

    @Override
    protected ParseResult<T> parseThirdResponse(String response) {
        JavaType javaType = JsonUtil.getJavaType(UniViewBaseResponse.class, this.getResClass());
        UniViewBaseResponse<T> uniViewBaseResponse = JsonUtil.readValue(response, javaType);
        uniViewBaseResponse = this.checkResponseStatus(uniViewBaseResponse);
        ParseResult<T> parseResult = new ParseResult<>();
        parseResult.setDataList(Lists.newArrayList());
        if (isOneService()) {
            if (Objects.nonNull(uniViewBaseResponse.getData())) {
                parseResult.getDataList().add(uniViewBaseResponse.getData());
            }
        } else if (isPageService()) {
            parseResult = parsePageResponse(response);
        }
        return parseResult;
    }

    /**
     * 解析分页响应
     *
     * @param response 响应
     * @return ParseResult
     */
    protected ParseResult<T> parsePageResponse(String response) {
        return new ParseResult<>();
    }

    /**
     * 检查响应状态
     *
     * @param uniViewBaseResponse 响应对象
     * @return 响应对象
     */
    protected UniViewBaseResponse<T> checkResponseStatus(UniViewBaseResponse<T> uniViewBaseResponse) {
        AssertUtil.isTure(Objects.nonNull(uniViewBaseResponse), ThirdExceptionCodeEnum.THIRD_CALL_FAIL);
        if (ThirdConstant.UNIVIEW_SUCCESS_STATUS.equals(uniViewBaseResponse.getCode())) {
            return uniViewBaseResponse;
        }
        throw new RemoteServiceException(ThirdExceptionCodeEnum.THIRD_CALL_FAIL.getCode() + CommonConstant.DEFAULT_JOINER + uniViewBaseResponse.getCode(),
                uniViewBaseResponse.getMessage());
    }

    /**
     * 是否登录
     *
     * @return bool
     */
    protected boolean isLogin() {
        return false;
    }

    @Override
    protected HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
