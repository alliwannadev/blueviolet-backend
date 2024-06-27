package com.blueviolet.backend.modules.stock.service;

import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public Optional<Stock> getOneByCombinationId(Long productOptionCombinationId) {
        return stockRepository.findOneByProductOptionCombinationId(productOptionCombinationId);
    }

    // TODO: 해당 방식은 데드락이 발생할 수 있기 때문에 이후에 다른 방법을 적용하여 개선해야 함
    @Transactional
    public Stock getOneByIdWithLock(Long stockId) {
        return stockRepository
                .findOneByStockIdWithLock(stockId);
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
    public void increaseQuantityByStockId(
            Long stockId,
            Long quantity
    ) {
        Stock foundStock = getOneByIdWithLock(stockId);
        foundStock.increaseQuantity(quantity);
    }

    @Transactional
    public void decreaseQuantityByStockId(
            Long stockId,
            Long quantity
    ) {
        Stock foundStock = getOneByIdWithLock(stockId);
        foundStock.decreaseQuantity(quantity);
    }
}
