package com.blueviolet.backend.modules.admin.stock.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminStockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public Optional<Stock> getOneByCombinationId(Long productOptionCombinationId) {
        return stockRepository.findOneByProductOptionCombinationId(productOptionCombinationId);
    }

    @Transactional(readOnly = true)
    public Stock getOneById(Long stockId) {
        return stockRepository
                .findById(stockId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
    }

    @Transactional
    public Stock create(
            Long productOptionCombinationId,
            String productOptionCombinationName
    ) {
        Stock stock = Stock.of(
                productOptionCombinationId,
                productOptionCombinationName,
                0L
        );
        return stockRepository.save(stock);
    }

    @Transactional
    public void processWarehousing(
            Long stockId,
            Long quantity
    ) {
        Stock foundStock = getOneById(stockId);
        foundStock.updateQuantity(foundStock.getQuantity() + quantity);
    }
}
