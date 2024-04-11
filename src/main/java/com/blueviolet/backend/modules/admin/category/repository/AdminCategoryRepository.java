package com.blueviolet.backend.modules.admin.category.repository;

import com.blueviolet.backend.modules.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminCategoryRepository extends JpaRepository<Category, Long> {
}
