package com.apextech.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotBlank(message = "Customer Id code is required")
    @JsonProperty("customer_id")
    @NotBlank
    String customerId;

    @Builder.Default
    @JsonProperty("customer_name")
    String customerName = "";

    @Builder.Default
    @JsonProperty("customer_email")
    String customerEmail = "";

    @JsonProperty("currency")
    @NotBlank(message = "currency is required")
    String currency;

    @DecimalMin("0.00")
    @JsonProperty("discount_amount")
    BigDecimal discountAmount;

    @DecimalMin("0.00")
    @JsonProperty("shipping_fee")
    BigDecimal shippingFee;

    @DecimalMin("0.00")
    @JsonProperty("tax_amount")
    BigDecimal taxAmount;

    @DecimalMin("0.00")
    @JsonProperty("subtotal")
    BigDecimal subtotal;

    @Builder.Default
    @JsonProperty("notes")
    String notes = "";

}
