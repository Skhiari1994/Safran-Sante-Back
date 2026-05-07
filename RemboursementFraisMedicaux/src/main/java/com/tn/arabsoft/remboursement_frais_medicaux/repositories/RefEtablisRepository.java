package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RefEtablis;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleRefEtablis;

import java.util.List;

public interface RefEtablisRepository extends JpaRepository<RefEtablis, CleRefEtablis> {

        @Query(value = """
                        select *
                        from ref_etablis
                        where prf_typ = '2'
                          and conv_cnam = 'O'
                        """, nativeQuery = true)
        List<RefEtablis> getRefEtablis();

        @Query(value = """
                        select *
                        from ref_etablis
                        where prf_typ = '1'
                          and conv_cnam = 'O'
                        """, nativeQuery = true)
        List<RefEtablis> getPersPhysique();

        @Query(value = """
                        select
                            t.prf_typ,
                            t.prf_cod,
                            t.prf_cle,
                            concat(coalesce(t.etab_rsoc, ''), concat(' ', coalesce(t.pr_rsoc, ''))) as etab_rsoc,
                            t.pr_rsoc,
                            t.etab_rsoc_a,
                            t.pr_rsoc_a,
                            t.met_etab,
                            t.comp_etab,
                            t.adr_etablis,
                            t.cod_post,
                            t.cod_gouv,
                            t.conv_etab,
                            t.dat_conv_etab,
                            t.rib_etab,
                            t.ref_conv_etat,
                            t.adr_etablis_a,
                            t.cod_activ,
                            t.conv_cnam,
                            t.tel_etablis,
                            t.fax_etablis,
                            t.email_etablis,
                            t.resp,
                            t.resp_a,
                            t.etat_act_mut,
                            t.etat_act_cnam
                        from ref_etablis t
                        where t.prf_typ = :refTyp
                        """, nativeQuery = true)
        List<RefEtablis> getRefEtablis(@Param("refTyp") String refTyp);

        @Query(value = """
                        select
                            t.prf_typ,
                            t.prf_cod,
                            t.prf_cle,
                            concat(coalesce(t.etab_rsoc, ''), concat(' ', coalesce(t.pr_rsoc, ''))) as etab_rsoc,
                            t.pr_rsoc,
                            t.etab_rsoc_a,
                            t.pr_rsoc_a,
                            t.met_etab,
                            t.comp_etab,
                            t.adr_etablis,
                            t.cod_post,
                            t.cod_gouv,
                            t.conv_etab,
                            t.dat_conv_etab,
                            t.rib_etab,
                            t.ref_conv_etat,
                            t.adr_etablis_a,
                            t.cod_activ,
                            t.conv_cnam,
                            t.tel_etablis,
                            t.fax_etablis,
                            t.email_etablis,
                            t.resp,
                            t.resp_a,
                            t.etat_act_mut,
                            t.etat_act_cnam
                        from ref_etablis t
                        where t.prf_typ = :refTyp
                          and t.prf_cod = :prfCod
                        """, nativeQuery = true)
        RefEtablis getRefEtablisByCode(
                        @Param("prfCod") String prfCod,
                        @Param("refTyp") String refTyp);

}