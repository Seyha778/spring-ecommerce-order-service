package com.apextech.orderservice.repository;

import com.apextech.orderservice.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Page<OrderEntity> findAllByCustomerIdOrderByCreatedOnDesc(
            String customerId,
            Pageable pageable
    );

    Page<OrderEntity> findAllByOrderByCreatedOnDesc(
            Pageable pageable
    );

    boolean existsByOrderNumber(String orderNumber);
}
