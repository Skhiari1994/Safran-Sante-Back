package com.arabsoft.communication.repository;

import com.arabsoft.communication.Entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, String> {
}
