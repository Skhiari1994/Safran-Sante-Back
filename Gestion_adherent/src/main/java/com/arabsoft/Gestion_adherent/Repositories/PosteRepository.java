package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.Poste;
import com.arabsoft.gestion_adherent.entities.cle.ClePoste;

import java.util.List;

public interface PosteRepository extends JpaRepository<Poste, ClePoste> {

    @Query(value = "select * from poste where cod_gouv = :gouv", nativeQuery = true)
    List<Poste> getPoste(@Param("gouv") String gouv);

}
