package com.blueviolet.backend.modules.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String password;

    private String name;

    private String phone;

    @Builder(access = AccessLevel.PRIVATE)
    private User(
            String email,
            String password,
            String name,
            String phone
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public static User of(
            String email,
            String password,
            String name,
            String phone
    ) {
        User user = User.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .build();

        return user;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
