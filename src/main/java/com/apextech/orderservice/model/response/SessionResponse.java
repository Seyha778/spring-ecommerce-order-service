package com.apextech.orderservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SessionResponse {

    private String sessionId;

    private long expiresIn;
}