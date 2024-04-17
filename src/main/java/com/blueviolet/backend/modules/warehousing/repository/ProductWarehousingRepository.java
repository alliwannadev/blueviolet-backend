package com.blueviolet.backend.modules.warehousing.repository;

import com.blueviolet.backend.modules.warehousing.domain.ProductWarehousing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductWarehousingRepository extends JpaRepository<ProductWarehousing, Long> {
}
