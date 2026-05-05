package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.cle.CleRefEtablis;
import com.arabsoft.referentiel.entities.RefEtablis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefEtablisRepository extends JpaRepository<RefEtablis, CleRefEtablis> {

    @Query(value = "select * from ref_etablis where prf_typ = '2' and conv_cnam = 'O'", nativeQuery = true)
    List<RefEtablis> getRefEtablis();

    @Query(value = "select * from ref_etablis where prf_typ = '1' and conv_etab = 'O'  ", nativeQuery = true)
    List<RefEtablis> getPersPhysiqueParamMutuelle();

    @Query(value = "select * from ref_etablis where prf_typ = '2' and conv_etab = 'O'  ", nativeQuery = true)
    List<RefEtablis> getEtablissParamMutuelle();

    @Query(value = "select * from ref_etablis where prf_typ = '1' and conv_cnam = 'O'", nativeQuery = true)
    List<RefEtablis> getPersPhysique();

}
