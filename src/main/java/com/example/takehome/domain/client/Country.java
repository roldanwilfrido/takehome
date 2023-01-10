package com.example.takehome.domain.client;

import lombok.Data;

@Data
public class Country {
    private String code;
    private String name;
    private Continent continent;

    @Data
    public static class Continent {
        private String code;
        private String name;
    }
}
