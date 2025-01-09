package com.liyz.cloud.common.base.advice;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.RemoteServiceException;
import com.liyz.cloud.common.feign.bo.RemotePage;
import com.liyz.cloud.common.feign.result.PageResult;
import com.liyz.cloud.common.feign.result.Result;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Desc:全局result解码处理器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2024/5/9 15:18
 */
@AllArgsConstructor
public class ResultFeignDecoderAdvice implements Decoder {

    private final ObjectMapper objectMapper;

    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        String responseStr = Util.toString(response.body().asReader(Util.UTF_8));
        JavaType paramType = objectMapper.getTypeFactory().constructType(type);
        if (paramType.hasRawClass(Result.class) || paramType.hasRawClass(PageResult.class)) {
            return objectMapper.readValue(responseStr, paramType);
        }
        if (paramType.hasRawClass(RemotePage.class)) {
            JavaType resultType = objectMapper.getTypeFactory().constructParametricType(PageResult.class, paramType.getBindings());
            PageResult<?> pageResult = objectMapper.readValue(responseStr, resultType);
            if (!CommonExceptionCodeEnum.SUCCESS.getCode().equals(pageResult.getCode())) {
                throw new RemoteServiceException(pageResult.getCode(), pageResult.getMessage());
            }
            return RemotePage.of(pageResult.getData(), pageResult.getTotal(), pageResult.getPageNum(), pageResult.getPageSize());
        } else {
            JavaType resultType = objectMapper.getTypeFactory().constructParametricType(Result.class, paramType);
            Result<?> result = objectMapper.readValue(responseStr, resultType);
            if (!CommonExceptionCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                throw new RemoteServiceException(result.getCode(), result.getMessage());
            }
            return result.getData();
        }
    }


}
