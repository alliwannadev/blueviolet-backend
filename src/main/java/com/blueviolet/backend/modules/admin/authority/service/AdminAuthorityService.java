package com.blueviolet.backend.modules.admin.authority.service;

import com.blueviolet.backend.common.constant.Role;
import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.user.domain.User;
import com.blueviolet.backend.modules.user.domain.UserRole;
import com.blueviolet.backend.modules.user.repository.UserRoleRepository;
import com.blueviolet.backend.modules.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminAuthorityService {

    private final UserService userService;
    private final UserRoleRepository userRoleRepository;

    public void createByUserId(Long userId) {
        if (existsAdminRoleByUserId(userId)) {
            throw new BusinessException(ErrorCode.ADMIN_AUTHORITY_ALREADY_EXISTS);
        }

        User foundUser = userService.getOneByUserId(userId);
        userRoleRepository.save(UserRole.of(foundUser, Role.ADMIN));
    }

    public boolean existsAdminRoleByUserId(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserIdAndRole(
                userId,
                Role.ADMIN,
                PageRequest.of(0, 1)
        );
        return !userRoles.isEmpty();
    }
}
