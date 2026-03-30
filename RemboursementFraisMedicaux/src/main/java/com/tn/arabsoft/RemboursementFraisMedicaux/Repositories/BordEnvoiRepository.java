package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BordArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BordEnvoi;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BordEnvoiCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BordArriverProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BordEnvoiPrejection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BordEnvoiProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BordereauProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BordEnvoiRepository extends JpaRepository<BordEnvoi, BordEnvoiCle> {


    @Query(value = "select cod_bord, dat_bord " +
            "from bord_envoi b " +
            "where b.cod_soc = :codSoc " +
            "and typ_bord = 'C' " +
            //"and nvl(envoi_bord, 'N') = 'N' " +
            "order by dat_bord desc",
            nativeQuery = true)
    List<BordereauProjection> getListBordEnvoi(@Param("codSoc") String codSoc);

    @Query(value="SELECT cod_bord, \n" +
            "       cod_assur, \n" +
            "       dat_bord, \n" +
            "       dat_deb, \n" +
            "       dat_fin, \n" +
            "       nbr_bult, \n" +
            "       tot_honor, \n" +
            "       typ_bord, \n" +
            "       cod_soc, \n" +
            "       tot_net, \n" +
            "       valid_bord, \n" +
            "       reg_bord, \n" +
            "       tot_remb, \n" +
            "       envoi_bord,\n" +
            "       (select lib_assur\n" +
            "\tfrom assurance\n" +
            "\twhere cod_assur = t.cod_assur) lib_assur\n" +
            "FROM BORD_ENVOI t\n" +
            "WHERE cod_bord = :codBord\n" +
            "AND TO_CHAR(dat_bord, 'DD/MM/YYYY') = :datBord",nativeQuery = true)
    List<BordEnvoiPrejection> getBordEnvoi(@Param("codBord")String codBord, @Param("datBord") LocalDate datBord);

    @Query(value="select cod_bord,dat_bord\n" +
            "from bord_envoi b \n" +
            "where b.cod_soc =:codSoc\n" +
            "and typ_bord = 'M'\n" +
            //"and nvl(envoi_bord,'N') = 'N'"
            "order by dat_bord desc"
            ,nativeQuery = true)
    List<BordereauProjection> getListBordEnvoiMut(@Param("codSoc")String codSoc);
    @Query(value="select * \n" +
            "from bord_envoi b \n" +
            "where b.cod_soc =:codSoc\n" +
            "and typ_bord = 'C'\n" +
            "and nvl(envoi_bord,'O') = 'O'\n"+
            "order by TO_DATE(dat_bord, 'DD/MM/YYYY') desc"
            ,nativeQuery = true)
    List<BordEnvoiPrejection> getListBordEnvoiCNAM(@Param("codSoc")String codSoc);

    @Query(value = "SELECT b.cod_bord,\n" +
            "       TO_DATE(TO_CHAR(b.dat_bord, 'dd/mm/yyyy'), 'dd/mm/yyyy') AS dat_bord,\n" +
            "       b.nbr_bult,\n" +
            "       (SELECT a.lib_assur FROM assurance a WHERE a.cod_assur = b.cod_assur) AS lib_assurance,\n" +
            "       b.dat_deb,\n" +
            "       b.dat_fin,\n" +
            "       b.tot_net,\n" +
            "       b.tot_honor\n" +
            "  FROM bord_envoi b\n" +
            " WHERE typ_bord = 'C'\n" +
            "   and nvl(envoi_bord, 'N') = 'N'\n" +
            " ORDER BY b.dat_bord DESC", nativeQuery = true)
    List<BordEnvoiProjection> EnteteBordEnvoi(@Param("soc") String soc);


    @Query(value="SELECT cod_bord, \n" +
            "       cod_assur, \n" +
            "       dat_bord, \n" +
            "       dat_deb, \n" +
            "       dat_fin, \n" +
            "       nbr_bult, \n" +
            "       tot_honor, \n" +
            "       typ_bord, \n" +
            "       cod_soc, \n" +
            "       tot_net, \n" +
            "       valid_bord, \n" +
            "       reg_bord, \n" +
            "       tot_remb, \n" +
            "       envoi_bord,\n" +
            "       (select lib_assur\n" +
            "\tfrom assurance\n" +
            "\twhere cod_assur = t.cod_assur) lib_assur\n" +
            "FROM BORD_ENVOI t\n" +
            "WHERE cod_bord = :codBord\n" +
            "AND TO_CHAR(dat_bord, 'DD/MM/YYYY') = :datBord",nativeQuery = true)
    List<BordEnvoiPrejection> getBordEnvoiMut(@Param("codBord")String codBord, @Param("datBord") LocalDate datBord);

    @Query(value = "SELECT b.cod_bord,\n" +
            "       TO_DATE(TO_CHAR(b.dat_bord, 'dd/mm/yyyy'), 'dd/mm/yyyy') AS dat_bord,\n" +
            "       b.nbr_bult,\n" +
            "       (SELECT a.lib_assur FROM assurance a WHERE a.cod_assur = b.cod_assur) AS lib_assurance,\n" +
            "       b.dat_deb,\n" +
            "       b.dat_fin,\n" +
            "       b.tot_net,\n" +
            "       b.tot_honor,b.tot_remb,b.cod_assur,b.reg_bord\n" +
            "  FROM bord_envoi b\n" +
            " WHERE typ_bord = 'M'\n" +
            "   and b.cod_soc=:soc ORDER BY b.dat_bord DESC", nativeQuery = true)
    List<BordEnvoiProjection> BordEnvoi(@Param("soc") String soc);


    @Query(value = "SELECT b.cod_bord,\n" +
            "       TO_DATE(TO_CHAR(b.dat_bord, 'dd/mm/yyyy'), 'dd/mm/yyyy') AS dat_bord,\n" +
            "       b.nbr_bult,\n" +
            "       (SELECT a.lib_assur FROM assurance a WHERE a.cod_assur = b.cod_assur) AS lib_assurance,\n" +
            "       b.dat_deb,\n" +
            "       b.dat_fin,\n" +
            "       b.tot_net,\n" +
            "       b.tot_honor,b.tot_remb,b.cod_assur\n" +
            "  FROM bord_envoi b\n" +
            " WHERE typ_bord = 'M'\n" +
            "   and b.cod_soc=:soc and nvl(VALID_BORD,'N') = 'O' ORDER BY b.dat_bord DESC", nativeQuery = true)
    List<BordEnvoiProjection> PayBordEnvoi(@Param("soc") String soc);


    @Modifying
    @Transactional
    @Query("UPDATE BordEnvoi b SET b.valid_bord = 'O' " +
            "WHERE b.cod_soc = :codSoc " +
            "AND b.cod_assur = :codAssur " +
            "AND b.cod_bord = :codBord " +
            "AND b.typ_bord = 'M'")
    int markValidBord(String codSoc, String codAssur, String codBord);
}
