package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleEtatPretPers;
import com.arabsoft.Credits.Entities.EtatPretPers;
import com.arabsoft.Credits.Projections.EtatPretPersProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EtatPretPersRepository extends JpaRepository<EtatPretPers,CleEtatPretPers> {
    @Query(value="select nvl(max(nvl(num_etat_pret,0))+1,1)   \n" +
            "          from etat_pret_pers  \n" +
            "    where cod_soc =:soc",nativeQuery = true)
    Long getnuEtatPret(@Param("soc")String soc);

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_etat_pret,\n" +
            "       t.dat_effet,\n" +
            "       t.nat_etat_pret,\n" +
            "       t.type_anticip,\n" +
            "       t.dat_debut_etat,\n" +
            "       t.dat_fin_etat,\n" +
            "       t.mnt_anticipe,\n" +
            "       t.mnt_capital,\n" +
            "       t.mnt_interet,\n" +
            "       t.int_grace,\n" +
            "       t.mode_payement,\n" +
            "       t.num_piece,\n" +
            "       t.dat_piece,\n" +
            "       t.cod_banq,\n" +
            "       t.cod_agc,\n" +
            "       t.nom_emet_piece,\n" +
            "       t.obs_etat,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.inject,\n" +
            "       t.num_mvt,\n" +
            "       t.dat_mvt,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.num_vir,\n" +
            "       (select p.nom_pers ||' '|| p.pren_pers  from personnel p where p.mat_pers=t.mat_pers)nom from etat_pret_pers t where nat_etat_pret in ('AT','AP') and \n" +
            "COD_GRP_PRET  in (select COD_GRP_PRET from groupe_pret \n" +
            "where TYP_GROUPE ='P')",nativeQuery = true)
    List<EtatPretPersProjection> getEtatPretPers();

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_etat_pret,\n" +
            "       t.dat_effet,\n" +
            "       t.nat_etat_pret,\n" +
            "       t.type_anticip,\n" +
            "       t.dat_debut_etat,\n" +
            "       t.dat_fin_etat,\n" +
            "       t.mnt_anticipe,\n" +
            "       t.mnt_capital,\n" +
            "       t.mnt_interet,\n" +
            "       t.int_grace,\n" +
            "       t.mode_payement,\n" +
            "       t.num_piece,\n" +
            "       t.dat_piece,\n" +
            "       t.cod_banq,\n" +
            "       t.cod_agc,\n" +
            "       t.nom_emet_piece,\n" +
            "       t.obs_etat,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.inject,\n" +
            "       t.num_mvt,\n" +
            "       t.dat_mvt,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.num_vir,\n" +
            "       (select p.nom_pers ||' '|| p.pren_pers  from personnel p where p.mat_pers=t.mat_pers)nom from etat_pret_pers t where nat_etat_pret in ('ST','SP') ",nativeQuery = true)
    List<EtatPretPersProjection> getEtatPretPersSusp();


}
