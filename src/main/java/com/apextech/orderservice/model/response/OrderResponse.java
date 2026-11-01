package com.apextech.orderservice.model.response;

import com.apextech.orderservice.constant.OrderStatusEnum;
import com.apextech.orderservice.constant.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    Long id;
    String orderNumber;
    String customerId;
    String customerName;
    String customerEmail;
    BigDecimal subtotal;
    BigDecimal discountAmount;
    BigDecimal shippingFee;
    BigDecimal taxAmount;
    BigDecimal totalAmount;
    String currency;
    OrderStatusEnum orderStatus;
    PaymentStatusEnum paymentStatus;
//    List<OrderItemResponse> items;
//    List<OrderAddressResponse> addresses;
    OffsetDateTime createdOn;
}
