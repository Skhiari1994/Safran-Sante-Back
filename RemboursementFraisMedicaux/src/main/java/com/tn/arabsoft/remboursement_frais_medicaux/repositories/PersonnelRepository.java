package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Personnel;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePersonnel;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.AdherentProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.FamillePersonnelBultSoin;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.PersonnelBultSoinProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.PersonnelProjection;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

        @Query(value = "select p.num_retr, p.mat_pers, p.mat_int, p.dat_nais, num_assur, nom_pers||' '||pren_pers nom,\n"
                        +
                        "p.cod_assur,lib_assur,f.cod_fil,lib_fill,p.num_assur\n" +
                        "from personnel p,assurance a,ref_filliere r,pers_affil f\n" +
                        "where p.cod_soc =:soc \n" +
                        "and etat_act = 'A'\n" +
                        "and p.cod_assur = a.cod_assur\n" +
                        "and p.mat_pers = f.mat_pers\n" +
                        "and f.cod_soc =:soc \n" +
                        "and f.courant = 'O'\n" +
                        "and f.cod_fil = r.cod_fil\n" +
                        "and nvl(r.BULT_MUT,'N') = 'O'", nativeQuery = true)
        List<PersonnelBultSoinProjection> getPersonnelBultSoin(@Param("soc") String soc);

        @Query(value = "select num_fam,NOM_PREN from famille \n" +
                        "where cod_soc =:soc\n" +
                        "and mat_pers =:mat\n" +
                        "and nvl(pec,'N') = 'O'\n" +
                        "union\n" +
                        "select num_fam,NOM_PREN from famille \n" +
                        "where cod_soc =:soc\n" +
                        "and mat_pers =:mat\n" +
                        "and nvl(pec_mut,'N') = 'O'\n" +
                        "and nom_pren is not null\n" +
                        "union\n" +
                        "select 00,'adhérent' from dual", nativeQuery = true)
        List<AdherentProjection> getListAdherent(@Param("soc") String soc, @Param("mat") String mat);

        @Query(value = "select num_fam,NOM_PREN from famille \n" +
                        "where cod_soc = :soc\n" +
                        "and mat_pers = :mat\n" +
                        "and nvl(pec,'N') = 'O'\n" +
                        "union\n" +
                        "select 00,'adhérant' from dual\n", nativeQuery = true)
        List<AdherentProjection> getListAdherentDossMld(@Param("soc") String soc, @Param("mat") String mat);

        @Query(value = "select num_fam,NOM_PREN from famille \n" +
                        "            where cod_soc = :soc\n" +
                        "            and mat_pers = :mat\n" +
                        "            and nvl(pec,'N') = 'O'\n" +
                        "            and num_fam=:fam", nativeQuery = true)
        List<AdherentProjection> getListAdherentFamille(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("fam") String fam);

        /*
         * @Query(
         * value="select p.num_retr,b.mat_pers,p.nom_pers||' '||p.pren_pers nom,b.cod_fil\n"
         * +
         * "from personnel p ,bult_soin b\n" +
         * "where b.cod_soc =:soc\n" +
         * "and p.cod_soc = b.cod_soc\n" +
         * "and p.mat_pers = b.mat_pers\n" +
         * "and b.COD_BORD = :COD_BORD\n" +
         * "and b.cod_assur = :cod_assur",nativeQuery = true)
         * List<PersonnelBultSoinProjection>
         * getPersonnelBultSoinSaisie(@Param("soc")String soc,@Param("cod_assur")String
         * cod_assur);
         */
        @Query(value = "select p.num_retr, p.mat_pers,p.nom_pers||' '||p.pren_pers nom,a.cod_fil\n" +
                        " \n" +
                        "\tfrom personnel p,pers_affil a\n" +
                        "\twhere p.cod_soc = :soc\n" +
                        "\tand p.cod_soc   = a.cod_soc\n" +
                        "\tand p.mat_pers  = a.mat_pers\n" +
                        "\tand a.courant   = 'O'", nativeQuery = true)
        List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisie(@Param("soc") String soc);

        @Query(value = "select  f.num_fam,f.nom_pren nom,dat_naiss \n" +
                        "from famille f\n" +
                        "where f.cod_soc = :soc\n" +
                        "and f.mat_pers = :pers\n" +
                        "and nvl(pec_mut,'N') = 'O'\n" +
                        "and parente in ('P','M','C')\n" +
                        "union\n" +
                        "select  l.num_fam,l.nom_pren nom,dat_naiss \n" +
                        "from famille l\n" +
                        "where l.cod_soc = :soc\n" +
                        "and l.mat_pers = :pers\n" +
                        "and nvl(pec,'N') = 'O'\n" +
                        "and parente in('E')\n" +
                        "union\n" +
                        "select 0,'Adhérent' nom,dat_nais \n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and mat_pers = :pers", nativeQuery = true)
        List<FamillePersonnelBultSoin> getListFamillePersonnelBultSoinLibre(@Param("soc") String soc,
                        @Param("pers") String pers);

        @Query(value = "select p.num_retr,p.mat_pers,p.nom_pers||' '||p.pren_pers nom,a.cod_fil\n" +
                        "from personnel p ,pers_affil a\n" +
                        "where P.cod_soc = :soc\n" +
                        "and P.cod_assur = :ass\n" +
                        "and etat_act = 'A'\n" +
                        "and P.cod_soc = a.cod_soc\n" +
                        "and p.mat_pers = a.mat_pers\n" +
                        "and courant = 'O'", nativeQuery = true)
        List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieLibre(@Param("soc") String soc,
                        @Param("ass") String ass);

        @Query(value = "select p.num_retr,b.mat_pers,p.nom_pers||' '||p.pren_pers nom,b.cod_fil\n" +
                        "from personnel p ,bult_soin b\n" +
                        "where b.cod_soc = :soc\n" +
                        "and p.cod_soc = b.cod_soc\n" +
                        "and p.mat_pers = b.mat_pers\n" +
                        "and b.COD_BORD = :cod_bord\n" +
                        "and b.cod_assur = :ass", nativeQuery = true)
        List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieCnam(@Param("soc") String soc,
                        @Param("ass") String mat, @Param("cod_bord") String cod_bord);

        @Query(value = "select distinct b.num_fam,decode(b.num_fam,'0','Adhérent',f.nom_pren) nom,dat_soin,dat_naiss \n"
                        +
                        "from famille f,bult_soin b\n" +
                        "where f.cod_soc = :soc\n" +
                        "and f.cod_soc = b.cod_soc\n" +
                        "and f.mat_pers = b.mat_pers\n" +
                        "and f.num_fam = b.num_fam\n" +
                        "and b.cod_soc = :soc\n" +
                        "and b.mat_pers = :mat\n" +
                        "and b.cod_bord =  nvl(:cod_bord,b.cod_bord)\n" +
                        "union\n" +
                        "select distinct b.num_fam,decode(b.num_fam,'0','Adhérent') nom,dat_soin,dat_nais \n" +
                        "from bult_soin b,personnel p\n" +
                        "where b.cod_soc = :soc\n" +
                        "and b.mat_pers = :mat\n" +
                        "and b.cod_bord = nvl(:cod_bord,b.cod_bord)\n" +
                        "and b.num_fam = 0\n" +
                        "and b.mat_pers = p.mat_pers\n" +
                        "and b.cod_soc = p.cod_soc", nativeQuery = true)
        List<FamillePersonnelBultSoin> getPrestat(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "select p.cod_soc,\n" +
                        "       p.mat_pers,\n" +
                        "       p.cod_assur,\n" +
                        "       p.nom_pers,\n" +
                        "       p.sexe,\n" +
                        "       p.cin,\n" +
                        "       p.dat_nais,\n" +
                        "       p.pren_pers,\n" +
                        "       p.dat_emb,\n" +
                        "       p.cod_sit,\n" +
                        "       p.nbr_enf,\n" +
                        "       p.cod_retr,\n" +
                        "       p.num_retr,\n" +
                        "       p.num_assur,\n" +
                        "       p.dat_ass,\n" +
                        "       p.cod_pay,\n" +
                        "       p.rib,\n" +
                        "       p.nom_pers_a,\n" +
                        "       p.pren_pers_a,\n" +
                        "       p.cod_natp,\n" +
                        "       p.cod_banq,\n" +
                        "       p.cod_agc,\n" +
                        "       p.dat_dece,\n" +
                        "       p.etat_act,\n" +
                        "       p.dat_motif,\n" +
                        "       p.cod_lieu_geog,\n" +
                        "       p.bas_plafond,\n" +
                        "       p.nom_jf,\n" +
                        "       p.nom_jf_a,\n" +
                        "       p.photo_pers,\n" +
                        "       p.lieu_nais,\n" +
                        "       p.mnt_param,\n" +
                        "       p.etat_prof,\n" +
                        "       p.dat_aff_cnam,\n" +
                        "       p.corps,\n" +
                        "       p.cod_affect,\n" +
                        "       p.dat_affect,\n" +
                        "       p.cod_typ_depart,\n" +
                        "       p.dat_depart,\n" +
                        "       p.typ_aff,\n" +
                        "       p.cod_user,\n" +
                        "       p.dat_maj,\n" +
                        "       p.mat_int,\n" +
                        "       p.pers_carte,(select LIB_ASSUR \n" +
                        "from assurance \n" +
                        "where COD_ASSUR = p.cod_assur)lib_assur from personnel p where etat_act='A'", nativeQuery = true)
        List<PersonnelProjection> getPersActif();

        @Query(value = "select mat_pers, mat_int, dat_nais, num_retr, nom_pers||' '||pren_pers nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and etat_act = 'A'", nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamDeb(@Param("soc") String soc);

        @Query(value = "select mat_pers, mat_int, dat_nais, num_retr, nom_pers||' '||pren_pers nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and etat_act = 'A'", nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamFin(@Param("soc") String soc);

}
