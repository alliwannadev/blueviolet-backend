package com.blueviolet.backend.modules.category.repository;

import com.blueviolet.backend.modules.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
