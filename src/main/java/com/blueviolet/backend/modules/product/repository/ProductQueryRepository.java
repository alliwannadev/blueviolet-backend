package com.blueviolet.backend.modules.product.repository;

import com.blueviolet.backend.common.constant.ProductSortingStandard;
import com.blueviolet.backend.common.domain.OrderByNull;
import com.blueviolet.backend.modules.product.repository.dto.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.blueviolet.backend.modules.option.domain.QProductOptionCombination.productOptionCombination;
import static com.blueviolet.backend.modules.product.domain.QProduct.product;
import static com.blueviolet.backend.modules.product.domain.QProductGroup.productGroup;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory primaryQueryFactory;

    @Transactional(readOnly = true)
    public Page<SearchProductDto> findAllByCond(
            SearchProductListCond condition,
            Pageable pageable
    ) {
        ProductSortingStandard productSortingStandard = ProductSortingStandard.getByCode(condition.sortStandard());
        List<SearchProductDto> content = primaryQueryFactory
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
                .where(
                        productGroup.category.categoryId.in(condition.categoryIds()),
                        colorIn(condition.colors()),
                        sizeIn(condition.sizes()),
                        sellingPriceBetween(condition.priceRange())
                )
                .orderBy(getOrderByStandard(productSortingStandard))
                .groupBy(product.productId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = primaryQueryFactory
                .select(product.productId.countDistinct())
                .from(productOptionCombination)
                .join(productOptionCombination.product, product)
                .join(product.productGroup, productGroup)
                .where(
                        productGroup.category.categoryId.in(condition.categoryIds()),
                        colorIn(condition.colors()),
                        sizeIn(condition.sizes()),
                        sellingPriceBetween(condition.priceRange())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, ObjectUtils.isEmpty(count) ? 0 : count);
    }

    @Transactional(readOnly = true)
    public List<GetProductDto> findInfiniteScrollAllByCond(
            List<Long> categoryIds,
            Long productId,
            int pageSize
    ) {
        return primaryQueryFactory
                .select(
                        new QGetProductDto(
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
                .from(product)
                .join(product.productGroup, productGroup)
                .where(
                        productGroup.category.categoryId.in(categoryIds),
                        productIdLt(productId)
                )
                .orderBy(product.productId.desc())
                .groupBy(product.productId)
                .limit(pageSize + 1)
                .fetch();
    }

    private BooleanExpression productIdLt(Long productId) {
        if (productId == null) {
            return null;
        }

        return product.productId.lt(productId);
    }

    private BooleanExpression colorIn(List<String> colors) {
        return CollectionUtils.isEmpty(colors) ? null : productOptionCombination.color.in(colors);
    }

    private BooleanExpression sizeIn(List<String> sizes) {
        return CollectionUtils.isEmpty(sizes) ? null : productOptionCombination.size.in(sizes);
    }

    private OrderSpecifier<?> getOrderByStandard(ProductSortingStandard productSortingStandard) {
        return switch (productSortingStandard) {
            case PRICE_LOW -> new OrderSpecifier<>(Order.ASC, product.sellingPrice);
            case PRICE_HIGH -> new OrderSpecifier<>(Order.DESC, product.sellingPrice);
            default -> OrderByNull.DEFAULT;
        };
    }

    private BooleanExpression sellingPriceBetween(SearchProductListCond.PriceRange priceRange) {
        return ObjectUtils.isEmpty(priceRange) ? null :
                product.sellingPrice.between(priceRange.minPrice(), priceRange.maxPrice());
    }
}
