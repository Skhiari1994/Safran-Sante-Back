package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Agence;
import com.arabsoft.referentiel.entities.cle.CleAgence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgenceRepository extends JpaRepository<Agence, CleAgence> {

  @Query(value = "select * from agence where cod_banq = :banq order by cod_banq, cod_agc", nativeQuery = true)
  List<Agence> getAgence(@Param("banq") String banq);

}
