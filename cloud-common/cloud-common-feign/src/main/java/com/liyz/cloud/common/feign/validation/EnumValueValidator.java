package com.liyz.cloud.common.feign.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * DESC:其枚举值验证器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/1/4 16:00
 */
@Slf4j
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private String field;

    private Class<?>[] targetEnums;

    private Map<Class<?>, Method> methodCache = new HashMap<>();

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.targetEnums = constraintAnnotation.target();
        this.field = constraintAnnotation.field();
        cacheEnumMethods();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.toString().isEmpty()) {
            return true;
        }
        return Stream.of(targetEnums).anyMatch(enumClass -> enumClass.isEnum() && isEnumValid(enumClass, value));
    }

    /**
     * 缓存枚举方法
     */
    private void cacheEnumMethods() {
        for (Class<?> enumClass : targetEnums) {
            if (enumClass.isEnum()) {
                try {
                    Method method = enumClass.getMethod(field);
                    methodCache.put(enumClass, method);
                } catch (NoSuchMethodException e) {
                    log.warn("Failed to cache method for enum: {}", enumClass.getName());
                }
            }
        }
    }

    /**
     * 枚举有效
     *
     * @param enumClass 枚举类
     * @param value 价值
     *
     * @return boolean
     */
    private boolean isEnumValid(Class<?> enumClass, Object value) {
        Method method = methodCache.get(enumClass);
        if (method != null) {
            for (Object enumConstant : enumClass.getEnumConstants()) {
                try {
                    Object code = method.invoke(enumConstant);
                    if (value.toString().equals(code.toString())) {
                        return true;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.warn("Error validating enum: {}", enumClass.getName());
                }
            }
        }
        return false;
    }
}
