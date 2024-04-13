package com.blueviolet.backend.modules.option.repository;

import com.blueviolet.backend.modules.option.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    @Query(
            """
            select  po
            from    ProductOption po
            where   po.productOptionCombination.productOptionGroupId = :productOptionGroupId
            """
    )
    List<ProductOption> findAllByProductOptionGroupId(Long productOptionGroupId);
}
