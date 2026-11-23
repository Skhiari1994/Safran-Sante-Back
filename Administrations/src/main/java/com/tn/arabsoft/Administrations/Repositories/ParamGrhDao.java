package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.ParamGrh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParamGrhDao extends JpaRepository<ParamGrh, Integer> {

    Optional<ParamGrh> findByLibParam(String libParam);

    @Query("select coalesce(max(p.idParam), 0) + 1 from ParamGrh p")
    Integer getNextId();

}
