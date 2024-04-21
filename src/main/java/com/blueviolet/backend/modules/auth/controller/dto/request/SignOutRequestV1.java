package com.blueviolet.backend.modules.auth.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignOutRequestV1 {

    @NotBlank
    private String refreshToken;

    @JsonCreator
    public SignOutRequestV1(
            @JsonProperty("refreshToken") String refreshToken
    ) {
        this.refreshToken = refreshToken;
    }
}
