package com.example.takehome.services;

import com.example.takehome.domain.login.THUserDetails;
import com.example.takehome.domain.entities.THUser;
import com.example.takehome.repositories.THUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class THUserService implements UserDetailsService {

    private final THUserRepository thUserRepository;

    @Override
    public UserDetails loadUserByUsername(
            String email) throws UsernameNotFoundException {

        THUser user = thUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists"));
        return new THUserDetails(user.getName(), user.getEmail(), user.getPassword());
    }
}
