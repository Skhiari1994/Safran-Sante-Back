package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Cle.CleRefEtablis;
import com.arabsoft.referentiel.Entities.RefEtablis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefEtablisRepository extends JpaRepository<RefEtablis, CleRefEtablis> {

    @Query(value="select * from ref_etablis where PRF_TYP = '2' AND COnv_cnam = 'O'",nativeQuery = true)
    List<RefEtablis> getRefEtablis();
    @Query(value="select * from ref_etablis where PRF_TYP = '1' AND CONV_ETAB = 'O'  ",nativeQuery = true)
    List<RefEtablis> getPersPhysiqueParamMutuelle();
    @Query(value="select * from ref_etablis where PRF_TYP = '2' AND CONV_ETAB = 'O'  ",nativeQuery = true)
    List<RefEtablis> getEtablissParamMutuelle();
    @Query(value="select * from ref_etablis where PRF_TYP = '1' AND COnv_cnam = 'O'",nativeQuery = true)
    List<RefEtablis> getPersPhysique();
}
