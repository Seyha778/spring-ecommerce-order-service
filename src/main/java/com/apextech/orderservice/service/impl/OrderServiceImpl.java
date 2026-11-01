package com.apextech.orderservice.service.impl;

import com.apextech.orderservice.constant.OrderStatusEnum;
import com.apextech.orderservice.entity.OrderEntity;
import com.apextech.orderservice.entity.OrderStatusHistoryEntity;
import com.apextech.orderservice.exception.ResourceNotFoundException;
import com.apextech.orderservice.model.request.OrderCreateRequest;
import com.apextech.orderservice.model.request.OrderListRequest;
import com.apextech.orderservice.model.request.OrderStatusUpdateRequest;
import com.apextech.orderservice.model.response.ListResponse;
import com.apextech.orderservice.model.response.OrderResponse;
import com.apextech.orderservice.repository.OrderRepository;
import com.apextech.orderservice.repository.OrderStatusHistoryRepository;
import com.apextech.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        OrderEntity order = new OrderEntity();

        order.setOrderNumber(generateOrderNumber());
        order.setCustomerId(request.getCustomerId());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setCurrency(request.getCurrency());

//TODO
//        BigDecimal subtotal = request.items().stream()
//                .map(item -> item.unitPrice()
//                        .multiply(BigDecimal.valueOf(item.quantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal subtotal = request.getSubtotal();

        BigDecimal discount = defaultAmount(request.getDiscountAmount());
        BigDecimal shipping = defaultAmount(request.getShippingFee());
        BigDecimal tax = defaultAmount(request.getTaxAmount());

        if (discount.compareTo(subtotal) > 0) {
            throw new ResourceNotFoundException(
                    "Discount cannot exceed subtotal"
            );
        }

        BigDecimal total = subtotal
                .subtract(discount)
                .add(shipping)
                .add(tax);

        order.setSubtotal(subtotal);
        order.setDiscountAmount(discount);
        order.setShippingFee(shipping);
        order.setTaxAmount(tax);
        order.setTotalAmount(total);
        order.setNotes(request.getNotes());

        OrderEntity savedOrder = orderRepository.save(order);

        // Initial status history
        saveStatusHistory(savedOrder.getOrderNumber(), null, OrderStatusEnum.PENDING, "Order created");

        return buildOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderByNumber(OrderListRequest request) {
        String orderNumber = request.getOrderNumber();
        if(!StringUtils.hasText(orderNumber)){
            log.info("Order Number is Emtpy.");
            return null;
        }
        Optional<OrderEntity> orders = orderRepository.findByOrderNumber(request.getOrderNumber());

        OrderEntity order = orders.orElseThrow(() ->
                new ResourceNotFoundException("Order not found")
        );

        return buildOrderResponse(order);
    }

    @Override
    public ListResponse<OrderResponse> getCustomerOrders(OrderListRequest request) {
        String customerNumber = request.getCustomerNumber();
        if(!StringUtils.hasText(customerNumber)){
            log.info("Customer Number is Emtpy.");
            return null;
        }

        int page = request.getPage() == null ? 0 : request.getPage();
        int size = request.getSize() == null ? 10 : request.getSize();

        if (page < 0) {
            throw new ResourceNotFoundException("Page must not be negative");
        }

        if (size < 1 || size > 100) {
            throw new ResourceNotFoundException("Page size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(page, size);


        Page<OrderEntity> orders = orderRepository.findAllByCustomerIdOrderByCreatedOnDesc(customerNumber, pageable);

        log.info("Total order data size:{}", orders.getSize());

        List<OrderResponse> content = orders.getContent()
                .stream()
                .map(this::buildOrderResponse)
                .toList();

        return ListResponse.<OrderResponse>builder()
                .content(content)
                .totalElements(orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .build();
    }

    @Override
    public OrderResponse updateOrderStatus(OrderStatusUpdateRequest request) {
        Optional<OrderEntity> order = orderRepository.findByOrderNumber(request.getOrderNumber());

        OrderEntity entity = order.orElseThrow(() ->
                new ResourceNotFoundException("Order not found")
        );

        OrderStatusEnum oldStatus = entity.getOrderStatus();
        OrderStatusEnum newStatus = request.getStatus();

        if (oldStatus == newStatus) {
            return buildOrderResponse(entity);
        }

        validateStatusTransition(oldStatus, newStatus);

        entity.setOrderStatus(newStatus);

        OrderEntity savedOrder = orderRepository.save(entity);

        saveStatusHistory(
                savedOrder.getOrderNumber(),
                oldStatus,
                newStatus,
                request.getReason()
        );

        return buildOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse cancelOrder(OrderStatusUpdateRequest request) {

        Optional<OrderEntity> order = orderRepository.findByOrderNumber(request.getOrderNumber());

        OrderEntity entity = order.orElseThrow(() ->
                new ResourceNotFoundException("Order not found")
        );

        validateStatusTransition(entity.getOrderStatus(), OrderStatusEnum.CANCELLED);

        OrderStatusEnum oldStatus = entity.getOrderStatus();

        entity.setOrderStatus(OrderStatusEnum.CANCELLED);

        OrderEntity savedOrder = orderRepository.save(entity);

        saveStatusHistory(
                savedOrder.getOrderNumber(),
                oldStatus,
                OrderStatusEnum.CANCELLED,
                request.getReason()
        );

        return buildOrderResponse(savedOrder);
    }

    @Override
    public ListResponse<OrderResponse> listOrder(OrderListRequest request) {
        int page = request.getPage() == null ? 0 : request.getPage();
        int size = request.getSize() == null ? 10 : request.getSize();

        if (page < 0) {
            throw new ResourceNotFoundException("Page must not be negative");
        }

        if (size < 1 || size > 100) {
            throw new ResourceNotFoundException("Page size must be between 1 and 100");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderEntity> list = orderRepository.findAllByOrderByCreatedOnDesc(pageable);

        log.info("Total data size:{}", list.getSize());

        List<OrderResponse> content = list.getContent()
                .stream()
                .map(this::buildOrderResponse)
                .toList();

        return ListResponse.<OrderResponse>builder()
                .content(content)
                .totalElements(list.getTotalElements())
                .totalPages(list.getTotalPages())
                .build();
    }

    private String generateOrderNumber() {
        String orderNumber;
        do {
            orderNumber = "ORD-" + UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 12)
                    .toUpperCase(Locale.ROOT);

        } while (orderRepository.existsByOrderNumber(orderNumber));

        return orderNumber;
    }

    private BigDecimal defaultAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    private void saveStatusHistory(String orderNumber, OrderStatusEnum oldStatus, OrderStatusEnum newStatus, String reason) {
        OrderStatusHistoryEntity history = new OrderStatusHistoryEntity();

        history.setOrderId(orderNumber);
        history.setOldStatus(oldStatus == null ? null : oldStatus.name());
        history.setNewStatus(newStatus.name());
        history.setChangeReason(reason);

        orderStatusHistoryRepository.save(history);
    }

    private OrderResponse buildOrderResponse(OrderEntity orderEntity){
        return OrderResponse.builder()
                .id(orderEntity.getId())
                .orderStatus(orderEntity.getOrderStatus())
                .subtotal(orderEntity.getSubtotal())
                .totalAmount(orderEntity.getTotalAmount())
                .build();
    }

    private void validateStatusTransition(
            OrderStatusEnum current,
            OrderStatusEnum next
    ) {

        Map<OrderStatusEnum, Set<OrderStatusEnum>> allowedTransitions =
                Map.of(
                        OrderStatusEnum.PENDING,
                        Set.of(OrderStatusEnum.CONFIRMED, OrderStatusEnum.CANCELLED),

                        OrderStatusEnum.CONFIRMED,
                        Set.of(OrderStatusEnum.PROCESSING, OrderStatusEnum.CANCELLED),

                        OrderStatusEnum.PROCESSING,
                        Set.of(OrderStatusEnum.SHIPPED, OrderStatusEnum.CANCELLED),

                        OrderStatusEnum.SHIPPED,
                        Set.of(OrderStatusEnum.DELIVERED),

                        OrderStatusEnum.DELIVERED,
                        Set.of(OrderStatusEnum.REFUNDED)
                );

        if (!allowedTransitions.getOrDefault(
                current, Collections.emptySet()
        ).contains(next)) {
            throw new ResourceNotFoundException(
                    "Invalid order status transition: "
                            + current + " -> " + next
            );
        }
    }
}
