package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.Agence;
import com.arabsoft.Gestion_adherent.Entities.Cle.ClePoste;
import com.arabsoft.Gestion_adherent.Entities.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosteRepository extends JpaRepository<Poste, ClePoste> {
    @Query(value="select * from poste where cod_gouv=:gouv",nativeQuery = true)
    List<Poste> getPoste(@Param("gouv") String gouv);
}
