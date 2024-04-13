package com.blueviolet.backend.modules.product.repository;

import com.blueviolet.backend.modules.product.domain.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {

}
