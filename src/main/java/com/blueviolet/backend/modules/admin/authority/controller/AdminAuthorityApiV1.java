package com.blueviolet.backend.modules.admin.authority.controller;

import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.modules.admin.authority.controller.dto.CreateAdminAuthorityRequestV1;
import com.blueviolet.backend.modules.admin.authority.service.AdminAuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class AdminAuthorityApiV1 {

    private final AdminAuthorityService adminAuthorityService;

    @PostMapping(AdminAuthorityApiPathsV1.V1_AUTHORITIES)
    public OkResponse<Void> createAuthority(
            @Valid @RequestBody CreateAdminAuthorityRequestV1 createAdminAuthorityRequestV1
    ) {
        adminAuthorityService.createByUserId(createAdminAuthorityRequestV1.userId());

        return OkResponse.empty();
    }
}
