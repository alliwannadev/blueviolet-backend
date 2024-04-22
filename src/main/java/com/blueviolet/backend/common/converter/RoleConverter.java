package com.blueviolet.backend.common.converter;

import com.blueviolet.backend.common.constant.Role;
import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Converter
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role orderStatus) {
        if (ObjectUtils.isEmpty(orderStatus)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "Role이 null 입니다.");
        }

        return orderStatus.getCode();
    }

    @Override
    public Role convertToEntityAttribute(String code) {
        if (StringUtils.isBlank(code)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "Role의 code가 올바르지 않습니다.");
        }

        return Role.getByCode(code);
    }
}
