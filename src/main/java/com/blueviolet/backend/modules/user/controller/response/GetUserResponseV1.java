package com.blueviolet.backend.modules.user.controller.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.blueviolet.backend.modules.user.domain.User;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetUserResponseV1 {

    private Long userId;

    private String email;

    private String password;

    private String name;

    private String phone;

    @Builder(access = AccessLevel.PRIVATE)
    public GetUserResponseV1(
            Long userId,
            String email,
            String password,
            String name,
            String phone
    ) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public static GetUserResponseV1 fromEntity(
            User user
    ) {
        return GetUserResponseV1
                .builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }

    public static List<GetUserResponseV1> fromEntities(
            List<User> userList
    ) {
        List<GetUserResponseV1> userResponseList = userList.stream()
                .map(GetUserResponseV1::fromEntity)
                .toList();

        return userResponseList;
    }
}
