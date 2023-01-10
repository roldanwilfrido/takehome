package com.example.takehome.services;

import com.example.takehome.domain.client.Country;
import com.example.takehome.domain.response.CountryElement;
import graphql.com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static com.example.takehome.utils.CountryQueryBuilder.countriesAndContinentsByCountryCodesQuery;
import static com.example.takehome.utils.CountryQueryBuilder.countriesByContinentCodeAndNotInCountryCodesQuery;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountriesTrevorBladesClientService countriesClientService;

    /**
     * Filters Countries by country codes list
     * @param countryCodes Country codes list
     * @return List of {@link CountryElement}
     */
    public List<CountryElement> filterCountriesByCountryCodes(List<String> countryCodes) {

        List<Country> countries = countriesClientService
                .executeQuery(countriesAndContinentsByCountryCodesQuery(countryCodes));

        return transformCountriesToContinentMap(countries).entrySet()
                .parallelStream()
                .map(continent -> {
                    List<String> otherCountryCodes = getMissingCountryCodesByContinentCode(
                            continent.getKey(), continent.getValue().getCountryCodes());
                    continent.getValue().setOtherCountryCodes(otherCountryCodes);
                    return continent.getValue();
                }).toList();
    }

    /**
     * Transforms List<{@link Country}> to HashMap< String, {@link CountryElement}>
     * @param countries List< {@link CountryElement}> object
     * @return HashMap of {@link CountryElement} (value) by Continent codes string (as a key)
     */
    private HashMap<String, CountryElement> transformCountriesToContinentMap(List<Country> countries) {
        HashMap<String, CountryElement> continentMap = new HashMap<>();

        countries.forEach(country -> {
            String continentCode = country.getContinent().getCode();
            if (continentMap.containsKey(continentCode)) {
                addCountryCodeToCountryElement(continentMap.get(continentCode), country.getCode());
            } else {
                putContinentCodeInTheMap(continentMap, country);
            }
        });
        return continentMap;
    }

    /**
     * Adds new country code to an existing {@link CountryElement} object is not exist
     * @param countryElement {@link CountryElement} object
     * @param countryCode country code
     */
    private void addCountryCodeToCountryElement(
            CountryElement countryElement, String countryCode) {
        if (!countryElement.getCountryCodes().contains(countryCode)) {
            countryElement.getCountryCodes().add(countryCode);
        }
    }

    /**
     * Puts new element into the HashMap< key, value > as follows:<br/>
     * - key: continent code string<br/>
     * - value: {@link CountryElement} object
     * @param continentMap HashMap< String, {@link CountryElement}> object
     * @param country {@link Country} object
     */
    private void putContinentCodeInTheMap(
            HashMap<String, CountryElement> continentMap, Country country) {

        CountryElement countryElement = CountryElement.builder()
                .continentName(country.getContinent().getName())
                .countryCodes(Lists.newArrayList(country.getCode()))
                .build();
        continentMap.put(country.getContinent().getCode(), countryElement);
    }

    /**
     * Gets a list of missing country codes by continent code
     * @param continentCode Continent code string
     * @param countryCodes List of country codes
     * @return List of country codes
     */
    private List<String> getMissingCountryCodesByContinentCode(
            String continentCode, List<String> countryCodes) {

        String query = countriesByContinentCodeAndNotInCountryCodesQuery(
                continentCode,
                countryCodes);
        return countriesClientService.executeQuery(query).stream().map(Country::getCode).toList();
    }
}
