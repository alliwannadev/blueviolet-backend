package com.blueviolet.backend.modules.product.repository;

import com.blueviolet.backend.common.constant.ProductSortingStandard;
import com.blueviolet.backend.common.domain.OrderByNull;
import com.blueviolet.backend.modules.product.repository.dto.QSearchProductDto;
import com.blueviolet.backend.modules.product.repository.dto.SearchProductDto;
import com.blueviolet.backend.modules.product.repository.dto.SearchProductListCond;
import com.querydsl.core.BooleanBuilder;
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

import java.text.MessageFormat;
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
                        filteringOptionLikeForList("COLOR", condition.colors()),
                        filteringOptionLikeForList("SIZE", condition.sizes()),
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
                        filteringOptionLikeForList("COLOR", condition.colors()),
                        filteringOptionLikeForList("SIZE", condition.sizes()),
                        sellingPriceBetween(condition.priceRange())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, ObjectUtils.isEmpty(count) ? 0 : count);
    }

    private OrderSpecifier<?> getOrderByStandard(ProductSortingStandard productSortingStandard) {
        return switch (productSortingStandard) {
            case PRICE_LOW -> new OrderSpecifier<>(Order.ASC, product.sellingPrice);
            case PRICE_HIGH -> new OrderSpecifier<>(Order.DESC, product.sellingPrice);
            default -> OrderByNull.DEFAULT;
        };
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
