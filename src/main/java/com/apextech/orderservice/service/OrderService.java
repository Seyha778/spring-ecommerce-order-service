package com.apextech.orderservice.service;

import com.apextech.orderservice.model.request.OrderCreateRequest;
import com.apextech.orderservice.model.request.OrderListRequest;
import com.apextech.orderservice.model.request.OrderStatusUpdateRequest;
import com.apextech.orderservice.model.response.ListResponse;
import com.apextech.orderservice.model.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest request);

    OrderResponse getOrderByNumber(OrderListRequest request);

    ListResponse<OrderResponse> getCustomerOrders(OrderListRequest request);

    OrderResponse updateOrderStatus(OrderStatusUpdateRequest request);

    OrderResponse cancelOrder(OrderStatusUpdateRequest request);

    ListResponse<OrderResponse> listOrder(OrderListRequest request);
}
