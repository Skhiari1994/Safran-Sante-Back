package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RefVisit;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.AppareilProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.VisitProjection;

import java.util.List;

public interface RefVisitRepository extends JpaRepository<RefVisit, String> {

        @Query(value = """
                        select
                            r.cod_visit,
                            r.lib_visit,
                            r.abrv_act,
                            r.prix_visit,
                            r.taux_remb
                        from ref_visit r
                        inner join acte a
                                on r.abrv_act = a.abrv_act
                        inner join bareme_remb b
                                on b.abrv_act = a.abrv_act
                        where coalesce(a.parente, :parente) = :parente
                          and coalesce(a.sexe, :sexe) = :sexe
                          and b.cod_fil = :codFil
                          and b.cod_assur = :codAssur
                        """, nativeQuery = true)
        List<VisitProjection> getVisit(
                        @Param("parente") String parente,
                        @Param("sexe") String sexe,
                        @Param("codFil") String codFil,
                        @Param("codAssur") String codAssur);

        @Query(value = """
                        select
                            r.cod_app,
                            r.lib_app,
                            r.abrv_act
                        from ref_appareil r
                        inner join acte a
                                on r.abrv_act = a.abrv_act
                        inner join bareme_remb b
                                on b.abrv_act = a.abrv_act
                        where b.cod_fil = :codFil
                          and b.cod_assur = :codAssur
                        """, nativeQuery = true)
        List<AppareilProjection> getAppareil(
                        @Param("codFil") String codFil,
                        @Param("codAssur") String codAssur);

        @Query(value = """
                        select
                            r.cod_visit,
                            r.lib_visit,
                            r.abrv_act,
                            r.prix_visit,
                            r.taux_remb
                        from ref_visit r
                        inner join acte a
                                on r.abrv_act = a.abrv_act
                        inner join bareme_remb b
                                on b.abrv_act = a.abrv_act
                        where b.cod_fil = :codFil
                          and b.cod_assur = :codAssur
                        """, nativeQuery = true)
        List<VisitProjection> getVisitManuel(
                        @Param("codFil") String codFil,
                        @Param("codAssur") String codAssur);

}