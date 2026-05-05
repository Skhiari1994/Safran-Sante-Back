package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Cle_PERS_VALIDEUR;
import com.tn.arabsoft.administrations.entities.PERS_VALIDEUR;
import com.tn.arabsoft.administrations.projections.PersValideurProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SuppressWarnings({ "java:S100", "java:S101", "java:S114", "java:S116", "java:S117" })
public interface PERS_VALIDEUR_Dao extends JpaRepository<PERS_VALIDEUR, Cle_PERS_VALIDEUR> {
    @Query(value = "select p.cod_soc,p.mat_pers,p.mat_resp,p.niveau,(select pa.nom_pers||' '||pa.pren_pers from personnel pa  where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_pers)nom,(select pa.nom_pers||' '||pa.pren_pers from personnel pa  where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_resp)nomResp from PERS_VALIDEUR p where p.cod_soc=:x and  p.mat_pers=:y", nativeQuery = true)
    public List<PersValideurProjection> getall(@Param("x") String cod, @Param("y") String mat);

    @Query(value = "select p.cod_soc,p.mat_pers,p.mat_resp,p.niveau,(select pa.nom_pers||' '||pa.pren_pers from personnel pa  where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_pers)nom,(select pa.nom_pers||' '||pa.pren_pers from personnel pa  where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_resp)nomResp from PERS_VALIDEUR p where   p.mat_pers=:y", nativeQuery = true)
    public List<PersValideurProjection> matchef(@Param("y") String mat);

    @Query(value = "select p.cod_soc,p.mat_pers,p.mat_resp,p.niveau,(select pa.nom_pers||' '||pa.pren_pers from personnel pa "
            + " where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_pers)nom,(select pa.nom_pers||' '||pa.pren_pers from personnel pa  "
            + "where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_resp)nomResp from PERS_VALIDEUR p where   p.mat_resp=:matChef and p.mat_pers=:mat", nativeQuery = true)
    public PersValideurProjection matchefbyniv(@Param("matChef") String matChef, @Param("mat") String mat);

    @Query(value = "select p.cod_soc,p.mat_pers,p.mat_resp,p.niveau,(select pa.nom_pers||' '||pa.pren_pers from personnel pa "
            + " where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_pers)nom,(select pa.nom_pers||' '||pa.pren_pers from personnel pa "
            + " where pa.cod_soc=p.cod_soc and pa.mat_pers=p.mat_resp)nomResp from PERS_VALIDEUR p ", nativeQuery = true)
    public List<PersValideurProjection> getPersValid();

    @Query(value = "insert into pers_valideur (cod_soc,mat_pers,mat_resp,niveau) values(:soc,:mat,:mat_resp,:niv)", nativeQuery = true)
    public void addPersValid(@Param("soc") String soc, @Param("mat") String mat, @Param("mat_resp") String mat_resp,
            @Param("niv") Long niv);

    @Query(value = "update pers_valideur set niveau=:niv where cod_soc=:soc and mat_pers=:mat and mat_resp=:mat_resp", nativeQuery = true)
    public void updatePersValid(@Param("soc") String soc, @Param("mat") String mat, @Param("mat_resp") String mat_resp,
            @Param("niv") Long niv);

    @Modifying
    @Query(value = "DELETE FROM pers_valideur WHERE cod_soc = :soc AND mat_pers = :mat AND mat_resp = :mat_resp", nativeQuery = true)
    void DeletePersValideur(@Param("soc") String soc, @Param("mat") String mat, @Param("mat_resp") String mat_resp);

    @Query(value = "commit", nativeQuery = true)
    public void commit();
}
