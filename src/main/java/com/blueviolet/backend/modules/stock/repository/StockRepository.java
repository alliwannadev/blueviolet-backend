package com.blueviolet.backend.modules.stock.repository;

import com.blueviolet.backend.modules.stock.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findOneByProductOptionCombinationId(Long productOptionCombinationId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
        """
        select  s
        from    Stock s
        where   s.stockId = :stockId
        """
    )
    Stock findOneByStockIdWithLock(Long stockId);
}
