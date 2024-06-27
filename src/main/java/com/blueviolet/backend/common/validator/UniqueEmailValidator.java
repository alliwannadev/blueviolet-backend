package com.blueviolet.backend.common.validator;

import com.blueviolet.backend.common.annotation.UniqueEmail;
import com.blueviolet.backend.modules.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean existsByEmail = userRepository.existsByEmail(email);

        if (existsByEmail) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MessageFormat.format("해당 이메일({0})은 이미 존재합니다!", email))
                    .addConstraintViolation();
        }

        return !existsByEmail;
    }
}
