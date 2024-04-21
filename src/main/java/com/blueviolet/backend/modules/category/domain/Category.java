package com.blueviolet.backend.modules.category.domain;


import com.blueviolet.backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategories = new ArrayList<>();

    private String name;

    private Integer level;

    private String pathName;

    @Builder(access = AccessLevel.PRIVATE)
    private Category(
            Category parentCategory,
            String name,
            Integer level,
            String pathName
    ) {
        this.parentCategory = parentCategory;
        this.name = name;
        this.level = level;
        this.pathName = pathName;
    }

    public static Category of(
            String name,
            Integer level,
            String pathName
    ) {
        return Category
                .builder()
                .name(name)
                .level(level)
                .pathName(pathName)
                .build();
    }

    // 연관관계 편의 메소드
    public void changeParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
