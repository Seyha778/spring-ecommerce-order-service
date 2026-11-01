package com.apextech.orderservice.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ProductStatusEnum {
    PA("PA", "Product Active"),
    PD("PD", "Product Delete"),
    PI("PI", "Product Progress");

    final String value;
    final String description;

    ProductStatusEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String getDescriptionByValue(String value) {
        return Arrays.stream(values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .map(ProductStatusEnum::getDescription)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid transaction type: " + value
                ));
    }

    @Override
    public String toString() {
        return value;
    }
}
