package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.CertifFamille;
import com.arabsoft.gestion_adherent.entities.cle.CleCertifFamille;

import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface CertifFamilleRepository extends JpaRepository<CertifFamille, CleCertifFamille> {

    @Query(value = "select * from certif_famille where mat_pers = :mat and num_fam = :num_fam", nativeQuery = true)
    List<CertifFamille> getCertifEnf(@Param("mat") String mat, @Param("num_fam") String num_fam);

    @Query(value = "delete from certif_famille f where cod_soc = :soc and f.num_fam = :num and f.mat_pers = :mat and annee_certif = :annee", nativeQuery = true)
    public void deleteCertif(@Param("soc") String soc, @Param("num") Long num, @Param("mat") String mat,
            @Param("annee") String annee);

}