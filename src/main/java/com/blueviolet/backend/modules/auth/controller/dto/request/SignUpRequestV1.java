package com.blueviolet.backend.modules.auth.controller.dto.request;

import com.blueviolet.backend.common.annotation.UniqueEmail;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.blueviolet.backend.modules.user.domain.User;

@Getter
@NoArgsConstructor
public class SignUpRequestV1 {

    @UniqueEmail
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @JsonCreator
    public SignUpRequestV1(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public User toUserEntity(String encodedPassword) {
        return User.of(
                email,
                encodedPassword,
                name,
                phone
        );
    }
}
