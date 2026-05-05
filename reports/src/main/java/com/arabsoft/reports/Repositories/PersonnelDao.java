package com.arabsoft.reports.repositories;

import com.arabsoft.reports.entities.Personnel;
import com.arabsoft.reports.entities.cle.ClePersonnel;
import com.arabsoft.reports.projections.PersonnelProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface PersonnelDao extends JpaRepository<Personnel, ClePersonnel> {

        @Query(value = """
                        select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom
                        from personnel
                        where cod_soc = :soc
                        and etat_act = 'A'
                        and to_number(mat_pers) <= nvl(:mat_pers, to_number(mat_pers))
                        order by to_number(mat_pers)
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersonnel(@Param("soc") String soc, @Param("mat_pers") String mat_pers);

        @Query(value = """
                        select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom
                        from personnel
                        where cod_soc = :soc
                        and mat_pers in (select mat_pers from bult_soin
                                         where cod_soc = :soc and ANN_PLAF_IMP = :annee)
                        or mat_pers in (select mat_pers from bult_arriver
                                         where cod_soc = :soc and ANN_PLAF_IMP = :annee)
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersRecap(@Param("soc") String soc, @Param("annee") String annee);

        @Query(value = """
                        select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom
                        from personnel
                        where cod_soc = :soc
                        and etat_act = 'A'
                        and to_number(mat_pers) <= nvl(to_number(:mat_fin), to_number(mat_pers))
                        and mat_pers in (select mat_pers from bult_soin
                                         where cod_soc = :soc
                                         and nat_bult = 'M'
                                         and cod_bord is null
                                         and nvl(envoi, 'N') = 'N')
                        order by to_number(mat_pers)
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultMutDeb(@Param("soc") String soc, @Param("mat_fin") String mat_fin);

        @Query(value = """
                        select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom
                        from personnel
                        where cod_soc = :soc
                        and etat_act = 'A'
                        and to_number(mat_pers) >= nvl(to_number(:mat_deb), to_number(mat_pers))
                        and mat_pers in (select mat_pers from bult_soin
                                         where cod_soc = :soc
                                         and nat_bult = 'M'
                                         and cod_bord is null
                                         and nvl(envoi, 'N') = 'N')
                        order by to_number(mat_pers)
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultMutFin(@Param("soc") String soc, @Param("mat_deb") String mat_fin);

        @Query(value = """
                        select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom
                        from personnel
                        where cod_soc = :soc
                        and etat_act = 'A'
                        and to_number(mat_pers) <= nvl(to_number(:mat_fin), to_number(mat_pers))
                        and mat_pers in (select mat_pers from bult_soin
                                         where cod_soc = :soc
                                         and nat_bult = 'C'
                                         and cod_bord is null
                                         and nvl(envoi, 'N') = 'N')
                        order by to_number(mat_pers)
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamDeb(@Param("soc") String soc, @Param("mat_fin") String mat_fin);

        @Query(value = """
                        select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom
                        from personnel
                        where cod_soc = :soc
                        and etat_act = 'A'
                        and to_number(mat_pers) >= nvl(to_number(:mat_deb), to_number(mat_pers))
                        and mat_pers in (select mat_pers from bult_soin
                                         where cod_soc = :soc
                                         and nat_bult = 'C'
                                         and cod_bord is null
                                         and nvl(envoi, 'N') = 'N')
                        order by to_number(mat_pers)
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamFin(@Param("soc") String soc, @Param("mat_deb") String mat_fin);

}
