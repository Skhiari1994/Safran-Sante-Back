package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RefEtablis;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleRefEtablis;

import java.util.List;

public interface RefEtablisRepository extends JpaRepository<RefEtablis, CleRefEtablis> {

        @Query(value = "select * from ref_etablis where PRF_TYP = '2' AND COnv_cnam = 'O'", nativeQuery = true)
        List<RefEtablis> getRefEtablis();

        @Query(value = "select * from ref_etablis where PRF_TYP = '1' AND COnv_cnam = 'O'", nativeQuery = true)
        List<RefEtablis> getPersPhysique();

        @Query(value = " select t.prf_typ,  \n" +
                        "       t.prf_cod,\n" +
                        "       t.prf_cle,\n" +
                        "      t.ETAB_RSOC||' '||t.PR_RSOC etab_rsoc  ,\n" +
                        "       t.pr_rsoc,\n" +
                        "       t.etab_rsoc_a,\n" +
                        "       t.pr_rsoc_a,\n" +
                        "       t.met_etab,\n" +
                        "       t.comp_etab,\n" +
                        "       t.adr_etablis,\n" +
                        "       t.cod_post,\n" +
                        "       t.cod_gouv,\n" +
                        "       t.conv_etab,\n" +
                        "       t.dat_conv_etab,\n" +
                        "       t.rib_etab,\n" +
                        "       t.ref_conv_etat,\n" +
                        "       t.adr_etablis_a,\n" +
                        "       t.cod_activ,\n" +
                        "       t.conv_cnam,\n" +
                        "       t.tel_etablis,\n" +
                        "       t.fax_etablis,\n" +
                        "       t.email_etablis,\n" +
                        "       t.resp,\n" +
                        "       t.resp_a,\n" +
                        "       t.etat_act_mut,\n" +
                        "       t.etat_act_cnam \n" +
                        "from ref_etablis t  where PRF_TYP =:refTyp", nativeQuery = true)
        List<RefEtablis> getRefEtablis(@Param("refTyp") String refTyp);

        @Query(value = " select t.prf_typ,  \n" +
                        "       t.prf_cod,\n" +
                        "       t.prf_cle,\n" +
                        "      t.ETAB_RSOC||' '||t.PR_RSOC etab_rsoc  ,\n" +
                        "       t.pr_rsoc,\n" +
                        "       t.etab_rsoc_a,\n" +
                        "       t.pr_rsoc_a,\n" +
                        "       t.met_etab,\n" +
                        "       t.comp_etab,\n" +
                        "       t.adr_etablis,\n" +
                        "       t.cod_post,\n" +
                        "       t.cod_gouv,\n" +
                        "       t.conv_etab,\n" +
                        "       t.dat_conv_etab,\n" +
                        "       t.rib_etab,\n" +
                        "       t.ref_conv_etat,\n" +
                        "       t.adr_etablis_a,\n" +
                        "       t.cod_activ,\n" +
                        "       t.conv_cnam,\n" +
                        "       t.tel_etablis,\n" +
                        "       t.fax_etablis,\n" +
                        "       t.email_etablis,\n" +
                        "       t.resp,\n" +
                        "       t.resp_a,\n" +
                        "       t.etat_act_mut,\n" +
                        "       t.etat_act_cnam \n" +
                        "from ref_etablis t  where PRF_TYP =:refTyp and t.prf_cod=:prfCod", nativeQuery = true)
        RefEtablis getRefEtablisByCode(@Param("prfCod") String prfCod, @Param("refTyp") String refTyp);
}
