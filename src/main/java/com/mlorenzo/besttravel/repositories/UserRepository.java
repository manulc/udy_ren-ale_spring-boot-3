package com.mlorenzo.besttravel.repositories;

import com.mlorenzo.besttravel.domain.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
