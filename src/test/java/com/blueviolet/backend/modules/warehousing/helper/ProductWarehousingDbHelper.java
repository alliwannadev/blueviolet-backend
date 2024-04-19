package com.blueviolet.backend.modules.warehousing.helper;

import com.blueviolet.backend.modules.admin.warehousing.service.AdminProductWarehousingService;
import com.blueviolet.backend.modules.admin.warehousing.service.dto.CreateWarehousingParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ProductWarehousingDbHelper {

    private final AdminProductWarehousingService adminProductWarehousingService;

    @Transactional
    public void createWarehousing(CreateWarehousingParam parameter) {
        adminProductWarehousingService.create(parameter);
    }
}
