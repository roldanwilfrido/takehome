package com.example.takehome.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "client.countries-trevor-blades")
@Configuration
@Data
public class CountriesClientConfiguration {

    private String name;
    private String url;
    private String retrievePath;
}
