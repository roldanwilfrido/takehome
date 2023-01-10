package com.example.takehome.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryResponse {

    @JsonProperty("continent")
    private List<CountryElement> elements;
}
