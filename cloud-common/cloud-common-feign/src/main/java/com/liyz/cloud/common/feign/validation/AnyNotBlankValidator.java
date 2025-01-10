package com.liyz.cloud.common.feign.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * DESC:其中一个不为blank验证器
 *
 * @author lyz
 * @version 1.0.0
 * @date 2025/1/4 16:00
 */
public class AnyNotBlankValidator implements ConstraintValidator<AnyNotBlank, String> {

    private static final ThreadLocal<Map<String, Boolean>> validContext = new InheritableThreadLocal<>();
    private String[] fields;

    @Override
    public void initialize(AnyNotBlank constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean validResult = false;
        try {
            String fieldName = ((ConstraintValidatorContextImpl) context).getConstraintViolationCreationContexts().getFirst().getPath().getLeafNode().getName();
            Boolean valid = StringUtils.isNotBlank(value);
            Map<String, Boolean> map = validContext.get();
            if (map == null) {
                map = new HashMap<>();
                validContext.set(map);
            }
            validContext.get().put(fieldName, valid);
            for (String field : fields) {
                if (!validContext.get().containsKey(field)) {
                    return true;
                }
                if (validContext.get().getOrDefault(field, false)) {
                    validResult = true;
                    break;
                }
            }
            validContext.remove();
        } catch (Exception e) {
            validContext.remove();
            throw e;
        }
        return validResult;
    }
}
