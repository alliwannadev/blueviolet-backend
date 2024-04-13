package com.blueviolet.backend.modules.product.repository;

import com.blueviolet.backend.modules.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findOneByProductCode(String productCode);
}
