package com.apextech.orderservice.model.request;

import com.apextech.orderservice.constant.OrderStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {

    @NotBlank(message = "Order Number is required")
    @JsonProperty("order_number")
    String orderNumber;

    @JsonProperty("status")
    @NotNull(message = "Status is required")
    private OrderStatusEnum status;

    @NotBlank(message = "Reason is required")
    @JsonProperty("reason")
    String reason;
}
