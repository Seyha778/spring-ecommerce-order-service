package com.apextech.orderservice.controller;

import com.apextech.orderservice.common.ResponseMessage;
import com.apextech.orderservice.common.ResponseMessageBuilder;
import com.apextech.orderservice.model.request.OrderCreateRequest;
import com.apextech.orderservice.model.request.OrderListRequest;
import com.apextech.orderservice.model.request.OrderStatusUpdateRequest;
import com.apextech.orderservice.model.response.ListResponse;
import com.apextech.orderservice.model.response.OrderResponse;
import com.apextech.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseMessage<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return new ResponseMessageBuilder<OrderResponse>()
                .success()
                .data(response)
                .build();
    }

    @PostMapping("/updateOrderStatus")
    public ResponseMessage<OrderResponse> updateOrderStatus(@Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse response = orderService.updateOrderStatus(request);
        return new ResponseMessageBuilder<OrderResponse>().success().data(response).build();
    }

    @PostMapping("/cancelOrder")
    public ResponseMessage<OrderResponse> cancelOrder(@Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse response = orderService.cancelOrder(request);
        return new ResponseMessageBuilder<OrderResponse>().success().data(response).build();
    }

    @PostMapping("/getOrder")
    public ResponseMessage<OrderResponse> getOrderByNumber(@Valid @RequestBody OrderListRequest request) {
        OrderResponse response = orderService.getOrderByNumber(request);
        return new ResponseMessageBuilder<OrderResponse>().success().data(response).build();
    }

    @PostMapping("/listCustomerOrder")
    public ResponseMessage<ListResponse<OrderResponse>> listCustomerOrder(@Valid @RequestBody OrderListRequest request) {
        ListResponse<OrderResponse> response = orderService.getCustomerOrders(request);
        return new ResponseMessageBuilder<ListResponse<OrderResponse>>().success().data(response).build();
    }

    @PostMapping("/listOrder")
    public ResponseMessage<ListResponse<OrderResponse>> listOrder(@Valid @RequestBody OrderListRequest request) {
        ListResponse<OrderResponse> response = orderService.listOrder(request);
        return new ResponseMessageBuilder<ListResponse<OrderResponse>>().success().data(response).build();
    }

}
