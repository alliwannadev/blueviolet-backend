package com.blueviolet.backend.modules.order.service;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.order.domain.Order;
import com.blueviolet.backend.modules.order.domain.OrderItem;
import com.blueviolet.backend.modules.order.repository.OrderItemRepository;
import com.blueviolet.backend.modules.order.repository.OrderRepository;
import com.blueviolet.backend.modules.order.service.dto.CreateOrderParam;
import com.blueviolet.backend.modules.order.service.dto.CreateOrderResult;
import com.blueviolet.backend.modules.stock.domain.Stock;
import com.blueviolet.backend.modules.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StockService stockService;

    public CreateOrderResult create(CreateOrderParam createOrderParam) {
        Order savedOrder = orderRepository.save(createOrderParam.toOrder());
        List<OrderItem> savedOrderItemList = orderItemRepository.saveAll(createOrderParam.toOrderItemList(savedOrder));

        // TODO: 결제 기능이 추가되면, 결제 이후에 재고 감소 처리하기
        savedOrderItemList
                .forEach(
                        orderItem -> {
                            Stock foundStock = stockService
                                    .getOneByCombinationId(orderItem.getProductOptionCombinationId())
                                    .orElseThrow(() -> new BusinessException(ErrorCode.STOCK_NOT_FOUND));
                            stockService.decreaseQuantityByStockId(
                                    foundStock.getStockId(),
                                    orderItem.getQuantity()
                            );
                        }
                );

        return CreateOrderResult.fromEntity(savedOrder, savedOrderItemList);
    }
}
