package com.example.takehome.services;

import com.example.takehome.configurations.TakehomeRateLimitConfiguration;
import com.example.takehome.exceptions.TakehomeRateLimitException;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitPublicServiceTests {

    @Mock
    Bucket bucket;

    static TakehomeRateLimitConfiguration config;

    @BeforeAll
    static void init() {

        config = new TakehomeRateLimitConfiguration();
        config.setNumberOfUnsecureRequest(5);
        config.setNumberOfSecureRequest(20);
    }

    @Test
    void whenRequestsReachTheLimitThrowsAnTakehomeRateLimitException() {

        when(bucket.tryConsume(1)).thenReturn(false);

        RateLimitPublicService rateLimitPublicService =
                new RateLimitPublicService(config);
        rateLimitPublicService.setBucket(bucket);
        assertThrows(
                TakehomeRateLimitException.class, rateLimitPublicService::checkRequestLimit);
    }
}
