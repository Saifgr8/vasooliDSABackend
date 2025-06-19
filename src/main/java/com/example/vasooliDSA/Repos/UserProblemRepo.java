package com.example.vasooliDSA.Repos;

import com.example.vasooliDSA.Models.UserProblem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserProblemRepo extends MongoRepository<UserProblem, String> {
    Optional<UserProblem> findByProblemLinkAndUserId(String problemLink, String userId);
    List<UserProblem> findByUserId(String userId);
}
