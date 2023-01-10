package com.example.takehome.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rate-limit.request-per-sec")
@Configuration
@Data
public class TakehomeRateLimitConfiguration {

    private int numberOfSecureRequest;
    private int numberOfUnsecureRequest;
}
