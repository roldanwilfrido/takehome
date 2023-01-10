package com.example.takehome.domain.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TakehomeError {

    private Instant time;
    private Integer status;
    private String message;
}
