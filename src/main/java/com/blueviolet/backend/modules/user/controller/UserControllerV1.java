package com.blueviolet.backend.modules.user.controller;

import com.blueviolet.backend.modules.user.controller.response.GetUserResponseV1;
import lombok.RequiredArgsConstructor;
import com.blueviolet.backend.common.dto.OkResponse;

import com.blueviolet.backend.modules.user.domain.User;
import com.blueviolet.backend.modules.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
public class UserControllerV1 {

    private final UserService userService;

    @GetMapping(ApiPathsV1.V1_USERS)
    public OkResponse<List<GetUserResponseV1>> getUserList() {
        List<User> userList = userService.getUserList();
        return OkResponse.of(GetUserResponseV1.fromEntities(userList));
    }
}
