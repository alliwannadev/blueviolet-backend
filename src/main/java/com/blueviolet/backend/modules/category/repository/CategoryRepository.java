package com.blueviolet.backend.modules.category.repository;

import com.blueviolet.backend.modules.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findOneByCategoryId(Long categoryId);
}
