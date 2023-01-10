package com.example.takehome.services;

import com.example.takehome.commons.FileLoadingUtils;
import com.example.takehome.configurations.CountriesClientConfiguration;
import com.example.takehome.domain.client.Country;
import com.example.takehome.exceptions.TakehomeClientException;
import com.example.takehome.exceptions.TakehomeServerException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.codec.CodecException;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountriesTrevorBladesClientServiceTests extends FileLoadingUtils {

    @Mock
    GraphQlClient.RequestSpec requestSpec;
    @Mock
    GraphQlClient.RetrieveSpec retrieveSpec;

    @Mock
    HttpGraphQlClient httpGraphQlClient;

    static CountriesClientConfiguration config;

    @BeforeAll
    static void init() {

        config = new CountriesClientConfiguration();
        config.setName("Countries trevor blades client");
        config.setUrl("mock-url");
        config.setRetrievePath("countries");
    }

    @Test
    void executeQueryReturnsListOfCountries() throws Exception {

        String countryClientResponse = getJsonArrayMock(COUNTRIES_CLIENT_RESPONSE).toString();
        List<Country> countries = objectMapper.readValue(countryClientResponse, new TypeReference<>() {
        });

        when(retrieveSpec.toEntityList(Country.class)).thenReturn(Mono.just(countries));
        when(requestSpec.retrieve(anyString())).thenReturn(retrieveSpec);
        when(httpGraphQlClient.document(anyString())).thenReturn(requestSpec);
        CountriesTrevorBladesClientService targetService =
                new CountriesTrevorBladesClientService(httpGraphQlClient, config);
        assertEquals(5, targetService.executeQuery("MOCK_QUERY").size());
    }

    @Test
    void executeQueryThrowsATakehomeClientExceptionWhenResponseDoesNotMatch() {

        when(retrieveSpec.toEntityList(Country.class)).thenThrow(CodecException.class);
        when(requestSpec.retrieve(anyString())).thenReturn(retrieveSpec);
        when(httpGraphQlClient.document(anyString())).thenReturn(requestSpec);
        CountriesTrevorBladesClientService targetService =
                new CountriesTrevorBladesClientService(httpGraphQlClient, config);
        TakehomeClientException exception = assertThrows(
                TakehomeClientException.class, () -> targetService.executeQuery("MOCK_QUERY"));


        assertEquals("Countries trevor blades client (Country.class)", exception.getMessage());
    }

    @Test
    void executeQueryThrowsATakehomeServerExceptionWhenCountriesAPIIsNotAvailable() {

        when(httpGraphQlClient.document(anyString())).thenThrow(new WebClientException("Any other exception") {
        });
        CountriesTrevorBladesClientService targetService =
                new CountriesTrevorBladesClientService(httpGraphQlClient, config);
        TakehomeServerException exception = assertThrows(
                TakehomeServerException.class, () -> targetService.executeQuery("MOCK_QUERY"));

        assertEquals(
                "Countries trevor blades client due to <<||Any other exception||>>",
                exception.getMessage());
    }

}
