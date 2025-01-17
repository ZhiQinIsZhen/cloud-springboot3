package com.liyz.cloud.common.feign.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.IExceptionService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:restful response body
 *
 * @author lyz
 * @version 1.0.0
 * @date 2023/11/13 10:17
 */
@Getter
@Setter
@JsonPropertyOrder({"code", "message", "data"})
public class Result<T> {

    public Result() {
        this((T) null);
    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(T data) {
        this(CommonExceptionCodeEnum.SUCCESS);
        this.data = data;
    }

    public Result(IExceptionService codeEnum) {
        this(codeEnum.getCode(), codeEnum.getMessage());
    }

    @Schema(description = "code码")
    private String code;

    @Schema(description = "消息")
    private String message;

    @Schema(description = "数据体")
    private T data;

    public static <E> Result<E> success(E data) {
        return new Result<>(data);
    }

    public static <E> Result<E> success() {
        return success(null);
    }

    public static <E> Result<E> error(IExceptionService codeEnum) {
        return new Result<>(codeEnum);
    }

    public static <E> Result<E> error(String code, String message) {
        return new Result<>(code, message);
    }
}
