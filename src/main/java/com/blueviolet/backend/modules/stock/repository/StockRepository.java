package com.blueviolet.backend.modules.stock.repository;

import com.blueviolet.backend.modules.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findOneByProductOptionCombinationId(Long productOptionCombinationId);

}
