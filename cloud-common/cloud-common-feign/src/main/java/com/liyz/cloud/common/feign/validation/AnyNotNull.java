package com.liyz.cloud.common.feign.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * DESC:其中一个不为null
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/1/4 16:00
 */
@Documented
@Constraint(validatedBy = { })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface AnyNotNull {

    String message() default "{jakarta.validation.constraints.AnyNotNull.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String[] fields();
}
