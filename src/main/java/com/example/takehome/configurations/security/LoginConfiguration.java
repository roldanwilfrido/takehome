package com.example.takehome.configurations.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "login")
@Configuration
@Data
public class LoginConfiguration {

    private String url;
    private String secret;
    private Long validitySeconds;
}
