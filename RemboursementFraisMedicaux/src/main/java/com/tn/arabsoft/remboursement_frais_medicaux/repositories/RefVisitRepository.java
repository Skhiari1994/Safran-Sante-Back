package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RefVisit;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.AppareilProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.VisitProjection;

import java.util.List;

public interface RefVisitRepository extends JpaRepository<RefVisit, String> {

        @Query(value = "select COD_VISIT,r.LIB_VISIT,r.abrv_act,r.PRIX_VISIT,r.TAUX_REMB\n" +
                        "from ref_visit r ,acte a,bareme_remb b \n" +
                        "where r.ABRV_ACT = a.abrv_act\n" +
                        "and nvl(a.parente,:parente) =:parente \n" +
                        "and nvl(a.sexe,:sexe) =:sexe \n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and b.cod_fil =:codFil \n" +
                        "and b.COD_ASSUR =:codAssur ", nativeQuery = true)
        List<VisitProjection> getVisit(@Param("parente") String parente, @Param("sexe") String sexe,
                        @Param("codFil") String codFil, @Param("codAssur") String codAssur);

        @Query(value = "select COD_APP,r.LIB_APP,r.abrv_act\n" +
                        "from ref_appareil r ,acte a,bareme_remb b \n" +
                        "where r.ABRV_ACT = a.abrv_act\n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and b.cod_fil =:codFil \n" +
                        "and b.COD_ASSUR =:codAssur", nativeQuery = true)
        List<AppareilProjection> getAppareil(@Param("codFil") String codFil, @Param("codAssur") String codAssur);

        @Query(value = "select COD_VISIT,r.LIB_VISIT,r.abrv_act,r.PRIX_VISIT,r.TAUX_REMB\n" +
                        "from ref_visit r ,acte a,bareme_remb b \n" +
                        "where r.ABRV_ACT = a.abrv_act\n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and b.cod_fil = :codFil\n" +
                        "and b.COD_ASSUR = :codAssur", nativeQuery = true)
        List<VisitProjection> getVisitManuel(@Param("codFil") String codFil, @Param("codAssur") String codAssur);
}
