package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.Personnel;
import com.arabsoft.gestion_adherent.entities.cle.ClePersonnel;
import com.arabsoft.gestion_adherent.projections.MatIntProjection;
import com.arabsoft.gestion_adherent.projections.PersonnelPrejection;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

       @Query(value = """
                     select
                            p.cod_soc,
                            p.mat_pers,
                            p.cod_assur,
                            p.nom_pers,
                            p.sexe,
                            p.cin,
                            p.dat_nais,
                            p.pren_pers,
                            p.dat_emb,
                            p.cod_sit,
                            p.nbr_enf,
                            p.cod_retr,
                            p.num_retr,
                            p.num_assur,
                            p.dat_ass,
                            p.cod_pay,
                            p.rib,
                            p.nom_pers_a,
                            p.pren_pers_a,
                            p.cod_natp,
                            p.cod_banq,
                            p.cod_agc,
                            p.dat_dece,
                            p.etat_act,
                            p.dat_motif,
                            p.cod_lieu_geog,
                            p.bas_plafond,
                            p.nom_jf,
                            p.nom_jf_a,
                            p.photo_pers,
                            p.lieu_nais,
                            p.mnt_param,
                            p.etat_prof,
                            p.dat_aff_cnam,
                            p.corps,
                            p.cod_affect,
                            p.dat_affect,
                            p.cod_typ_depart,
                            p.dat_depart,
                            p.typ_aff,
                            p.cod_user,
                            p.dat_maj,
                            p.mat_int,
                            p.pers_carte,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                            lg.lib_lieu as lib_lieu
                       from personnel p
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      where p.etat_act = 'I'
                     """, nativeQuery = true)
       List<PersonnelPrejection> getPersonnelEnInstance();

       @Query(value = """
                     select
                            p.cod_soc,
                            p.mat_pers,
                            p.cod_assur,
                            p.nom_pers,
                            p.sexe,
                            p.cin,
                            p.dat_nais,
                            p.pren_pers,
                            p.dat_emb,
                            p.cod_sit,
                            p.nbr_enf,
                            p.cod_retr,
                            p.num_retr,
                            p.num_assur,
                            p.dat_ass,
                            p.cod_pay,
                            p.rib,
                            p.nom_pers_a,
                            p.pren_pers_a,
                            p.cod_natp,
                            p.cod_banq,
                            p.cod_agc,
                            p.dat_dece,
                            p.etat_act,
                            p.dat_motif,
                            p.cod_lieu_geog,
                            p.bas_plafond,
                            p.nom_jf,
                            p.nom_jf_a,
                            p.photo_pers,
                            p.lieu_nais,
                            p.mnt_param,
                            p.etat_prof,
                            p.dat_aff_cnam,
                            p.corps,
                            p.cod_affect,
                            p.dat_affect,
                            p.cod_typ_depart,
                            p.dat_depart,
                            p.typ_aff,
                            p.cod_user,
                            p.dat_maj,
                            p.mat_int,
                            p.pers_carte,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                            lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      where p.cod_soc = :soc
                        and p.mat_pers = :mat
                     """, nativeQuery = true)
       List<PersonnelPrejection> getPersonnelByMat(@Param("soc") String mat, @Param("mat") String soc);

       @Query(value = """
                     select
                            p.cod_soc,
                            p.mat_pers,
                            p.cod_assur,
                            p.nom_pers,
                            p.sexe,
                            p.cin,
                            p.dat_nais,
                            p.pren_pers,
                            p.dat_emb,
                            p.cod_sit,
                            p.nbr_enf,
                            p.cod_retr,
                            p.num_retr,
                            p.num_assur,
                            p.dat_ass,
                            p.cod_pay,
                            p.rib,
                            p.nom_pers_a,
                            p.pren_pers_a,
                            p.cod_natp,
                            p.cod_banq,
                            p.cod_agc,
                            p.dat_dece,
                            p.etat_act,
                            p.dat_motif,
                            p.cod_lieu_geog,
                            p.bas_plafond,
                            p.nom_jf,
                            p.nom_jf_a,
                            p.photo_pers,
                            p.lieu_nais,
                            p.mnt_param,
                            p.etat_prof,
                            p.dat_aff_cnam,
                            p.corps,
                            p.cod_affect,
                            p.dat_affect,
                            p.cod_typ_depart,
                            p.dat_depart,
                            p.typ_aff,
                            p.cod_user,
                            p.dat_maj,
                            p.mat_int,
                            p.pers_carte,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                            lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      order by p.mat_pers
                     """, nativeQuery = true)
       List<PersonnelPrejection> getPersonnels();

       @Query(value = "select count(*) from personnel p where p.cod_soc = :soc and p.mat_pers = :mat ", nativeQuery = true)
       Long countCodSocAndMatPers(@Param("soc") String codSoc, @Param("mat") String matPers);

       @Query(value = """
                     select p.mat_int matInt,
                            p.mat_pers matPers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) nomPren
                       from personnel p
                      order by p.mat_int
                     """, nativeQuery = true)
       List<MatIntProjection> getMatInt();

}
