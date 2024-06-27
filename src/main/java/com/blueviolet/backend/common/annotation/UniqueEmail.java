package com.blueviolet.backend.common.annotation;

import com.blueviolet.backend.common.validator.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "이미 존재하는 이메일입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
