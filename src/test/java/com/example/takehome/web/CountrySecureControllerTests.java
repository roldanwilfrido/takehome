package com.example.takehome.web;

import com.example.takehome.configurations.security.LoginConfiguration;
import com.example.takehome.domain.response.CountryElement;
import com.example.takehome.exceptions.TakehomeClientException;
import com.example.takehome.exceptions.TakehomeRateLimitException;
import com.example.takehome.exceptions.TakehomeServerException;
import com.example.takehome.services.CountryService;
import com.example.takehome.services.RateLimitSecureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountrySecureController.class)
class CountrySecureControllerTests {

    @MockBean
    CountryService countryService;
    @MockBean
    RateLimitSecureService rateLimitService;
    @MockBean
    LoginConfiguration loginConfig;

    @Autowired
    WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    private static final String ENDPOINT = "/secure/countries/v1/filter?";

    @BeforeEach
    public void init() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void whenOKThenReturns200() throws Exception {

        String continentName = "North America";
        CountryElement countryElement = CountryElement.builder().continentName(continentName).build();
        doNothing().when(rateLimitService).checkRequestLimit();
        when(countryService.filterCountriesByCountryCodes(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(countryElement));
        String body = mockMvc.perform(
                        get(ENDPOINT + "countryCodes=CA, US"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertTrue(body.contains(continentName));
    }

    @Test
    void whenNoParameterThenReturns400() throws Exception {

        mockMvc.perform(
                        get(ENDPOINT))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenNoValuesInTheParameterThenReturns400() throws Exception {

        mockMvc.perform(
                        get(ENDPOINT + "countryCodes="))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenTooManyRequestReturns429() throws Exception {

        doThrow(TakehomeRateLimitException.class).when(rateLimitService).checkRequestLimit();
        mockMvc.perform(
                        get(ENDPOINT + "countryCodes=CA, US"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    void whenCodecExceptionThenReturns422() throws Exception {

        doNothing().when(rateLimitService).checkRequestLimit();
        when(countryService.filterCountriesByCountryCodes(ArgumentMatchers.anyList()))
                .thenThrow(TakehomeClientException.class);
        mockMvc.perform(
                        get(ENDPOINT + "countryCodes=CA, US"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenInternalServerErrorThenReturns500() throws Exception {

        doNothing().when(rateLimitService).checkRequestLimit();
        when(countryService.filterCountriesByCountryCodes(ArgumentMatchers.anyList()))
                .thenThrow(TakehomeServerException.class);
        mockMvc.perform(
                        get(ENDPOINT + "countryCodes=CA, US"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void callFilterCountriesNormalWorksOK() {

        String continentName = "North America";
        CountryElement countryElement = CountryElement.builder().continentName(continentName).build();
        doNothing().when(rateLimitService).checkRequestLimit();
        when(countryService.filterCountriesByCountryCodes(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(countryElement));
        CountrySecureController controller = new CountrySecureController(rateLimitService, countryService);
        assertEquals(continentName,
                controller.filterCountries(Set.of("CA", "US"))
                        .getBody().getElements().get(0).getContinentName());
    }

}
