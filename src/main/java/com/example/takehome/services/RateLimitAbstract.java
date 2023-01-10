package com.example.takehome.services;

import com.example.takehome.exceptions.TakehomeRateLimitException;
import io.github.bucket4j.Bucket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RateLimitAbstract {

    private Bucket bucket;

    /**
     * Checks if the request limit has been reached
     */
    public void checkRequestLimit() {

        if (!getBucket().tryConsume(1)) {
            throw new TakehomeRateLimitException();
        }
    }
}
