package com.example.takehome.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenUtilsTests {

    @Test
    void callGenerateTokenReturnsTokenString() {

        byte[] bytes = new byte[36];
        String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String token = TokenUtils.generateToken(
                "Roldan",
                "roldanhollow@gmail.com",
                3_600L,
                secret);
        assertTrue(Objects.nonNull(token));
    }

    @Test
    void callGetAuthenticationReturnsUsernamePasswordAuthenticationTokenObject() {

        byte[] bytes = new byte[36];
        String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        String token = TokenUtils.generateToken(
                "Roldan",
                "roldanhollow@gmail.com",
                3_600L,
                secret);
        assertEquals("roldanhollow@gmail.com",
                        Objects.requireNonNull(
                                TokenUtils.getAuthentication(token, secret))
                                .getPrincipal());
    }

    @Test
    void callGetAuthenticationReturnsNullWhenTokenIsWrong() {

        assertNull(TokenUtils.getAuthentication("TOKEN", "SECRET"));
    }
}
