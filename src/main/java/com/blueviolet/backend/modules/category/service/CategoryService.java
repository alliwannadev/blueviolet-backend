package com.blueviolet.backend.modules.category.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.category.domain.Category;
import com.blueviolet.backend.modules.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // TODO: 캐싱을 이용한 성능 개선 필요
    @Transactional(readOnly = true)
    public List<Long> getChildCategoryIdsByCurrentId(Long categoryId) {
        return getChildCategoryIdsByIdWithRecursive(categoryId, new ArrayList<>());
    }

    private List<Long> getChildCategoryIdsByIdWithRecursive(Long categoryId, List<Long> path) {
        Category foundCategory =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
        for (Category childCategory : foundCategory.getChildCategories()) {
            path.add(childCategory.getCategoryId());
            getChildCategoryIdsByIdWithRecursive(childCategory.getCategoryId(), path);
        }

        return path;
    }
}
