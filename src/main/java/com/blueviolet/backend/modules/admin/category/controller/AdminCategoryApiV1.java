package com.blueviolet.backend.modules.admin.category.controller;

import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.modules.admin.category.controller.dto.CreateCategoryRequestV1;
import com.blueviolet.backend.modules.admin.category.controller.dto.GetCategoryResponseV1;
import com.blueviolet.backend.modules.admin.category.service.AdminCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
public class AdminCategoryApiV1 {

    private final AdminCategoryService adminCategoryService;

    @PostMapping(AdminCategoryApiPathsV1.V1_CATEGORIES)
    public OkResponse<Void> createCategories(
            @Valid @RequestBody CreateCategoryRequestV1 createCategoryRequestV1
    ) {
        adminCategoryService.createCategories(createCategoryRequestV1.toDto());
        return OkResponse.of("카테고리 생성을 완료하였습니다.");
    }

    @GetMapping(AdminCategoryApiPathsV1.V1_CATEGORIES)
    public OkResponse<List<GetCategoryResponseV1>> getCategories() {
        return OkResponse.of(
                adminCategoryService.getCategories()
                        .stream()
                        .map(GetCategoryResponseV1::fromDto)
                        .toList()
        );
    }
}
