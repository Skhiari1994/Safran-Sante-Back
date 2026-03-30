package com.arabsoft.auth.repository;

import com.arabsoft.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository  extends JpaRepository<Token, Integer> {

    @Query(value="insert into token (createdat,expiresat,token,useid,validatedat) values (:created,:expires,:token,:useid,:validatedat)",nativeQuery = true)
     void insertToken(@Param("created") String created,@Param("expires") String expires,@Param("token") String token,@Param("useid") Long useid,@Param("validatedat") String validatedat);
Optional<Token> findByToken(String token);
}
