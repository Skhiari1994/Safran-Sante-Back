package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BordArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BordArriverProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BultArriverProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BordArriverRepository extends JpaRepository<BordArriver, String> {

        @Query(value = "select b.cod_bord,b.dat_bord,b.nbr_bult,b.tot_remb,b.valid,\n" +
                        "(select a.lib_assur from assurance a where a.cod_assur = b.cod_assur) lib_assurance\n" +
                        "from bord_arriver b" +
                        "ORDER BY b.dat_bord DESC", nativeQuery = true)

        List<BordArriverProjection> findAllBord();

        @Query(value = "select b.cod_bord,b.dat_bord,b.nbr_bult,b.tot_remb,b.valid,\n" +
                        "(select a.lib_assur from assurance a where a.cod_assur = b.cod_assur) lib_assurance,cod_assur\n"
                        +
                        "from bord_arriver b where COD_SOC = :cod_soc\n" +
                        "and nvl(VALID,'N') = 'N' ORDER BY b.dat_bord DESC", nativeQuery = true)

        List<BordArriverProjection> EnteteSaisieBordArriverManuCnam(@Param("cod_soc") String cod_soc);

        @Query(value = "select b.cod_bord,b.dat_bord,b.nbr_bult,b.tot_remb,b.valid,\n" +
                        "(select a.lib_assur from assurance a where a.cod_assur = b.cod_assur) lib_assurance,cod_assur\n"
                        +
                        "from bord_arriver b where COD_SOC = :cod_soc\n" +
                        "and nvl(VALID,'N') = 'N' and typ_bord = 'L' order by b.dat_bord DESC", nativeQuery = true)

        List<BordArriverProjection> EnteteSaisieLibreCnam(@Param("cod_soc") String cod_soc);

        @Query(value = "select b.cod_bord,b.dat_bord,b.nbr_bult,b.tot_remb,b.valid,\n" +
                        "(select a.lib_assur from assurance a where a.cod_assur = b.cod_assur) lib_assurance,cod_assur\n"
                        +
                        "from bord_arriver b where COD_SOC = :cod_soc\n" +
                        " and nvl(VALID,'N') = 'O' and nvl(valid_bord,'N') = 'N' and typ_bord = 'L' ORDER BY b.dat_bord DESC", nativeQuery = true)

        List<BordArriverProjection> EnteteCalCompteLibreCnam(@Param("cod_soc") String cod_soc);

        @Query(value = "SELECT b.cod_bord, b.dat_bord, b.nbr_bult, b.tot_remb, b.valid, b.tot, " +
                        "(SELECT a.lib_assur FROM assurance a WHERE a.cod_assur = b.cod_assur) AS lib_assurance " +
                        "FROM bord_arriver b " +
                        "WHERE COD_SOC = :cod_soc " +
                        "AND NVL(VALID, 'N') = 'N' ORDER BY b.dat_bord DESC", nativeQuery = true)
        List<BordArriverProjection> EnteteSaisieBordArriverManuLibreCnam(@Param("cod_soc") String cod_soc);

        @Query(value = "select b.cod_bord,b.dat_bord,b.cod_assur,b.nbr_bult,b.tot_remb,b.valid,b.valid_bord,b.reg_bord,\n" +
                        "(select a.lib_assur from assurance a where a.cod_assur = b.cod_assur) lib_assurance\n" +
                        "from bord_arriver b where COD_SOC = :cod_soc " +
                        "  ORDER BY b.dat_bord DESC", nativeQuery = true)

        List<BordArriverProjection> GetAllBordArriver(@Param("cod_soc") String cod_soc);

        @Query(value = "SELECT ba.cod_bord," +
                        "ba.cod_assur,\n" +
                        "        (select lib_assur from assurance where cod_assur = ba.cod_assur) lib_assurance,\n" +
                        "       ba.dat_bord,\n" +
                        "       ba.nbr_bult,\n" +
                        "       ba.tot_remb,\n" +
                        "       ba.tot\n" +
                        "  FROM bord_arriver ba\n" +
                        "  ORDER BY ba.dat_bord DESC", nativeQuery = true)
        List<BordArriverProjection> BordArriver();

        @Query(value = "SELECT ba.cod_bord," +
                        "ba.cod_assur,\n" +
                        "        (select lib_assur from assurance where cod_assur = ba.cod_assur) lib_assurance,\n" +
                        "       ba.dat_bord,\n" +
                        "       ba.nbr_bult,\n" +
                        "       ba.tot_remb,\n" +
                        "       ba.tot\n" +
                        "  FROM bord_arriver ba where \n" +
                        " nvl(valid_bord,'N') = 'O' \n" +
                        "and nvl(clot_bord,'N') = 'O' ORDER BY ba.dat_bord DESC\n", nativeQuery = true)
        List<BordArriverProjection> ConsultBordArriver();

        @Query(value = "SELECT pk_bord_arriver.generate_bord_arriver_code(:codSoc) FROM dual", nativeQuery = true)
        String generateCodBord(@Param("codSoc") String codSoc);


        @Query(value = "SELECT ba.cod_bord," +
                "ba.cod_assur,\n" +
                "        (select lib_assur from assurance where cod_assur = ba.cod_assur) lib_assurance,\n" +
                "       ba.dat_bord,\n" +
                "       ba.nbr_bult,\n" +
                "       ba.tot_remb,\n" +
                "       ba.tot_net,\n" +
                "       ba.reg_bord,\n" +
                "       ba.valid_bord\n" +

                "  FROM bord_arriver ba where  ba.cod_bord=:bord \n", nativeQuery = true)
        List<BordArriverProjection> getCodBordArriver(@Param("bord") String bord);

}
