package com.example.takehome.services;

import com.example.takehome.configurations.CountriesClientConfiguration;
import com.example.takehome.domain.client.Country;
import com.example.takehome.exceptions.TakehomeClientException;
import com.example.takehome.exceptions.TakehomeServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.CodecException;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountriesTrevorBladesClientService {

    private final HttpGraphQlClient httpGraphQlClient;
    private final CountriesClientConfiguration configuration;

    @Autowired
    public CountriesTrevorBladesClientService(
            HttpGraphQlClient httpGraphQlClient, CountriesClientConfiguration configuration) {

        this.httpGraphQlClient = httpGraphQlClient;
        this.configuration = configuration;
    }

    /**
     * Executes a query by calling the Countries trevor blades graphql endpoint
     * @param query Query string
     * @return List of countries
     */
    public List<Country> executeQuery(String query) {

        try {
            return httpGraphQlClient.document(query)
                    .retrieve(configuration.getRetrievePath())
                    .toEntityList(Country.class)
                    .block();
        } catch (CodecException e) {
            throw new TakehomeClientException(
                    configuration.getName() + " (" + Country.class.getSimpleName() + ".class)");
        } catch (Exception ex) {
                throw new TakehomeServerException(
                        configuration.getName() + " due to <<||" + ex.getMessage() + "||>>");
        }
    }

}
