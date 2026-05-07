package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.PersAffil;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePersAffil;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.PersAffilProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.RefEtablisProjection;

import java.util.List;

public interface PersAffilRepository extends JpaRepository<PersAffil, ClePersAffil> {

        @Query(value = """
                        select
                            t.cod_soc,
                            t.mat_pers,
                            t.cod_fil,
                            t.courant,
                            t.annee_aff,
                            t.prf_typ,
                            t.prf_cod,
                            rf.lib_fill AS lib_fil
                        from pers_affil t
                        left join ref_filliere rf
                               on rf.cod_fil = t.cod_fil
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                        """, nativeQuery = true)
        List<PersAffilProjection> getPersAffil(
                        @Param("soc") String soc,
                        @Param("mat") String mat);

        @Query(value = """
                        select
                            r.prf_typ,
                            r.prf_cod,
                            concat(
                                coalesce(r.etab_rsoc, ''),
                                concat(' ', coalesce(r.pr_rsoc, ''))
                            ) AS nom
                        from ref_etablis r
                        where r.prf_typ = '1'
                          and r.cod_activ in (
                                select cod_activ
                                  from ref_filliere
                                 where cod_fil = '2'
                          )
                        order by cast(r.prf_cod as int)
                        """, nativeQuery = true)
        List<RefEtablisProjection> getRefEtablis();

}