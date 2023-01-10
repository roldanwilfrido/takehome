package com.example.takehome.utils;

import java.util.List;
import java.util.stream.Collectors;

public class CountryQueryBuilder {

    private CountryQueryBuilder() {
    }

    /**
     * Builds a query to get all the countries and continents that matches with the list of country codes
     * @param countryCodes List of country codes
     * @return Query string
     */
    public static String countriesAndContinentsByCountryCodesQuery(
            List<String> countryCodes) {

        String query = """
                    query {
                        countries(filter: { code: { in: [%s] } }) {
                            code
                            name
                            continent {
                                code
                                name
                            }
                        }
                    }
                """;

        return String.format(query, listToStringConverter(countryCodes));
    }


    /**
     * Builds a query to get all the country codes that belong to the given continent,
     * excluding the list of country codes
     * @param continent Continent codes
     * @param countryCodes List of country codes
     * @return Query string
     */
    public static String countriesByContinentCodeAndNotInCountryCodesQuery(
            String continent, List<String> countryCodes) {

        String query = """
                    query {
                        countries(filter: { continent: { eq: "%s" }, code: { nin: [%s] } }) {
                            code
                        }
                    }
                """;

        return String.format(query,
                continent, listToStringConverter(countryCodes));
    }

    /**
     * Converts a list of strings to String separated by comma
     * @param list List of strings
     * @return String values separated by comma
     */
    private static String listToStringConverter(List<String> list) {

        return list.stream()
                .map(string -> "\"" + string + "\"")
                .collect(Collectors.joining(","));
    }

}
