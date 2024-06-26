package com.blueviolet.backend.modules.product.controller;

import com.blueviolet.backend.common.dto.CustomInfiniteScrollResponseV1;
import com.blueviolet.backend.common.dto.CustomPageRequestV1;
import com.blueviolet.backend.common.dto.CustomPageResponseV1;
import com.blueviolet.backend.common.dto.OkResponse;
import com.blueviolet.backend.modules.product.controller.dto.GetInfiniteScrollProductListRequestV1;
import com.blueviolet.backend.modules.product.controller.dto.GetProductResponseV1;
import com.blueviolet.backend.modules.product.controller.dto.SearchProductListRequestV1;
import com.blueviolet.backend.modules.product.controller.dto.SearchProductResponseV1;
import com.blueviolet.backend.modules.product.service.ProductService;
import com.blueviolet.backend.modules.product.service.dto.GetProductListForInfiniteScrollParam;
import com.blueviolet.backend.modules.product.service.dto.GetProductResult;
import com.blueviolet.backend.modules.product.service.dto.SearchProductResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
public class ProductApiV1 {

    private final ProductService productService;

    @GetMapping(ProductApiPathsV1.V1_PRODUCTS)
    public OkResponse<CustomPageResponseV1<SearchProductResponseV1>> getAllByCond(
            @Valid SearchProductListRequestV1 searchProductListRequestV1,
            CustomPageRequestV1 customPageRequestV1
    ) {
        Page<SearchProductResult> searchProductResultPage = productService.getAllByCond(
                searchProductListRequestV1.toDto(),
                customPageRequestV1.getPageable()
        );
        CustomPageResponseV1<SearchProductResponseV1> pageResponse = CustomPageResponseV1.of(
                searchProductResultPage.map(SearchProductResult::toResponse),
                customPageRequestV1
        );

        return OkResponse.of(pageResponse);
    }

    @GetMapping(ProductApiPathsV1.V1_PRODUCTS_INFINITE_SCROLL)
    public OkResponse<CustomInfiniteScrollResponseV1<GetProductResponseV1>> getAllByCondForInfiniteScroll(
            @Valid GetInfiniteScrollProductListRequestV1 request
    ) {
        List<GetProductResponseV1> result = productService
                .getInfiniteScrollAllByCond(
                        new GetProductListForInfiniteScrollParam(
                                request.categoryId(),
                                request.lastProductId(),
                                request.size()
                        )
                )
                .stream()
                .map(GetProductResult::toResponse)
                .toList();

        return OkResponse.of(
                CustomInfiniteScrollResponseV1.of(
                        result,
                        request.size()
                )
        );
    }

    @GetMapping(ProductApiPathsV1.V1_PRODUCTS_BY_PRODUCT_ID)
    public OkResponse<SearchProductResponseV1> getOneByProductId(
            @PathVariable @Positive Long productId
    ) {
        SearchProductResult result = productService.getOneByProductId(productId);
        return OkResponse.of(result.toResponse());
    }
}
