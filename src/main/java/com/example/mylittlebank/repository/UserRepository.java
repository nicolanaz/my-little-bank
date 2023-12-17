package com.example.mylittlebank.repository;

import com.example.mylittlebank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByFullName(String fullName);

    Optional<User> findUserByEmail(String email);
}
