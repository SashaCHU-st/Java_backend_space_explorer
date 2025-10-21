package com.example.server.repository;

import com.example.server.model.User;
// import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {
    // Optional<User> findByEmail(String email);
     User findByEmail(String email);
}
