package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.BordEnvoi;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BordEnvoiCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.BordEnvoiPrejection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.BordEnvoiProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.BordereauProjection;

import java.time.LocalDate;
import java.util.List;

public interface BordEnvoiRepository extends JpaRepository<BordEnvoi, BordEnvoiCle> {

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord
                        from bord_envoi b
                        where b.cod_soc = :codSoc
                          and b.typ_bord = 'C'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordereauProjection> getListBordEnvoi(@Param("codSoc") String codSoc);

        @Query(value = """
                        select t.cod_bord,
                               t.cod_assur,
                               t.dat_bord,
                               t.dat_deb,
                               t.dat_fin,
                               t.nbr_bult,
                               t.tot_honor,
                               t.typ_bord,
                               t.cod_soc,
                               t.tot_net,
                               t.valid_bord,
                               t.reg_bord,
                               t.tot_remb,
                               t.envoi_bord,
                               a.lib_assur AS lib_assur
                        from bord_envoi t
                        left join assurance a
                               on a.cod_assur = t.cod_assur
                        where t.cod_bord = :codBord
                          and t.dat_bord = :datBord
                        """, nativeQuery = true)
        List<BordEnvoiPrejection> getBordEnvoi(
                        @Param("codBord") String codBord,
                        @Param("datBord") LocalDate datBord);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord
                        from bord_envoi b
                        where b.cod_soc = :codSoc
                          and b.typ_bord = 'M'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordereauProjection> getListBordEnvoiMut(@Param("codSoc") String codSoc);

        @Query(value = """
                        select b.*
                        from bord_envoi b
                        where b.cod_soc = :codSoc
                          and b.typ_bord = 'C'
                          and coalesce(b.envoi_bord, 'O') = 'O'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordEnvoiPrejection> getListBordEnvoiCNAM(@Param("codSoc") String codSoc);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               a.lib_assur as lib_assurance,
                               b.dat_deb,
                               b.dat_fin,
                               b.tot_net,
                               b.tot_honor
                        from bord_envoi b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.typ_bord = 'C'
                          and coalesce(b.envoi_bord, 'N') = 'N'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordEnvoiProjection> enteteBordEnvoi(@Param("soc") String soc);

        @Query(value = """
                        select t.cod_bord,
                               t.cod_assur,
                               t.dat_bord,
                               t.dat_deb,
                               t.dat_fin,
                               t.nbr_bult,
                               t.tot_honor,
                               t.typ_bord,
                               t.cod_soc,
                               t.tot_net,
                               t.valid_bord,
                               t.reg_bord,
                               t.tot_remb,
                               t.envoi_bord,
                               a.lib_assur as lib_assur
                        from bord_envoi t
                        left join assurance a
                               on a.cod_assur = t.cod_assur
                        where t.cod_bord = :codBord
                          and t.dat_bord = :datBord
                        """, nativeQuery = true)
        List<BordEnvoiPrejection> getBordEnvoiMut(
                        @Param("codBord") String codBord,
                        @Param("datBord") LocalDate datBord);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               a.lib_assur as lib_assurance,
                               b.dat_deb,
                               b.dat_fin,
                               b.tot_net,
                               b.tot_honor,
                               b.tot_remb,
                               b.cod_assur,
                               b.reg_bord
                        from bord_envoi b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.typ_bord = 'M'
                          and b.cod_soc = :soc
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordEnvoiProjection> bordEnvoi(@Param("soc") String soc);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               a.lib_assur AS lib_assurance,
                               b.dat_deb,
                               b.dat_fin,
                               b.tot_net,
                               b.tot_honor,
                               b.tot_remb,
                               b.cod_assur
                        from bord_envoi b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.typ_bord = 'M'
                          and b.cod_soc = :soc
                          and coalesce(b.valid_bord, 'N') = 'O'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordEnvoiProjection> payBordEnvoi(@Param("soc") String soc);

        @Modifying
        @Transactional
        @Query("""
                        update BordEnvoi b
                        set b.valid_bord = 'O'
                        where b.cod_soc = :codSoc
                          and b.cod_assur = :codAssur
                          and b.cod_bord = :codBord
                          and b.typ_bord = 'M'
                        """)
        int markValidBord(String codSoc,
                        String codAssur,
                        String codBord);
}