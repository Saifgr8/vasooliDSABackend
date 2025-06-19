package com.example.vasooliDSA.Repos;

import com.example.vasooliDSA.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
