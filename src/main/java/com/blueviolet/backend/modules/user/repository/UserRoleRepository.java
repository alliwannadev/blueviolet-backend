package com.blueviolet.backend.modules.user.repository;

import com.blueviolet.backend.common.constant.Role;
import com.blueviolet.backend.modules.user.domain.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query(
            """
            select  ur
            from    UserRole ur
            where   ur.user.userId = :userId
            and     ur.role = :role
            """
    )
    List<UserRole> findAllByUserIdAndRole(@Param("userId") Long userId, @Param("role") Role role, Pageable pageable);
}
