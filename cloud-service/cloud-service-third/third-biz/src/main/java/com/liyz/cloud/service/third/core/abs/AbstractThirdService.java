package com.liyz.cloud.service.third.core.abs;

import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.service.third.constant.ThirdExceptionCodeEnum;
import com.liyz.cloud.service.third.constant.ThirdType;
import com.liyz.cloud.service.third.core.ListThirdService;
import com.liyz.cloud.service.third.core.OneThirdService;
import com.liyz.cloud.service.third.core.PageThirdService;
import com.liyz.cloud.service.third.core.ThirdService;
import com.liyz.cloud.service.third.dto.ThirdBaseDTO;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:18
 */
@Slf4j
public abstract class AbstractThirdService<Q extends ThirdBaseDTO> implements ThirdService<Q> {

    private static final WebClient webClient = WebClient.create();

    /**
     * feign 请求
     *
     * @param req 请求
     * @return response
     */
    @Deprecated
    protected abstract Response getResponse(Q req);

    /**
     * 获取第三方类型
     *
     * @return 第三方类型
     */
    public abstract ThirdType getThirdType();

    /**
     * 查询第三方接口数据
     *
     * @param req 请求参数
     * @return response
     */
    @Override
    public String query(Q req) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(this.getDomain()).path(this.getThirdType().getSubUrl())
                    .queryParams(addQueryParams(req)).build(uriVariables(req));
            if (getHttpMethod() == HttpMethod.GET) {
                return webClient.get()
                        .uri(uri)
                        .headers(headers -> addHttpHeaders(req, headers))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            } else {
                return webClient.method(getHttpMethod())
                        .uri(uri)
                        .headers(headers -> addHttpHeaders(req, headers))
                        .bodyValue(getReqBody(req))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }

        } catch (Exception e) {
            log.error("三方接口({})调用获取body失败", this.getThirdType().getThirdName(), e);
            throw new RemoteServiceException(ThirdExceptionCodeEnum.THIRD_CALL_FAIL);
        }
    }

    /**
     * 获取域名
     *
     * @return 域名u
     */
    protected abstract String getDomain();

    /**
     * 获取请求类型
     *
     * @return HttpMethod
     */
    protected HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 请求头
     *
     * @param req 请求参数
     * @return headersConsumer
     */
    protected void addHttpHeaders(Q req, HttpHeaders headers) {}

    /**
     * 获取请求url路劲参数
     *
     * @param req 请求参数
     * @return map
     */
    protected Map<String, ?> uriVariables(Q req) {
        return new HashMap<>();
    }

    /**
     * 请求体
     *
     * @param req 请求参数
     * @return reqBody
     */
    protected Object getReqBody(Q req) {
        return new HashMap<>();
    }

    /**
     * 请求属性
     *
     * @param req 请求参数
     */
    protected MultiValueMap<String, String> addQueryParams(Q req) {
        return new LinkedMultiValueMap<>();
    }

    /**
     * 是否单条查询服务
     *
     * @return true:是;false:否
     */
    protected boolean isOneService() {
        return this instanceof OneThirdService<?,?>;
    }

    /**
     * 是否列表查询服务
     *
     * @return true:是;false:否
     */
    protected boolean isListService() {
        return this instanceof ListThirdService<?,?>;
    }

    /**
     * 是否分页服务
     *
     * @return true:是;false:否
     */
    protected boolean isPageService() {
        return this instanceof PageThirdService<?,?>;
    }
}
