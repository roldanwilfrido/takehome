package com.example.takehome.web;

import com.example.takehome.domain.response.CountryResponse;
import com.example.takehome.domain.response.TakehomeError;
import com.example.takehome.services.CountryService;
import com.example.takehome.services.RateLimitSecureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Tag(name = "Countries (Secure)", description = "Secure endpoints for managing countries")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/secure/countries/v1")
public class CountrySecureController extends CountryAbstract {

    private final RateLimitSecureService rateLimitService;
    private final CountryService countryService;

    @Operation(summary = "Filter countries by their country codes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CountryResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameter.", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TakehomeError.class)) }),
            @ApiResponse(responseCode = "422",
                    description = "Codec exception due to changes on Countries Graphql API.", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TakehomeError.class)) }),
            @ApiResponse(responseCode = "429", description = "Many request.", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TakehomeError.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TakehomeError.class)) })
    })
    @GetMapping("/filter")
    public ResponseEntity<CountryResponse> filterCountries(
            @RequestParam @Valid @Size(min = 1) Set<String> countryCodes) {

        setRateLimitService(rateLimitService);
        return getCountryResponse(countryService, countryCodes);
    }
}
