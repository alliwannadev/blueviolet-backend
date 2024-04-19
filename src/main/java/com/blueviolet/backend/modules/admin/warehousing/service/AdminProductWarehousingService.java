package com.blueviolet.backend.modules.admin.warehousing.service;

import com.blueviolet.backend.common.util.DateTimeUtil;
import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.stock.service.StockService;
import com.blueviolet.backend.modules.warehousing.domain.ProductWarehousing;
import com.blueviolet.backend.modules.warehousing.repository.ProductWarehousingRepository;
import com.blueviolet.backend.modules.admin.warehousing.service.dto.CreateWarehousingParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminProductWarehousingService {

    private final ProductWarehousingRepository productWarehousingRepository;

    private final StockService stockService;

    @Transactional
    public void create(
            CreateWarehousingParam parameter
    ) {
        Optional<Stock> findStock = stockService.getOneByCombinationId(parameter.productOptionCombinationId());
        Stock foundStock =
                findStock.orElseGet(
                        () -> stockService.create(
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

        stockService.increaseQuantityByStockId(
                foundStock.getStockId(),
                parameter.quantity()
        );
    }
}