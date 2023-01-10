package com.example.takehome.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryElement {

    @JsonProperty("countries")
    private List<String> countryCodes;

    @JsonProperty("name")
    private String continentName;

    @JsonProperty("otherCountries")
    private List<String> otherCountryCodes;
}
