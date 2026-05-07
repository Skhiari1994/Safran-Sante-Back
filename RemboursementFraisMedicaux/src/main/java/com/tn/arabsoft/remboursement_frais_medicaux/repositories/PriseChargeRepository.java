package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.PriseCharge;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePriseCharge;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.PriseChargeProjection;

import java.util.List;

public interface PriseChargeRepository extends JpaRepository<PriseCharge, ClePriseCharge> {

    @Query(value = """
            select
                t.cod_soc,
                t.mat_pers,
                t.num_pec,
                t.dat_pec,
                t.num_fam,
                t.etat_pec,
                t.prf_typ,
                t.prf_cod,
                t.mnt_pec,
                t.mnt_remb,
                t.dat_eff,
                case
                    when t.num_fam <> 0 then f.nom_pren
                    else 'Adhérent'
                end as nom,
                concat(coalesce(r.etab_rsoc, ''), concat(' ', coalesce(r.pr_rsoc, ''))) as nomEtab
            from prise_charge t
            left join famille f
                   on f.cod_soc = t.cod_soc
                  and f.mat_pers = t.mat_pers
                  and f.num_fam = t.num_fam
            left join ref_etablis r
                   on r.prf_typ = t.prf_typ
                  and r.prf_cod = t.prf_cod
            where t.cod_soc = :soc
              and t.mat_pers = :mat
            order by t.dat_pec desc
            """, nativeQuery = true)
    List<PriseChargeProjection> getPriseCharge(
            @Param("soc") String soc,
            @Param("mat") String mat);

    @Modifying
    @Transactional
    @Query(value = """
            delete from prise_charge
            where cod_soc = :soc
              and mat_pers = :mat
              and num_pec = :pec
            """, nativeQuery = true)
    void deletePriseCharge(
            @Param("soc") String soc,
            @Param("mat") String mat,
            @Param("pec") String pec);

}