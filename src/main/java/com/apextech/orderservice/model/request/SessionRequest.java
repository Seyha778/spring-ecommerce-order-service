package com.apextech.orderservice.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionRequest {

    @JsonProperty("session_id")
    @NotBlank
    private String sessionId;

}
