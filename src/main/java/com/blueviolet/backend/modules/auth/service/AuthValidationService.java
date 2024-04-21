package com.blueviolet.backend.modules.auth.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthValidationService {

    private final PasswordEncoder passwordEncoder;

    public void validateIfMatchesPassword(
            String rawPassword,
            String encodedPassword
    ) {
        if (StringUtils.isBlank(rawPassword)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }

        if (!passwordEncoder.matches(
                rawPassword,
                encodedPassword)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
    }

}
