package com.example.takehome.services;

import com.example.takehome.commons.FileLoadingUtils;
import com.example.takehome.domain.client.Country;
import com.example.takehome.domain.response.CountryElement;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTests extends FileLoadingUtils {

    @Mock
    CountriesTrevorBladesClientService countriesClientService;

    @Test
    void filterCountriesByCountryCodesReturnsListOfCountryElement() throws Exception {

        List<String> countryCodes = Arrays.asList("PA", "CA", "US", "CL", "CO");

        String countryClientResponse = getJsonArrayMock(COUNTRIES_CLIENT_RESPONSE).toString();
        List<Country> countries = objectMapper.readValue(countryClientResponse, new TypeReference<>() {
        });
        //This step is just to duplicate values to test the 'avoid duplicated results'
        countries.addAll(countries);

        when(countriesClientService.executeQuery(anyString())).thenReturn(countries);
        CountryService service =
                new CountryService(countriesClientService);
        List<CountryElement> countryElements = service.filterCountriesByCountryCodes(countryCodes);
        assertEquals(2, countryElements.size());
    }

}
