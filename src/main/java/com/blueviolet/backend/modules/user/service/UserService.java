package com.blueviolet.backend.modules.user.service;

import com.blueviolet.backend.common.constant.Role;
import com.blueviolet.backend.modules.user.domain.UserRole;
import com.blueviolet.backend.modules.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.user.domain.User;
import com.blueviolet.backend.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public User saveNewUser(User newUser) {
        User savedUser = userRepository.save(newUser);
        userRoleRepository.save(UserRole.of(savedUser, Role.USER));
        return savedUser;
    }

    @Transactional(readOnly = true)
    public Optional<User> getOptionalOneByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Transactional(readOnly = true)
    public User getOneByUserId(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<User> getOptionalOneByUserId(Long userId) {
        return userRepository
                .findById(userId);
    }

    @Transactional(readOnly = true)
    public Long getCountByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
