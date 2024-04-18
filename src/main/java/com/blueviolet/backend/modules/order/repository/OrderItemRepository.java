package com.blueviolet.backend.modules.order.repository;

import com.blueviolet.backend.modules.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
