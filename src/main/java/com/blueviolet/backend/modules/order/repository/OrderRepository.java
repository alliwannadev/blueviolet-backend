package com.blueviolet.backend.modules.order.repository;

import com.blueviolet.backend.modules.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
