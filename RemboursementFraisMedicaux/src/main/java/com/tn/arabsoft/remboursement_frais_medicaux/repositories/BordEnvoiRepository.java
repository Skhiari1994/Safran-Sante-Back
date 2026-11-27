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

        /*
         * Bordereaux CNAM non envoyés, page par page : même liste que enteteBordEnvoi (BordEnvoiEntete/{soc}).
         * code : égalité exacte (code saisi dans le champ) ; autres colonnes : « contient » (dates en jj/mm/aaaa) ;
         * l1..l3 : mots de la recherche unique, cherchés dans les colonnes affichées par l'écran (v_xxx = 1).
         * Ordre : date du bordereau décroissante, puis code (ordre stable d'une page à l'autre).
         */
        @Query(value = """
                        select q.cod_bord, q.dat_bord, q.nbr_bult, q.lib_assurance, q.dat_deb, q.dat_fin, q.tot_net, q.tot_honor
                        from (
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
                        where b.cod_soc = :soc
                          and b.typ_bord = 'C'
                          and coalesce(b.envoi_bord, 'N') = 'N'
                        ) q
                        where (:code is null or q.cod_bord = :code)
                          and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                          and (:dat_bord is null or convert(varchar(10), q.dat_bord, 103) like :dat_bord escape '!')
                          and (:l1 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l1 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l1 escape '!'))
                          and (:l2 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l2 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l2 escape '!'))
                          and (:l3 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l3 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l3 escape '!'))
                        order by q.dat_bord desc, q.cod_bord desc
                        offset :debut rows fetch next :taille rows only
                        """, nativeQuery = true)
        List<BordEnvoiProjection> pageBordEnvoiEntete(@Param("soc") String soc, @Param("code") String code, @Param("cod_bord") String cod_bord, @Param("dat_bord") String dat_bord, @Param("v_cod_bord") int v_cod_bord, @Param("v_dat_bord") int v_dat_bord, @Param("l1") String l1, @Param("l2") String l2, @Param("l3") String l3,
                        @Param("debut") int debut, @Param("taille") int taille);

        @Query(value = """
                        select count(*)
                        from (
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
                        where b.cod_soc = :soc
                          and b.typ_bord = 'C'
                          and coalesce(b.envoi_bord, 'N') = 'N'
                        ) q
                        where (:code is null or q.cod_bord = :code)
                          and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                          and (:dat_bord is null or convert(varchar(10), q.dat_bord, 103) like :dat_bord escape '!')
                          and (:l1 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l1 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l1 escape '!'))
                          and (:l2 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l2 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l2 escape '!'))
                          and (:l3 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l3 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l3 escape '!'))
                        """, nativeQuery = true)
        long compterBordEnvoiEntete(@Param("soc") String soc, @Param("code") String code, @Param("cod_bord") String cod_bord, @Param("dat_bord") String dat_bord, @Param("v_cod_bord") int v_cod_bord, @Param("v_dat_bord") int v_dat_bord, @Param("l1") String l1, @Param("l2") String l2, @Param("l3") String l3);

        /*
         * Bordereaux CNAM envoyés, page par page : même liste que getListBordEnvoiCNAM (non_vide = 0) ou getBordtCnam (non_vide = 1, sans code vide).
         * code : égalité exacte (code saisi dans le champ) ; autres colonnes : « contient » (dates en jj/mm/aaaa) ;
         * l1..l3 : mots de la recherche unique, cherchés dans les colonnes affichées par l'écran (v_xxx = 1).
         * Ordre : date du bordereau décroissante, puis code (ordre stable d'une page à l'autre).
         */
        @Query(value = """
                        select q.*
                        from (
                        select b.*
                        from bord_envoi b
                        where b.cod_soc = :soc
                          and b.typ_bord = 'C'
                          and coalesce(b.envoi_bord, 'O') = 'O'
                          and (:non_vide = 0 or b.cod_bord is not null)
                        ) q
                        where (:code is null or q.cod_bord = :code)
                          and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                          and (:dat_bord is null or convert(varchar(10), q.dat_bord, 103) like :dat_bord escape '!')
                          and (:cod_assur is null or lower(q.cod_assur) like :cod_assur escape '!')
                          and (:nbr_bult is null or cast(q.nbr_bult as varchar(30)) like :nbr_bult escape '!')
                          and (:tot_remb is null or cast(q.tot_remb as varchar(40)) like :tot_remb escape '!')
                          and (:l1 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l1 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l1 escape '!')
                               or (:v_cod_assur = 1 and lower(q.cod_assur) like :l1 escape '!')
                               or (:v_nbr_bult = 1 and cast(q.nbr_bult as varchar(30)) like :l1 escape '!')
                               or (:v_tot_remb = 1 and cast(q.tot_remb as varchar(40)) like :l1 escape '!'))
                          and (:l2 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l2 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l2 escape '!')
                               or (:v_cod_assur = 1 and lower(q.cod_assur) like :l2 escape '!')
                               or (:v_nbr_bult = 1 and cast(q.nbr_bult as varchar(30)) like :l2 escape '!')
                               or (:v_tot_remb = 1 and cast(q.tot_remb as varchar(40)) like :l2 escape '!'))
                          and (:l3 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l3 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l3 escape '!')
                               or (:v_cod_assur = 1 and lower(q.cod_assur) like :l3 escape '!')
                               or (:v_nbr_bult = 1 and cast(q.nbr_bult as varchar(30)) like :l3 escape '!')
                               or (:v_tot_remb = 1 and cast(q.tot_remb as varchar(40)) like :l3 escape '!'))
                        order by q.dat_bord desc, q.cod_bord desc
                        offset :debut rows fetch next :taille rows only
                        """, nativeQuery = true)
        List<BordEnvoiPrejection> pageBordEnvoiCnam(@Param("soc") String soc, @Param("code") String code, @Param("cod_bord") String cod_bord, @Param("dat_bord") String dat_bord, @Param("cod_assur") String cod_assur, @Param("nbr_bult") String nbr_bult, @Param("tot_remb") String tot_remb, @Param("v_cod_bord") int v_cod_bord, @Param("v_dat_bord") int v_dat_bord, @Param("v_cod_assur") int v_cod_assur, @Param("v_nbr_bult") int v_nbr_bult, @Param("v_tot_remb") int v_tot_remb, @Param("l1") String l1, @Param("l2") String l2, @Param("l3") String l3, @Param("non_vide") int non_vide,
                        @Param("debut") int debut, @Param("taille") int taille);

        @Query(value = """
                        select count(*)
                        from (
                        select b.*
                        from bord_envoi b
                        where b.cod_soc = :soc
                          and b.typ_bord = 'C'
                          and coalesce(b.envoi_bord, 'O') = 'O'
                          and (:non_vide = 0 or b.cod_bord is not null)
                        ) q
                        where (:code is null or q.cod_bord = :code)
                          and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                          and (:dat_bord is null or convert(varchar(10), q.dat_bord, 103) like :dat_bord escape '!')
                          and (:cod_assur is null or lower(q.cod_assur) like :cod_assur escape '!')
                          and (:nbr_bult is null or cast(q.nbr_bult as varchar(30)) like :nbr_bult escape '!')
                          and (:tot_remb is null or cast(q.tot_remb as varchar(40)) like :tot_remb escape '!')
                          and (:l1 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l1 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l1 escape '!')
                               or (:v_cod_assur = 1 and lower(q.cod_assur) like :l1 escape '!')
                               or (:v_nbr_bult = 1 and cast(q.nbr_bult as varchar(30)) like :l1 escape '!')
                               or (:v_tot_remb = 1 and cast(q.tot_remb as varchar(40)) like :l1 escape '!'))
                          and (:l2 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l2 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l2 escape '!')
                               or (:v_cod_assur = 1 and lower(q.cod_assur) like :l2 escape '!')
                               or (:v_nbr_bult = 1 and cast(q.nbr_bult as varchar(30)) like :l2 escape '!')
                               or (:v_tot_remb = 1 and cast(q.tot_remb as varchar(40)) like :l2 escape '!'))
                          and (:l3 is null
                               or (:v_cod_bord = 1 and lower(q.cod_bord) like :l3 escape '!')
                               or (:v_dat_bord = 1 and convert(varchar(10), q.dat_bord, 103) like :l3 escape '!')
                               or (:v_cod_assur = 1 and lower(q.cod_assur) like :l3 escape '!')
                               or (:v_nbr_bult = 1 and cast(q.nbr_bult as varchar(30)) like :l3 escape '!')
                               or (:v_tot_remb = 1 and cast(q.tot_remb as varchar(40)) like :l3 escape '!'))
                        """, nativeQuery = true)
        long compterBordEnvoiCnam(@Param("soc") String soc, @Param("code") String code, @Param("cod_bord") String cod_bord, @Param("dat_bord") String dat_bord, @Param("cod_assur") String cod_assur, @Param("nbr_bult") String nbr_bult, @Param("tot_remb") String tot_remb, @Param("v_cod_bord") int v_cod_bord, @Param("v_dat_bord") int v_dat_bord, @Param("v_cod_assur") int v_cod_assur, @Param("v_nbr_bult") int v_nbr_bult, @Param("v_tot_remb") int v_tot_remb, @Param("l1") String l1, @Param("l2") String l2, @Param("l3") String l3, @Param("non_vide") int non_vide);

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
                        where b.cod_soc = :soc
                          and b.typ_bord = 'C'
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