package com.blueviolet.backend.modules.order.helper;

import com.blueviolet.backend.common.error.BusinessException;
import com.blueviolet.backend.common.error.ErrorCode;
import com.blueviolet.backend.modules.order.domain.Order;
import com.blueviolet.backend.modules.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class OrderTestHelper {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Order getOneByOrderIdWithRelationship(Long orderId) {
        return orderRepository
                .findOneByOrderIdWithRelationship(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }
}
