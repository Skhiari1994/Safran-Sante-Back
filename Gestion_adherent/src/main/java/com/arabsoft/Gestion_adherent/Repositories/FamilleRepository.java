package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.Famille;
import com.arabsoft.gestion_adherent.entities.cle.CleFamille;
import com.arabsoft.gestion_adherent.projections.FamilleProjection;

import java.util.List;

public interface FamilleRepository extends JpaRepository<Famille, CleFamille> {

   @Query(value = """
         select t.cod_soc,
                t.mat_pers,
                t.num_fam,
                t.parente,
                t.nom_pren,
                t.dat_naiss,
                t.sexe,
                t.cod_sit,
                t.handicap,
                t.cod_activite,
                t.dat_dece,
                t.pec,
                t.dat_pec,
                t.dat_mar,
                t.nom_jf,
                t.num_ass_conj,
                t.mat_pers_conj,
                t.pec_mut,
                t.dat_pec_mut
           from famille t
          where cod_soc = :codSoc
            and mat_pers = :matPers
            and parente = 'M'
         """, nativeQuery = true)
   public Famille getMere(@Param("codSoc") String codSoc,
         @Param("matPers") String matPers);

   @Query(value = """
         select t.cod_soc,
                t.mat_pers,
                t.num_fam,
                t.parente,
                t.nom_pren,
                t.dat_naiss,
                t.sexe,
                t.cod_sit,
                t.handicap,
                t.cod_activite,
                t.dat_dece,
                t.pec,
                t.dat_pec,
                t.dat_mar,
                t.nom_jf,
                t.num_ass_conj,
                t.mat_pers_conj,
                t.pec_mut,
                t.dat_pec_mut
           from famille t
          where cod_soc = :codSoc
            and mat_pers = :matPers
            and parente = 'P'
         """, nativeQuery = true)
   public Famille getPere(@Param("codSoc") String codSoc,
         @Param("matPers") String matPers);

   @Query(value = """
         select t.cod_soc,
                t.mat_pers,
                t.num_fam,
                t.parente,
                t.nom_pren,
                t.dat_naiss,
                t.sexe,
                t.cod_sit,
                t.handicap,
                t.cod_activite,
                t.dat_dece,
                t.pec,
                t.dat_pec,
                t.dat_mar,
                t.nom_jf,
                t.num_ass_conj,
                t.mat_pers_conj,
                t.pec_mut,
                t.dat_pec_mut
           from famille t
          where cod_soc = :codSoc
            and mat_pers = :matPers
            and parente = 'C'
         """, nativeQuery = true)
   public Famille getConjoint(@Param("codSoc") String codSoc,
         @Param("matPers") String matPers);

   @Query(value = """
         select t.cod_soc,
                t.mat_pers,
                t.num_fam,
                t.parente,
                t.nom_pren,
                t.dat_naiss,
                t.sexe,
                t.cod_sit,
                t.handicap,
                t.cod_activite,
                t.dat_dece,
                t.pec,
                t.dat_pec,
                t.dat_mar,
                t.nom_jf,
                t.num_ass_conj,
                t.mat_pers_conj,
                t.pec_mut,
                t.dat_pec_mut
           from famille t
          where cod_soc = :codSoc
            and mat_pers = :matPers
         """, nativeQuery = true)
   public List<Famille> getFamille(@Param("codSoc") String codSoc,
         @Param("matPers") String matPers);

   @Query(value = """
         select t.cod_soc cod_soc,
                t.mat_pers mat_pers,
                t.num_fam num_fam,
                t.parente parente,
                t.nom_pren nom_pren,
                t.dat_naiss dat_naiss,
                t.sexe sexe,
                t.cod_sit cod_sit,
                t.handicap handicap,
                t.cod_activite cod_activite,
                t.dat_dece dat_dece,
                t.pec pec,
                t.dat_pec dat_pec,
                t.dat_mar dat_mar,
                t.nom_jf nom_jf,
                t.num_ass_conj num_ass_conj,
                t.mat_pers_conj mat_pers_conj,
                t.pec_mut pec_mut,
                t.dat_pec_mut dat_pec_mut,
                (select lib_activite
                   from activite_famille
                  where cod_activite = t.cod_activite) lib_activite
           from famille t
          where cod_soc = :soc
            and mat_pers = :mat
            and parente = 'E'
         """, nativeQuery = true)
   List<FamilleProjection> getEnfants(@Param("soc") String codSoc,
         @Param("mat") String matPers);

   @Query(value = "select max(f.num_fam) from famille f where f.cod_soc = :codSoc and f.mat_pers = :mat and f.parente ='E'", nativeQuery = true)
   public Long getmaxEnfant(@Param("codSoc") String codSoc, @Param("mat") String mat);

   @Query(value = "delete from famille f where f.cod_soc = :soc and f.num_fam = :num and f.mat_pers = :mat", nativeQuery = true)
   public void deleteFamille(@Param("soc") String soc, @Param("num") Long num, @Param("mat") String mat);

   @Query(value = "select count(*) from famille where cod_soc = :soc and mat_pers = :mat and parente = 'E'", nativeQuery = true)
   Long countNbreEnf(@Param("soc") String soc, @Param("mat") String mat);

}
