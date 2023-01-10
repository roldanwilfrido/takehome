package com.example.takehome.configurations.security.filters;

import com.example.takehome.configurations.security.LoginConfiguration;
import com.example.takehome.domain.login.LoginCredentials;
import com.example.takehome.domain.login.THUserDetails;
import com.example.takehome.utils.TokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private LoginConfiguration loginConfig;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        LoginCredentials loginCredentials = new LoginCredentials();

        try {
            loginCredentials = new ObjectMapper().readValue(request.getReader(), LoginCredentials.class);
        } catch (IOException e) {
            log.info(e.getMessage());
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginCredentials.getEmail(),
                        loginCredentials.getPassword(),
                        Collections.emptyList());

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        THUserDetails userDetails = (THUserDetails) authResult.getPrincipal();
        String token = TokenUtils.generateToken(
                userDetails.getName(),
                userDetails.getUsername(),
                loginConfig.getValiditySeconds(),
                loginConfig.getSecret());

        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }

    public void setLoginConfig(LoginConfiguration loginConfig) {
        this.loginConfig = loginConfig;
    }
}
