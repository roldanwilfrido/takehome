package com.example.takehome.services;

import com.example.takehome.configurations.TakehomeRateLimitConfiguration;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitPublicService extends RateLimitAbstract {

    @Autowired
    public RateLimitPublicService(TakehomeRateLimitConfiguration rateLimitConfig) {

        Refill refill = Refill.intervally(rateLimitConfig.getNumberOfUnsecureRequest(), Duration.ofSeconds(1));
        Bandwidth bandwidth = Bandwidth.classic(rateLimitConfig.getNumberOfUnsecureRequest(), refill);
        setBucket(Bucket.builder().addLimit(bandwidth).build());
    }
}
