package com.blueviolet.backend.modules.product.repository;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.category.domain.Category;
import com.blueviolet.backend.modules.category.repository.CategoryRepository;
import com.blueviolet.backend.modules.product.repository.dto.QSearchProductDto;
import com.blueviolet.backend.modules.product.repository.dto.SearchProductDto;
import com.blueviolet.backend.modules.product.service.dto.SearchProductListCond;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.List;

import static com.blueviolet.backend.modules.category.domain.QCategory.category;
import static com.blueviolet.backend.modules.option.domain.QProductOptionCombination.productOptionCombination;
import static com.blueviolet.backend.modules.product.domain.QProduct.product;
import static com.blueviolet.backend.modules.product.domain.QProductGroup.productGroup;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final CategoryRepository categoryRepository;

    public Page<SearchProductDto> findAllByCond(
            SearchProductListCond condition,
            Pageable pageable
    ) {
        String pathName = getCategoryPathName(condition.categoryId());
        JPAQuery<SearchProductDto> mainQuery = queryFactory
                .select(
                        new QSearchProductDto(
                                product.productId,
                                product.productGroup.productGroupId.max(),
                                product.productCode.max(),
                                product.productName.max(),
                                product.modelName.max(),
                                product.purchasePrice.max(),
                                product.sellingPrice.max(),
                                product.description.max(),
                                product.isDisplayed.max()
                        )
                )
                .from(productOptionCombination)
                .join(productOptionCombination.product, product)
                .join(product.productGroup, productGroup)
                .join(productGroup.category, category)
                .where(
                        category.pathName.like(pathName),
                        filteringOptionLikeForList("COLOR", condition.colors()),
                        filteringOptionLikeForList("SIZE", condition.sizes()),
                        sellingPriceBetween(condition.priceRange())
                );

        JPAQuery<SearchProductDto> queryContainingOrderByClause;
        if (StringUtils.equalsIgnoreCase(condition.sortStandard(), "PRICE_LOW")) {
            queryContainingOrderByClause = mainQuery.orderBy(product.sellingPrice.asc());
        } else if (StringUtils.equalsIgnoreCase(condition.sortStandard(), "PRICE_HIGH")) {
            queryContainingOrderByClause = mainQuery.orderBy(product.sellingPrice.desc());
        } else {
            queryContainingOrderByClause = mainQuery;
        }

        JPAQuery<SearchProductDto> resultQuery = queryContainingOrderByClause.groupBy(product.productId);

        List<SearchProductDto> content =
                resultQuery
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        long count = resultQuery.fetch().size();

        return new PageImpl<>(content, pageable, count);
    }

    private String getCategoryPathName(Long categoryId) {
        Category foundCategory = categoryRepository
                .findOneByCategoryId(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));

        return MessageFormat.format("%{0}%", foundCategory.getPathName());
    }

    private static BooleanExpression sellingPriceBetween(SearchProductListCond.PriceRange priceRange) {
        return ObjectUtils.isEmpty(priceRange) ? null :
                product.sellingPrice.between(priceRange.minPrice(), priceRange.maxPrice());
    }

    private static BooleanBuilder filteringOptionLikeForList(String optionCode, List<String> optionValueList) {
        if (ObjectUtils.isEmpty(optionValueList)) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String optionValue : optionValueList) {
            String formatted = MessageFormat.format("%{0}-{1}%", optionCode, optionValue);
            booleanBuilder.or(productOptionCombination.filteringOption.like(formatted));
        }

        return booleanBuilder;
    }
}
