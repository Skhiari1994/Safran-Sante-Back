package com.arabsoft.auth.repository;

import com.arabsoft.auth.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role , Integer> {

    Optional<Role> findByName(String role);
    Page<Role> findAll(Pageable pageable);
}
