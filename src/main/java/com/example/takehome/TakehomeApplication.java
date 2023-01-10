package com.example.takehome;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.client.HttpGraphQlClient;

@Slf4j
@SpringBootApplication
public class TakehomeApplication {

	public static void main(String[] args) {
		log.debug("This is a debug message");
		SpringApplication.run(TakehomeApplication.class, args);
	}

	@Bean
	HttpGraphQlClient httpGraphQlClient(@Value("${client.countries-trevor-blades.url}") String countriesUrl){
		return HttpGraphQlClient.builder().url(countriesUrl).build();
	}
}
