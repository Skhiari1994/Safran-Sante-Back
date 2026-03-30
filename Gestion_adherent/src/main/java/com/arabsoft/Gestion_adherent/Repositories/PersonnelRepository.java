package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.Cle.ClePersonnel;
import com.arabsoft.Gestion_adherent.Entities.Personnel;
import com.arabsoft.Gestion_adherent.Projections.AffilPersProjection;
import com.arabsoft.Gestion_adherent.Projections.MatIntProjection;
import com.arabsoft.Gestion_adherent.Projections.PersonnelPrejection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

@Query(value="select p.cod_soc,p.mat_pers, p.cod_assur, p.nom_pers,p.sexe,p.cin,p.dat_nais,p.pren_pers,\n" +
        "       p.dat_emb, p.cod_sit,p.nbr_enf, p.cod_retr,p.num_retr,p.num_assur,p.dat_ass,p.cod_pay,\n" +
        "       p.rib, p.nom_pers_a,p.pren_pers_a,p.cod_natp,p.cod_banq,p.cod_agc,p.dat_dece,p.etat_act,\n" +
        "       p.dat_motif, p.cod_lieu_geog, p.bas_plafond, p.nom_jf,p.nom_jf_a,p.photo_pers, p.lieu_nais,\n" +
        "       p.mnt_param,  p.etat_prof,p.dat_aff_cnam,p.corps, p.cod_affect, p.dat_affect,p.cod_typ_depart,\n" +
        "       p.dat_depart,p.typ_aff,p.cod_user,p.dat_maj, p.mat_int,p.pers_carte,(p.pren_pers ||' '||p.nom_pers)full_name,(select lib_lieu from PRM_LIEU_GEOGRAPHIQUE where cod_lieu_geog=p.cod_lieu_geog)lib_lieu\n" +
        "  from personnel p where etat_act='I'",nativeQuery = true)
List<PersonnelPrejection> getPersonnelEnInstance();

    @Query(value="select p.cod_soc,p.mat_pers, p.cod_assur, p.nom_pers,p.sexe,p.cin,p.dat_nais,p.pren_pers,\n" +
            "       p.dat_emb, p.cod_sit,p.nbr_enf, p.cod_retr,p.num_retr,p.num_assur,p.dat_ass,p.cod_pay,\n" +
            "       p.rib, p.nom_pers_a,p.pren_pers_a,p.cod_natp,p.cod_banq,p.cod_agc,p.dat_dece,p.etat_act,\n" +
            "       p.dat_motif, p.cod_lieu_geog, p.bas_plafond, p.nom_jf,p.nom_jf_a,p.photo_pers, p.lieu_nais,\n" +
            "       p.mnt_param,  p.etat_prof,p.dat_aff_cnam,p.corps, p.cod_affect, p.dat_affect,p.cod_typ_depart,\n" +
            "       p.dat_depart,p.typ_aff,p.cod_user,p.dat_maj, p.mat_int,p.pers_carte,(p.pren_pers ||' '||p.nom_pers)full_name,(select lib_lieu from PRM_LIEU_GEOGRAPHIQUE where cod_lieu_geog=p.cod_lieu_geog)libLieu\n" +
            "  from personnel p where p.mat_pers=:mat",nativeQuery = true)
    List<PersonnelPrejection> getPersonnelByMat(@Param("mat") String mat);
    @Query(value="select p.cod_soc,p.mat_pers, p.cod_assur, p.nom_pers,p.sexe,p.cin,p.dat_nais,p.pren_pers,\n" +
            "       p.dat_emb, p.cod_sit,p.nbr_enf, p.cod_retr,p.num_retr,p.num_assur,p.dat_ass,p.cod_pay,\n" +
            "       p.rib, p.nom_pers_a,p.pren_pers_a,p.cod_natp,p.cod_banq,p.cod_agc,p.dat_dece,p.etat_act,\n" +
            "       p.dat_motif, p.cod_lieu_geog, p.bas_plafond, p.nom_jf,p.nom_jf_a,p.photo_pers, p.lieu_nais,\n" +
            "       p.mnt_param,  p.etat_prof,p.dat_aff_cnam,p.corps, p.cod_affect, p.dat_affect,p.cod_typ_depart,\n" +
            "       p.dat_depart,p.typ_aff,p.cod_user,p.dat_maj, p.mat_int,p.pers_carte,(p.pren_pers ||' '||p.nom_pers)full_name,(select lib_lieu from PRM_LIEU_GEOGRAPHIQUE where cod_lieu_geog=p.cod_lieu_geog)libLieu\n" +
            "  from personnel p order by \n" +
            "  case \n" +
            "    when regexp_like(p.mat_pers, '^\\d+$') then to_number(p.mat_pers)\n" +
            "    else null\n" +
            "  end\n",nativeQuery = true)
    List<PersonnelPrejection> getPersonnels();



//    @Query(value="    select count(*)  from personnel\n" +
//            "    where cod_soc =:soc\n" +
//            "    and mat_pers =:mat",nativeQuery = true)
//    Long count(@Param("mat") String mat);
@Query(value="select count(*) from personnel where  cod_soc=:soc and mat_pers=:mat ",nativeQuery = true)
    Long countCodSocAndMatPers(@Param("soc") String codSoc, @Param("mat") String matPers);
    @Query(value="SELECT ALL P.MAT_INT matInt, P.MAT_PERS matPers, P.NOM_PERS||' '||P.PREN_PERS nomPren\n" +
            "FROM PERSONNEL P \n" +
            "order by p.mat_int",nativeQuery = true)
    List<MatIntProjection> getMatInt();


}
