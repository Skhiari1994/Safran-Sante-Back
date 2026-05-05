package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.cle.ClePoste;
import com.arabsoft.referentiel.entities.Poste;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosteRepository extends JpaRepository<Poste, ClePoste> {

    @Query(value = "select * from poste where cod_gouv = :gouv", nativeQuery = true)
    List<Poste> getPoste(@Param("gouv") String gouv);

    @Query(value = "select * from poste where cod_gouv = :gouv and cod_poste = :post", nativeQuery = true)
    Poste getPosteById(@Param("gouv") String gouv, @Param("post") String post);

}
