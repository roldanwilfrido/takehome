package com.example.takehome.web;

import com.example.takehome.configurations.TakehomeRateLimitConfiguration;
import com.example.takehome.configurations.security.LoginConfiguration;
import com.example.takehome.domain.response.CountryElement;
import com.example.takehome.services.CountryService;
import com.example.takehome.services.RateLimitPublicService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(CountryPublicController.class)
class CountryPublicControllerTests {

    @MockBean
    CountryService countryService;
    @MockBean
    RateLimitPublicService rateLimitService;
    @MockBean
    LoginConfiguration loginConfig;

    @Test
    void callFilterCountriesNormalWorksOK() {

        String continentName = "North America";
        CountryElement countryElement = CountryElement.builder().continentName(continentName).build();
        doNothing().when(rateLimitService).checkRequestLimit();
        when(countryService.filterCountriesByCountryCodes(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(countryElement));
        CountryPublicController controller = new CountryPublicController(rateLimitService, countryService);
        assertEquals(continentName,
                controller.filterCountries(Set.of("CA", "US"))
                        .getBody().getElements().get(0).getContinentName());
    }

    @Test
    void callFilterCountriesFailsWhenIsCalledMoreThanTheRateLimit() {

        TakehomeRateLimitConfiguration config = new TakehomeRateLimitConfiguration();
        config.setNumberOfUnsecureRequest(5);
        config.setNumberOfSecureRequest(20);
        RateLimitPublicService rateLimitPublicService = new RateLimitPublicService(config);

        String continentName = "North America";
        CountryElement countryElement = CountryElement.builder().continentName(continentName).build();
        doNothing().when(rateLimitService).checkRequestLimit();
        when(countryService.filterCountriesByCountryCodes(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(countryElement));
        CountryPublicController controller = new CountryPublicController(rateLimitPublicService, countryService);

        boolean hasException = IntStream.range(0, 6)
                .parallel()
                .mapToObj(value -> {
                    try {
                        controller.filterCountries(Set.of("CA", "US"));
                        return "";
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::isNull).count() == 1;

        assertTrue(hasException);
    }

}
