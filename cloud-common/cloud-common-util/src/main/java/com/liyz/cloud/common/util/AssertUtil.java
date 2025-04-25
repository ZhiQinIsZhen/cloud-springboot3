package com.liyz.cloud.common.util;

import com.liyz.cloud.common.exception.CommonExceptionCodeEnum;
import com.liyz.cloud.common.exception.IExceptionService;
import com.liyz.cloud.common.exception.RemoteServiceException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * Desc:
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025-04-24 10:34
 */
@UtilityClass
public class AssertUtil {

    /**
     * 判断对象不能为空
     *
     * @param object 对象
     */
    public static void notNull(Object object) {
        notNull(object, CommonExceptionCodeEnum.PARAMS_IS_NULL);
    }

    /**
     * 判断对象不能为空
     *
     * @param object 对象
     * @param codeService 异常信息
     */
    public static void notNull(Object object, IExceptionService codeService) {
        if (Objects.isNull(object)) {
            throw new RemoteServiceException(codeService);
        }
    }

    /**
     * 判断集合不能为空
     *
     * @param collection 集合
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, CommonExceptionCodeEnum.COLLECTION_IS_EMPTY);
    }

    /**
     * 判断集合不能为空
     *
     * @param collection 集合
     * @param codeService 异常信息
     */
    public static void notEmpty(Collection<?> collection, IExceptionService codeService) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new RemoteServiceException(codeService);
        }
    }

    /**
     * 判断字符串不能为空
     *
     * @param content 字符串
     */
    public static void notBlank(String content) {
        notBlank(content, CommonExceptionCodeEnum.PARAMS_IS_NULL);
    }

    /**
     * 判断字符串不能为空
     *
     * @param content 字符串
     * @param codeService 异常信息
     */
    public static void notBlank(String content, IExceptionService codeService) {
        if (StringUtils.isBlank(content)) {
            throw new RemoteServiceException(codeService);
        }
    }

    /**
     * 判断flag为false
     *
     * @param flag flag
     * @param codeService 异常信息
     */
    public static void isFalse(Boolean flag, IExceptionService codeService) {
        state(!flag, codeService);
    }

    /**
     * 判断flag为true
     *
     * @param flag flag
     * @param codeService 异常信息
     */
    public static void isTure(Boolean flag, IExceptionService codeService) {
        state(flag, codeService);
    }

    /**
     * 判断表达式为true
     *
     * @param expression 表达式
     * @param codeService 异常信息
     */
    public static void state(boolean expression, IExceptionService codeService) {
        if (!expression) {
            throw new RemoteServiceException(codeService);
        }
    }
}
