package com.blueviolet.backend.modules.admin.category.service;

import com.blueviolet.backend.modules.category.repository.CategoryRepository;
import com.blueviolet.backend.modules.admin.category.service.dto.CreateCategoryParam;
import com.blueviolet.backend.modules.admin.category.service.dto.GetCategoryResult;
import com.blueviolet.backend.modules.category.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void createCategories(CreateCategoryParam createCategoryParam) {
        Map<String, Category> mappingTable = new LinkedHashMap<>();
        List<CreateCategoryParam.CategoryDto> sortedCategories =
                createCategoryParam
                        .categories()
                        .stream()
                        .sorted(Comparator.comparing(CreateCategoryParam.CategoryDto::categoryKey))
                        .toList();

        for (CreateCategoryParam.CategoryDto categoryDto : sortedCategories) {
            Category category = mappingTable.getOrDefault(
                    categoryDto.categoryKey(),
                    Category.of(
                            categoryDto.name(),
                            categoryDto.level()
                    )
            );

            category.changeParentCategory(
                    mappingTable.getOrDefault(categoryDto.parentCategoryKey(), null)
            );
            mappingTable.put(categoryDto.categoryKey(), category);
        }

        categoryRepository.saveAll(
                mappingTable.values()
        );
    }

    @Transactional(readOnly = true)
    public List<GetCategoryResult> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(GetCategoryResult::fromEntity)
                .toList();
    }
}
