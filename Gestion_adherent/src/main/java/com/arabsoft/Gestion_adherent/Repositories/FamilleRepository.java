package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.Cle.FAMILLEiD;
import com.arabsoft.Gestion_adherent.Entities.Famille;
import com.arabsoft.Gestion_adherent.Projections.FamilleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FamilleRepository  extends JpaRepository<Famille, FAMILLEiD> {

    @Query(value="select t.cod_soc,\n" +
            "        t.mat_pers,\n" +
            "        t.num_fam,\n" +
            "        t.parente,\n" +
            "        t.nom_pren,\n" +
            "        t.dat_naiss,\n" +
            "        t.sexe,\n" +
            "        t.cod_sit,\n" +
            "        t.handicap,\n" +
            "        t.cod_activite,\n" +
            "        t.dat_dece,\n" +
            "        t.pec,\n" +
            "        t.dat_pec,\n" +
            "        t.dat_mar,\n" +
            "        t.nom_jf,\n" +
            "        t.num_ass_conj,\n" +
            "        t.mat_pers_conj,\n" +
            "        t.pec_mut,\n" +
            "        t.dat_pec_mut"
            + "	from famille t\r\n"
            + "	where cod_soc = :codSoc and mat_pers = :matPers and parente ='M' ",nativeQuery = true)
    public Famille getMere(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value="select t.cod_soc,\n" +
            "        t.mat_pers,\n" +
            "        t.num_fam,\n" +
            "        t.parente,\n" +
            "        t.nom_pren,\n" +
            "        t.dat_naiss,\n" +
            "        t.sexe,\n" +
            "        t.cod_sit,\n" +
            "        t.handicap,\n" +
            "        t.cod_activite,\n" +
            "        t.dat_dece,\n" +
            "        t.pec,\n" +
            "        t.dat_pec,\n" +
            "        t.dat_mar,\n" +
            "        t.nom_jf,\n" +
            "        t.num_ass_conj,\n" +
            "        t.mat_pers_conj,\n" +
            "        t.pec_mut,\n" +
            "        t.dat_pec_mut"
            + "	from famille t\r\n"
            + "	where cod_soc = :codSoc and mat_pers = :matPers and parente ='P' ",nativeQuery = true)
    public Famille getPere(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value="select t.cod_soc,\n" +
            "        t.mat_pers,\n" +
            "        t.num_fam,\n" +
            "        t.parente,\n" +
            "        t.nom_pren,\n" +
            "        t.dat_naiss,\n" +
            "        t.sexe,\n" +
            "        t.cod_sit,\n" +
            "        t.handicap,\n" +
            "        t.cod_activite,\n" +
            "        t.dat_dece,\n" +
            "        t.pec,\n" +
            "        t.dat_pec,\n" +
            "        t.dat_mar,\n" +
            "        t.nom_jf,\n" +
            "        t.num_ass_conj,\n" +
            "        t.mat_pers_conj,\n" +
            "        t.pec_mut,\n" +
            "        t.dat_pec_mut"
            + "	from famille t \r\n"
            + "	where cod_soc = :codSoc and mat_pers = :matPers and parente ='C' ",nativeQuery = true)
    public Famille getConjoint(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value="select t.cod_soc,\n" +
            "        t.mat_pers,\n" +
            "        t.num_fam,\n" +
            "        t.parente,\n" +
            "        t.nom_pren,\n" +
            "        t.dat_naiss,\n" +
            "        t.sexe,\n" +
            "        t.cod_sit,\n" +
            "        t.handicap,\n" +
            "        t.cod_activite,\n" +
            "        t.dat_dece,\n" +
            "        t.pec,\n" +
            "        t.dat_pec,\n" +
            "        t.dat_mar,\n" +
            "        t.nom_jf,\n" +
            "        t.num_ass_conj,\n" +
            "        t.mat_pers_conj,\n" +
            "        t.pec_mut,\n" +
            "        t.dat_pec_mut"
            + "	from famille t \r\n"
            + "	where cod_soc = :codSoc and mat_pers = :matPers",nativeQuery = true)
    public List<Famille> getFamille(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value="select T.COD_SOC cod_soc,\n" +
            "       T.MAT_PERS mat_pers,\n" +
            "       T.NUM_FAM num_fam,\n" +
            "       T.PARENTE parente,\n" +
            "       T.NOM_PREN nom_pren,\n" +
            "       T.DAT_NAISS dat_naiss,\n" +
            "       T.SEXE sexe,\n" +
            "       T.COD_SIT cod_sit,\n" +
            "       T.HANDICAP,\n" +
            "       T.COD_ACTIVITE cod_activite,\n" +
            "       T.DAT_DECE dat_dece,\n" +
            "       T.PEC,\n" +
            "       T.DAT_PEC dat_pec,\n" +
            "       T.DAT_MAR dat_mar,\n" +
            "       T.NOM_JF nom_jf,\n" +
            "       T.NUM_ASS_CONJ num_ass_conj,\n" +
            "       T.MAT_PERS_CONJ mat_pers_conj,\n" +
            "       T.PEC_MUT pec_mut,\n" +
            "       T.DAT_PEC_MUT dat_pec_mut,(select lib_activite from activite_famille where t.cod_activite=cod_activite)lib_activite from famille T where cod_soc=:soc and mat_pers=:mat and parente='E'",nativeQuery = true)
    List<FamilleProjection> getEnfants(@Param("soc") String codSoc, @Param("mat") String matPers);


    @Query(value="select max(f.num_fam) from Famille f where f.parente ='E' and cod_soc =:codSoc and mat_pers =:mat ",nativeQuery=true)
    public Long getmaxEnfant(@Param("codSoc") String codSoc,@Param("mat") String mat);

    @Query(value="delete from Famille f where cod_soc=:soc and f.num_fam=:num and f.mat_pers=:mat",nativeQuery=true)
    public void deleteFamille(@Param("soc")String soc ,@Param("num")Long num ,@Param("mat") String mat);

    @Query(value="select count(*) from famille where cod_soc=:soc and mat_pers=:mat and parente='E'",nativeQuery = true)
    Long countNbreEnf(@Param("soc")String soc ,@Param("mat")String mat);
}
