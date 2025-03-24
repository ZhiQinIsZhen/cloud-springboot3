package com.liyz.cloud.common.feign.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * DESC:枚举值验证注解
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/1/4 16:00
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValueValidator.class})
@Documented
public @interface EnumValue {

    String message() default "{jakarta.validation.constraints.EnumValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?>[] target() default {};

    String field() default "getCode";
}
