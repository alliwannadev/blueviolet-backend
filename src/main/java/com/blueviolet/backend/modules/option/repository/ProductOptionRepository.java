package com.blueviolet.backend.modules.option.repository;

import com.blueviolet.backend.modules.option.domain.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

}
