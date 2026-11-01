package com.apextech.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class OrderListRequest {

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("customer_number")
    private String customerNumber;

    @JsonProperty("order_number")
    private String orderNumber;

    @JsonProperty("order_status")
    private String orderStatus;

    @Builder.Default
    @JsonProperty("page")
    private Integer page = 0;

    @Builder.Default
    @JsonProperty("size")
    private Integer size = 10;

}
