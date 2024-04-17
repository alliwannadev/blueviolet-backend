package com.blueviolet.backend.modules.admin.warehosing.service;

import com.blueviolet.backend.common.util.DateTimeUtil;
import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.admin.stock.service.AdminStockService;
import com.blueviolet.backend.modules.warehousing.domain.ProductWarehousing;
import com.blueviolet.backend.modules.warehousing.repository.ProductWarehousingRepository;
import com.blueviolet.backend.modules.admin.warehosing.service.dto.CreateWarehousingParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminProductWarehousingService {

    private final ProductWarehousingRepository productWarehousingRepository;

    private final AdminStockService adminStockService;

    @Transactional
    public void create(
            CreateWarehousingParam parameter
    ) {
        Optional<Stock> findStock = adminStockService.getOneByCombinationId(parameter.productOptionCombinationId());
        Stock foundStock =
                findStock.orElseGet(
                        () -> adminStockService.create(
                                parameter.productOptionCombinationId(),
                                parameter.productOptionCombinationName()
                        )
                );

        productWarehousingRepository.save(
                ProductWarehousing.of(
                        foundStock,
                        DateTimeUtil.toLocalDate(parameter.warehousingDate()),
                        parameter.productOptionCombinationId(),
                        parameter.quantity()
                )
        );

        adminStockService.processWarehousing(
                foundStock.getStockId(),
                parameter.quantity()
        );
    }
}