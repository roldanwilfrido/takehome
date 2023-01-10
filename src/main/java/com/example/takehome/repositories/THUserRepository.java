package com.example.takehome.repositories;

import com.example.takehome.domain.entities.THUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface THUserRepository extends JpaRepository<THUser, Long> {

    Optional<THUser> findByEmail(String email);
}
