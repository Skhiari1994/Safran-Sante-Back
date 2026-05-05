package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.cle.CleEtatPret;
import com.arabsoft.referentiel.entities.EtatPret;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtatPretRepository extends JpaRepository<EtatPret, CleEtatPret> {

    @Query(value = "select * from etat_pret where typ_etat = 'P'", nativeQuery = true)
    List<EtatPret> getEtatPret();

    @Query(value = "select * from etat_pret where nat_etat_pret in ('SP', 'ST')", nativeQuery = true)
    List<EtatPret> getEtatPretSusp();

    @Query(value = "select * from etat_pret where cod_etat_pret = :etat", nativeQuery = true)
    EtatPret getEtatPretSuspByEtat(@Param("etat") String etat);

    @Modifying
    @Transactional
    @Query(value = "delete from etat_pret where typ_etat = :typ and cod_etat_pret = :etat", nativeQuery = true)
    void deleteEtatPret(@Param("typ") String typ, @Param("etat") String etat);

}
