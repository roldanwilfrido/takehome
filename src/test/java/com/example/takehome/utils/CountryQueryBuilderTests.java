package com.example.takehome.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CountryQueryBuilderTests {

    private static final String MOCK_CONTINENT = "CONTINENT";
    private static final String MOCK_COUNTRY_CODE = "COUNTRY_CODE";

    @Test
    void callCountriesAndContinentsByCountryCodesQueryReturnsQueryString() {

        List<String> countryCodes = Collections.singletonList(MOCK_COUNTRY_CODE);
        assertTrue(CountryQueryBuilder
                .countriesAndContinentsByCountryCodesQuery(
                        countryCodes)
                .contains(MOCK_COUNTRY_CODE));
    }

    @Test
    void callCountriesByContinentCodeAndNotInCountryCodesQueryReturnsQueryString() {

        List<String> countryCodes = Collections.singletonList(MOCK_COUNTRY_CODE);
        assertTrue(CountryQueryBuilder
                .countriesByContinentCodeAndNotInCountryCodesQuery(
                        MOCK_CONTINENT, countryCodes)
                .contains(MOCK_COUNTRY_CODE));
    }

}
