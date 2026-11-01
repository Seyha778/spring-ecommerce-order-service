package com.apextech.orderservice.repository;

import com.apextech.orderservice.entity.OrderStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistoryEntity, Long> {
    List<OrderStatusHistoryEntity> findAllByOrderIdOrderByCreatedOnDesc(
            Long orderId
    );
}
