package com.example.takehome.web;

import com.example.takehome.domain.response.CountryResponse;
import com.example.takehome.services.CountryService;
import com.example.takehome.services.RateLimitAbstract;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@Setter
public abstract class CountryAbstract {

    private RateLimitAbstract rateLimitService;

    /**
     * Builds a country response
     * @param countryService {@link CountryService} object
     * @param countryCodes List of country codes
     * @return A ResponseEntity ({@link CountryResponse}) object
     */
    public ResponseEntity<CountryResponse> getCountryResponse(CountryService countryService,
            Set<String> countryCodes) {

        rateLimitService.checkRequestLimit();
        CountryResponse countryResponse = CountryResponse
                .builder()
                .elements(countryService.filterCountriesByCountryCodes(countryCodes.stream().toList()))
                .build();
        return ResponseEntity.ok().body(countryResponse);
    }
}
