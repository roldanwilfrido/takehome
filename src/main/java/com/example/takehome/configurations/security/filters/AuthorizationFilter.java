package com.example.takehome.configurations.security.filters;

import com.example.takehome.configurations.security.LoginConfiguration;
import com.example.takehome.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@AllArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private LoginConfiguration loginConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String bearerToken = request.getHeader(TokenUtils.AUTHORIZATION);

        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(TokenUtils.BEARER)) {
            String token = bearerToken.replace(TokenUtils.BEARER, "");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    TokenUtils.getAuthentication(token, loginConfig.getSecret());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
