package com.example.takehome.configurations.security;

import com.example.takehome.configurations.security.filters.AuthorizationFilter;
import com.example.takehome.configurations.security.filters.LoginFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private final LoginConfiguration loginConfig;
    private final UserDetailsService userDetailsService;
    private final AuthorizationFilter authorizationFilter;

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManager authManager) throws Exception {

        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(generateLoginFilter(authManager))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /**
     * Generates a filter to login purposes
     * @param authManager {@link AuthenticationManager} object
     * @return {@link LoginFilter} object
     */
    private LoginFilter generateLoginFilter(AuthenticationManager authManager) {

        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(authManager);
        loginFilter.setLoginConfig(loginConfig);
        loginFilter.setFilterProcessesUrl(loginConfig.getUrl());
        return loginFilter;
    }
}
