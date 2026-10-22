package com.arabsoft.communication.repository;

import com.arabsoft.communication.Entities.AdmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmUserRepository extends JpaRepository<AdmUser, Long> {

    Optional<AdmUser> findByLogin(String login);

    Optional<AdmUser> findByMatricule(String matricule);
}
