package com.arabsoft.gestionconvention.Repositories;

import com.arabsoft.gestionconvention.Entities.Cle.CleOffDemandeConv;
import com.arabsoft.gestionconvention.Entities.OffDemandeConv;
import com.arabsoft.gestionconvention.Projections.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.arabsoft.gestionconvention.Projections.OffDemandeConvProjection;

import java.util.List;

public interface OffDemandeConvRepository extends JpaRepository<OffDemandeConv,CleOffDemandeConv> {



    @Query(value="select cod_off,lib_off,mnt_off\n" +
            "from off_conv\n" +
            "where cod_conv = :codConv\n" +
            "order by cod_off",nativeQuery = true)
    List<OffConvProjection> getLisOffConv(@Param("codConv") String codConv );



    @Query (value="select cod_conv,\n" +
            "       cod_soc,\n" +
            "       mat_pers,\n" +
            "       cod_off,\n" +
            "       dat_off_dem,\n" +
            "       dat_fin_off,\n" +
            "       mnt_off,\n" +
            "       etat_off_dem,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       seq,\n" +
            "       dat_susp,\n" +
            "       num_tel,\n" +
            "       cod_mot_susp,\n" +
            "       renouv,\n" +
            "       dat_renouv,\n" +
            "       obs_off\n" +
            "  from OFF_DEMANDE_CONV\n" +
            " where cod_conv = :codConv\n" +
            "   and cod_soc = :codSoc\n" +
            "   and mat_pers = :matPers\n" +
            "   and etat_off_dem = 'I'",nativeQuery=true)
    List<OffDemandeConv> getListDemOff(@Param("codConv") String codConv, @Param("codSoc") String codSoc, @Param("matPers") String matPers);

    @Query (value="select cod_conv,\n" +
            "       cod_soc,\n" +
            "       mat_pers,\n" +
            "       cod_off,\n" +
            "       dat_off_dem,\n" +
            "       dat_fin_off,\n" +
            "       mnt_off,\n" +
            "       etat_off_dem,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       seq,\n" +
            "       dat_susp,\n" +
            "       num_tel,\n" +
            "       cod_mot_susp,\n" +
            "       renouv,\n" +
            "       dat_renouv,\n" +
            "       obs_off\n" +
            "  from off_DEMANDE_CONV\n" +
            " where cod_soc = :codSoc\n" +
            "   and mat_pers = :matPers\n" ,nativeQuery=true)
    List<DemondeConvProjection> getListDemOffPers(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value="select t.cod_conv,\n" +
            "       t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_off,\n" +
            "       t.dat_off_dem,\n" +
            "       t.dat_fin_off,\n" +
            "       t.mnt_off,\n" +
            "       t.etat_off_dem,\n" +
            "       t.cod_user,\n" +
            "       t.dat_saisie,\n" +
            "       t.seq,\n" +
            "       t.dat_susp,\n" +
            "       t.num_tel,\n" +
            "       t.cod_mot_susp,\n" +
            "       t.renouv,\n" +
            "       t.dat_renouv,\n" +
            "       t.obs_off,\n" +
            "       (select o.lib_off\n" +
            "          from off_conv o\n" +
            "         where o.cod_conv = t.cod_conv\n" +
            "           and o.cod_off = t.cod_off) lib_off,\n" +
            "       (select LIB_MOT_SUSP\n" +
            "          from MOTIF_SUSP_CONV\n" +
            "         where t.cod_mot_susp = cod_mot_susp) lib_mot\n" +
            "  from off_demande_conv t\n" +
            " where t.cod_conv = :conv\n" +
            "   and t.mat_pers = :mat\n",nativeQuery = true)
    List<OffDemandeConvProjection> getOffDemandeConv(@Param("conv") String conv,@Param("mat") String mat);





    @Query(value="select cod_conv,lib_conv from convention",nativeQuery = true)
    List<CodConvProjection> getListCodConv();
}
