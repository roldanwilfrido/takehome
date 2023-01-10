package com.example.takehome.services;

import com.example.takehome.domain.entities.THUser;
import com.example.takehome.repositories.THUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class THUserServiceTests {

    @Mock
    THUserRepository repository;

    @Test
    void loadUserByUsernameReturnsAnUserDetails() {

        String email = "roldanhollow@gmail.com";
        THUser user = THUser.builder()
                .id(1L)
                .name("Roldan")
                .email(email)
                .build();

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        THUserService service =  new THUserService(repository);
        assertEquals(email, service.loadUserByUsername(email).getUsername());
    }

    @Test
    void loadUserByUsernameThrowsAnUsernameNotFoundException() {

        String noFoundEmail = "unknown@gmail.com";

        when(repository.findByEmail(noFoundEmail)).thenReturn(Optional.empty());
        THUserService service =  new THUserService(repository);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class, () -> service.loadUserByUsername(noFoundEmail));

        assertEquals("User not exists", exception.getMessage());
    }

}
