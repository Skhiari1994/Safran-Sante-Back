package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.AffilMutuelle;
import com.arabsoft.Gestion_adherent.Entities.Cle.cleAffilMutuell;

import com.arabsoft.Gestion_adherent.Projections.AffilPersProjection;
import com.arabsoft.Gestion_adherent.Projections.AffilPersValid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AffilMutuelleRepository extends JpaRepository<AffilMutuelle,cleAffilMutuell> {

    @Query(value="select * from affil_mutuelle order by mat_pers",nativeQuery = true)
    List<AffilMutuelle> getAffilMutuelle();

    @Query(value="select mat_pers, cin,nom_pers,pren_pers,etat_act,dat_emb,cod_sit,nbr_enf,num_assur,corps\n" +
            "from personnel\n" +
            "where cod_soc =:soc\n" +
            "and nvl(typ_aff,'S')='A'\n" +
            "order by mat_pers" ,nativeQuery = true)
    List<AffilPersProjection> getAffilPers(@Param("soc") String soc);
    @Query(value="select mat_pers, cin,nom_pers,pren_pers,etat_act,dat_emb,cod_sit,nbr_enf,num_assur,corps\n" +
            "from personnel\n" +
            "where cod_soc = :soc\n" +
            "and nvl(typ_aff,'S')='S'\n" +
            "order by mat_pers" ,nativeQuery = true)
    List<AffilPersProjection> getAffilPersMut(@Param("soc") String soc);
    @Query(value="select mat_pers, cin,nom_pers,pren_pers,etat_act,dat_emb,cod_sit,nbr_enf,num_assur,corps,cod_affect,cod_typ_depart\n" +
            "from personnel\n" +
            "where cod_soc =:soc\n" +
            "and mat_pers=:mat \n" +
            "and nvl(typ_aff,'S')='A'\n" +
            "order by mat_pers" ,nativeQuery = true)
    AffilPersProjection getAffilPersByMat(@Param("soc") String soc,@Param("mat") String mat);

    @Query(value="select * from affil_mutuelle\n" +
             "where cod_soc =:soc\n" +
            "and  typ_aff ='A'\n" +
            "and  etat_aff ='I'\n" +
            "order by mat_pers,dat_ass " ,nativeQuery = true)
    List<AffilMutuelle> getAffilPersRenouvAdhesion(@Param("soc") String soc);
    @Query(value="select t.cod_soc,\n" +
            "       (select p.nom_pers ||' '||p.pren_pers from personnel p where p.cod_soc=t.cod_soc and p.mat_pers=t.mat_pers)nompren,\n" +
            "       t.mat_pers,\n" +
            "       t.dat_ass,\n" +
            "       t.num_assur,\n" +
            "       t.typ_aff,\n" +
            "       t.dat_dem,\n" +
            "       t.obs_aff,\n" +
            "       t.coef_cot,\n" +
            "       t.etat_aff,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.cod_affect from affil_mutuelle t where cod_soc =:soc and nvl(etat_aff,'I' )='I'\n" +
            "       order by mat_pers,dat_ass" ,nativeQuery = true)
    List<AffilPersValid> getAffilPersEnInstance(@Param("soc") String soc);


    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.dat_ass,\n" +
            "       t.num_assur,\n" +
            "       t.typ_aff,\n" +
            "       t.dat_dem,\n" +
            "       t.obs_aff,\n" +
            "       t.coef_cot,\n" +
            "       t.etat_aff,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.cod_affect from affil_mutuelle t where cod_soc =:soc and nvl(etat_aff,'I' )='I' and mat_pers=:mat and dat_ass=:dat_ass\n" +
            "       order by mat_pers,dat_ass" ,nativeQuery = true)
    AffilMutuelle getAffilPersAffiliation(@Param("soc") String soc,@Param("mat") String mat,@Param("dat_ass") LocalDate dat_ass);

 }
