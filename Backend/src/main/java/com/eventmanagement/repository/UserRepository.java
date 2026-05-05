package com.eventmanagement.repository;

import com.eventmanagement.model.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends org.springframework.data.mongodb.repository.MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findByRole(User.UserRole role);
    boolean existsByEmail(String email);
}

