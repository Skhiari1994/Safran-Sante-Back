package com.arabsoft.auth.repository;

import com.arabsoft.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User ,Long> {
   Optional<User> findByUselogin(String email);
   Optional<User> findByEmail(String email);
}
