package com.blueviolet.backend.modules.order.repository;

import com.blueviolet.backend.modules.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            """
            select  o
            from    Order o join fetch o.orderItems oi
            where   o.orderId = :orderId
            """
    )
    Optional<Order> findOneByOrderIdWithRelationship(Long orderId);
}
