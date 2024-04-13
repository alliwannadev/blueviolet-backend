package com.blueviolet.backend.modules.option.repository;

import com.blueviolet.backend.modules.option.domain.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductOptionCombinationRepository extends JpaRepository<ProductOptionCombination, Long> {

    @Query(
        """
        select  poc
        from    ProductOptionCombination poc
        where   poc.product.productId = :productId
        """
    )
    List<ProductOptionCombination> findAllByProductId(Long productId);
}
