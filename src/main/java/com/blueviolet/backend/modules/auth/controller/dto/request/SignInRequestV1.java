package com.blueviolet.backend.modules.auth.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestV1 {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @JsonCreator
    public SignInRequestV1(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password
    ) {
        this.email = email;
        this.password = password;
    }
}
