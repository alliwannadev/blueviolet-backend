package com.blueviolet.backend.modules.admin.warehousing.controller;

import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.modules.admin.warehousing.controller.dto.CreateWarehousingRequestV1;
import com.blueviolet.backend.modules.admin.warehousing.service.AdminProductWarehousingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class AdminWarehousingApiV1 {

    private final AdminProductWarehousingService adminProductWarehousingService;

    @PostMapping(AdminWarehousingApiPathsV1.V1_WAREHOUSING)
    public OkResponse<Void> create(
            @Valid @RequestBody CreateWarehousingRequestV1 createWarehousingRequestV1
    ) {
        adminProductWarehousingService.create(
                createWarehousingRequestV1.toDto()
        );
        return OkResponse.empty();
    }

}
