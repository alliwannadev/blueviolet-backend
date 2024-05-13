package com.blueviolet.backend.modules.category.helper;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.category.domain.Category;
import com.blueviolet.backend.modules.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CategoryTestHelper {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(
            String name,
            Integer level,
            String pathName
    ) {
        return categoryRepository.save(
                Category.of(
                        name,
                        level,
                        pathName
                )
        );
    }

    @Transactional
    public Category createWithDefaultValue() {
        Category category1 = create(
                "남성",
                1,
                "남성"
        );
        Category category2 = create(
                "상의",
                2,
                "남성>상의"
        );
        changeParentCategory(category2, category1);
        Category category3 = create(
                "반소매 티셔츠",
                3,
                "남성>상의>반소매 티셔츠"
        );
        changeParentCategory(category3, category2);

        return category3;
    }

    public void changeParentCategory(
            Category category,
            Category parentCategory
    ) {
        category.changeParentCategory(parentCategory);
    }

    @Transactional(readOnly = true)
    public Category findOneByName(String name) {
        return categoryRepository
                .findOneByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
