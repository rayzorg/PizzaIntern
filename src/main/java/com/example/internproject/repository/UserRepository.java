package com.example.internproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.internproject.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
