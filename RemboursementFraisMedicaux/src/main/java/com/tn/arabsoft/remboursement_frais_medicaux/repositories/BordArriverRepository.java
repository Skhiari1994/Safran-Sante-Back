package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.BordArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.BordArriverProjection;

import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface BordArriverRepository extends JpaRepository<BordArriver, String> {

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               b.tot_remb,
                               b.valid,
                               a.lib_assur AS lib_assurance
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> findAllBord();

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               b.tot_remb,
                               b.valid,
                               a.lib_assur AS lib_assurance,
                               b.cod_assur
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.cod_soc = :cod_soc
                          and coalesce(b.valid, 'N') = 'N'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> enteteSaisieBordArriverManuCnam(@Param("cod_soc") String cod_soc);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               b.tot_remb,
                               b.valid,
                               a.lib_assur AS lib_assurance,
                               b.cod_assur
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.cod_soc = :cod_soc
                          and coalesce(b.valid, 'N') = 'N'
                          and b.typ_bord = 'L'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> enteteSaisieLibreCnam(@Param("cod_soc") String cod_soc);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               b.tot_remb,
                               b.valid,
                               a.lib_assur as lib_assurance,
                               b.cod_assur
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.cod_soc = :cod_soc
                          and coalesce(b.valid, 'N') = 'O'
                          and coalesce(b.valid_bord, 'N') = 'N'
                          and b.typ_bord = 'L'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> enteteCalCompteLibreCnam(@Param("cod_soc") String cod_soc);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.nbr_bult,
                               b.tot_remb,
                               b.valid,
                               b.tot,
                               a.lib_assur AS lib_assurance
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.cod_soc = :cod_soc
                          and coalesce(b.valid, 'N') = 'N'
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> enteteSaisieBordArriverManuLibreCnam(@Param("cod_soc") String cod_soc);

        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.cod_assur,
                               b.nbr_bult,
                               b.tot_remb,
                               b.valid,
                               b.valid_bord,
                               b.reg_bord,
                               a.lib_assur AS lib_assurance
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where b.cod_soc = :cod_soc
                        order by b.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> getAllBordArriver(@Param("cod_soc") String cod_soc);

        @Query(value = """
                        select ba.cod_bord,
                               ba.cod_assur,
                               a.lib_assur AS lib_assurance,
                               ba.dat_bord,
                               ba.nbr_bult,
                               ba.tot_remb,
                               ba.tot
                        from bord_arriver ba
                        left join assurance a
                               on a.cod_assur = ba.cod_assur
                        order by ba.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> bordArriver();

        @Query(value = """
                        select ba.cod_bord,
                               ba.cod_assur,
                               a.lib_assur AS lib_assurance,
                               ba.dat_bord,
                               ba.nbr_bult,
                               ba.tot_remb,
                               ba.tot
                        from bord_arriver ba
                        left join assurance a
                               on a.cod_assur = ba.cod_assur
                        where coalesce(ba.valid_bord, 'N') = 'O'
                          and coalesce(ba.clot_bord, 'N') = 'O'
                        order by ba.dat_bord desc
                        """, nativeQuery = true)
        List<BordArriverProjection> consultBordArriver();

        /*
         * Fenêtre de recherche des bordereaux arrivés, page par page : filtres et tri faits par la base,
         * seules les lignes de la page demandée sont renvoyées. Chaque critère null est ignoré, ce qui
         * reproduit les listes complètes existantes (findAllBord, getAllBordArriver, bordArriver,
         * consultBordArriver, enteteSaisieBordArriverManuLibreCnam). cod_bord / dat_bord : motif LIKE
         * déjà préparé par le contrôleur (%texte%) ; code : égalité exacte (code saisi dans le champ).
         * Tri stable (date puis code) : une ligne n'apparaît jamais sur deux pages.
         */
        @Query(value = """
                        select b.cod_bord,
                               b.dat_bord,
                               b.cod_assur,
                               b.nbr_bult,
                               b.tot_remb,
                               b.tot,
                               b.valid,
                               b.valid_bord,
                               b.reg_bord,
                               a.lib_assur AS lib_assurance
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where (:cod_soc is null or b.cod_soc = :cod_soc)
                          and (:valid is null or coalesce(b.valid, 'N') = :valid)
                          and (:valid_bord is null or coalesce(b.valid_bord, 'N') = :valid_bord)
                          and (:clot_bord is null or coalesce(b.clot_bord, 'N') = :clot_bord)
                          and (:typ_bord is null or b.typ_bord = :typ_bord)
                          and (:code is null or b.cod_bord = :code)
                          and (:cod_bord is null or lower(b.cod_bord) like lower(:cod_bord) escape '!')
                          and (:dat_bord is null or convert(varchar(10), b.dat_bord, 103) like :dat_bord escape '!')
                        order by b.dat_bord desc, b.cod_bord desc
                        offset :debut rows fetch next :taille rows only
                        """, nativeQuery = true)
        List<BordArriverProjection> pageBordArriver(@Param("cod_soc") String cod_soc, @Param("valid") String valid,
                        @Param("valid_bord") String valid_bord, @Param("clot_bord") String clot_bord,
                        @Param("typ_bord") String typ_bord, @Param("code") String code,
                        @Param("cod_bord") String cod_bord, @Param("dat_bord") String dat_bord,
                        @Param("debut") int debut, @Param("taille") int taille);

        @Query(value = """
                        select count(*)
                        from bord_arriver b
                        left join assurance a
                               on a.cod_assur = b.cod_assur
                        where (:cod_soc is null or b.cod_soc = :cod_soc)
                          and (:valid is null or coalesce(b.valid, 'N') = :valid)
                          and (:valid_bord is null or coalesce(b.valid_bord, 'N') = :valid_bord)
                          and (:clot_bord is null or coalesce(b.clot_bord, 'N') = :clot_bord)
                          and (:typ_bord is null or b.typ_bord = :typ_bord)
                          and (:code is null or b.cod_bord = :code)
                          and (:cod_bord is null or lower(b.cod_bord) like lower(:cod_bord) escape '!')
                          and (:dat_bord is null or convert(varchar(10), b.dat_bord, 103) like :dat_bord escape '!')
                        """, nativeQuery = true)
        long compterBordArriver(@Param("cod_soc") String cod_soc, @Param("valid") String valid,
                        @Param("valid_bord") String valid_bord, @Param("clot_bord") String clot_bord,
                        @Param("typ_bord") String typ_bord, @Param("code") String code,
                        @Param("cod_bord") String cod_bord, @Param("dat_bord") String dat_bord);

        @Query(value = "select pk_bord_arriver.generate_bord_arriver_code(:codSoc) from dual", nativeQuery = true)
        String generateCodBord(@Param("codSoc") String codSoc);

        @Query(value = """
                        select ba.cod_bord,
                               ba.cod_assur,
                               a.lib_assur AS lib_assurance,
                               ba.dat_bord,
                               ba.nbr_bult,
                               ba.tot_remb,
                               ba.tot_net,
                               ba.reg_bord,
                               ba.valid_bord
                        from bord_arriver ba
                        left join assurance a
                               on a.cod_assur = ba.cod_assur
                        where ba.cod_bord = :bord
                        """, nativeQuery = true)
        List<BordArriverProjection> getCodBordArriver(@Param("bord") String bord);

}