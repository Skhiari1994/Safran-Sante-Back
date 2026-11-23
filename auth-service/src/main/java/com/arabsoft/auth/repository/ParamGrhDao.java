package com.arabsoft.auth.repository;

import com.arabsoft.auth.model.ParamGrh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ParamGrhDao extends JpaRepository<ParamGrh, Integer> {

    List<ParamGrh> findByLibParamIn(Collection<String> libParams);

}
