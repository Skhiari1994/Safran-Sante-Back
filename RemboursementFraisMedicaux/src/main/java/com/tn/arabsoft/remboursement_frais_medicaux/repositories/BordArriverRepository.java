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