package com.arabsoft.gestionconvention.Repositories;

import com.arabsoft.gestionconvention.Entities.Convention;
import com.arabsoft.gestionconvention.Projections.DemondeConvProjection;
import com.arabsoft.gestionconvention.Projections.LibSocProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConventionRepository extends JpaRepository<Convention,String> {

    @Query(value = "select cod_soc, lib_soc from societe",nativeQuery = true)
    List<LibSocProjection> getLibSocProjection();
}
