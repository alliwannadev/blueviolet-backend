package com.blueviolet.backend.modules.user.domain;

import com.blueviolet.backend.common.constant.Role;
import com.blueviolet.backend.common.converter.RoleConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserRole {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Convert(converter = RoleConverter.class)
    private Role role;

    @Builder(access = AccessLevel.PRIVATE)
    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public static UserRole of(
            User user,
            Role role
    ) {
        UserRole userRole = UserRole.builder()
                .role(role)
                .build();

        // 연관관계 설정
        userRole.changeUser(user);

        return userRole;
    }

    // 연관관계 편의 메소드
    public void changeUser(User user) {
        this.user = user;
    }
}
