package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUselogin(String useLogin);

    Optional<User> findByEmail(String email);

}