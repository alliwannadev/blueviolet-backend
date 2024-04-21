package com.blueviolet.backend.modules.stock.helper;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class StockTestHelper {

    private final StockService stockService;

    @Transactional(readOnly = true)
    public Stock getOneByCombinationId(Long combinationId) {
        return stockService
                .getOneByCombinationId(combinationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
    }
}
