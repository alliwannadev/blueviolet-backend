package com.blueviolet.backend.modules.category.domain;


import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    private String name;

    private Integer level;

    @Builder(access = AccessLevel.PRIVATE)
    private Category(
            Category parentCategory,
            String name,
            Integer level) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.level = level;
    }

    public static Category of(
            String name,
            Category parentCategory,
            Integer level
    ) {
        Category category = Category
                .builder()
                .name(name)
                .level(level)
                .build();

        // 연관관계 설정
        category.changeParentCategory(parentCategory);
        return category;
    }

    // 연관관계 편의 메소드
    public void changeParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
