package com.apextech.orderservice.service;

import com.apextech.orderservice.model.response.SessionData;
import com.apextech.orderservice.model.response.SessionResponse;

public interface SessionService {

    SessionResponse create(SessionData data);

    boolean verify(String sessionId);

    boolean consume(String sessionId);

    SessionData getSession(String sessionId);

}