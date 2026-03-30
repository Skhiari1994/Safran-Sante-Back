package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleVirAnticip;
import com.arabsoft.Credits.Entities.VirAnticip;
import com.arabsoft.Credits.Projections.VirAnticipProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VirAnticipRepository extends JpaRepository<VirAnticip, CleVirAnticip> {

    @Query(value="select nvl(max(nvl(num_vir,0)),0)+1 from vir_anticip where mat_pers=:mat",nativeQuery = true)
    Long getMaxNumVir(@Param("mat") String mat);

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_vir,\n" +
            "       t.dat_anticip,\n" +
            "       t.mont_vir,\n" +
            "       t.mont_antic,\n" +
            "       t.rest_vir,\n" +
            "       t.etat_vir,\n" +
            "       t.imput_cpt,\n" +
            "       t.seq_ecrt,\n" +
            "       t.ref_metier,\n" +
            "       t.obs_metier,\n" +
            "       t.mnt_esp,\n" +
            "       (select p.nom_pers ||' '||p.pren_pers from personnel p where p.mat_pers=t.mat_pers)nom  from VIR_ANTICIP t",nativeQuery = true)
    List<VirAnticipProjection> getAllVirAnticip();
    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_vir,\n" +
            "       t.dat_anticip,\n" +
            "       t.mont_vir,\n" +
            "       t.mont_antic,\n" +
            "       t.rest_vir,\n" +
            "       t.etat_vir,\n" +
            "       t.imput_cpt,\n" +
            "       t.seq_ecrt,\n" +
            "       t.ref_metier,\n" +
            "       t.obs_metier,\n" +
            "       t.mnt_esp,\n" +
            "       (select p.nom_pers ||' '||p.pren_pers from personnel p where p.mat_pers=t.mat_pers)nom  from VIR_ANTICIP t where t.etat_vir='N'",nativeQuery = true)
    List<VirAnticipProjection> getVirAnticipInst();

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_vir,\n" +
            "       t.dat_anticip,\n" +
            "       t.mont_vir,\n" +
            "       t.mont_antic,\n" +
            "       t.rest_vir,\n" +
            "       t.etat_vir,\n" +
            "       t.imput_cpt,\n" +
            "       t.seq_ecrt,\n" +
            "       t.ref_metier,\n" +
            "       t.obs_metier,\n" +
            "       t.mnt_esp,\n" +
            "       (select p.nom_pers ||' '||p.pren_pers from personnel p where p.mat_pers=t.mat_pers)nom  from VIR_ANTICIP t where  t.mat_pers=:mat ",nativeQuery = true)
    List<VirAnticipProjection> getVirAnticipByMat(@Param("mat") String mat);

    @Query(value = "SELECT t.cod_soc, " +
            "       t.mat_pers, " +
            "       t.num_vir, " +
            "       t.dat_anticip, " +
            "       t.mont_vir, " +
            "       t.mont_antic, " +
            "       t.rest_vir, " +
            "       t.etat_vir, " +
            "       t.imput_cpt, " +
            "       t.seq_ecrt, " +
            "       t.ref_metier, " +
            "       t.obs_metier, " +
            "       t.mnt_esp, " +
            "       (SELECT p.nom_pers ||' '||p.pren_pers FROM personnel p WHERE p.mat_pers=t.mat_pers) nom, " +
            "       t.rowid " +
            "FROM VIR_ANTICIP t " +
            "WHERE t.cod_soc = :codSoc AND t.mat_pers = :matPers",
            nativeQuery = true)
    List<VirAnticipProjection> getVirementsByMat(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers
    );
}
