package com.arabsoft.reports.Repositories;

import com.arabsoft.reports.Entities.Cle.ClePersonnel;
import com.arabsoft.reports.Entities.Personnel;
import com.arabsoft.reports.Projections.PersonnelProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonnelDao extends JpaRepository<Personnel, ClePersonnel> {
        @Query(value = "select mat_pers, mat_int, dat_nais, num_retr, nom_pers || ' ' || pren_pers as nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "  and etat_act = 'A'\n" +
                        "  and to_number(mat_pers) <= nvl(:mat_pers, to_number(mat_pers))\n" +
                        "order by to_number(mat_pers)", nativeQuery = true)
        List<PersonnelProjection> getPersonnel(@Param("soc") String soc, @Param("mat_pers") String mat_pers);

        @Query(value = "select mat_pers,mat_int,dat_nais,num_retr,nom_pers||' '||pren_pers as nom from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and mat_pers in(select mat_pers from bult_soin\n" +
                        "where cod_soc = :soc and ANN_PLAF_IMP = :annee)\n" +
                        "or \n" +
                        "mat_pers in(select mat_pers from bult_arriver\n" +
                        "where cod_soc = :soc and ANN_PLAF_IMP = :annee)", nativeQuery = true)
        List<PersonnelProjection> getPersRecap(@Param("soc") String soc, @Param("annee") String annee);

        @Query(value = "select mat_pers,mat_int,dat_nais,num_retr,nom_pers||' '||pren_pers nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and etat_act = 'A'\n" +
                        "and to_number(mat_pers) <= nvl(to_number(:mat_fin),to_number(mat_pers))\n" +
                        "and mat_pers in(select mat_pers from bult_soin where cod_soc = :soc and  nat_bult = 'M'\n" +
                        "and cod_bord is null\n" +
                        "and nvl(envoi,'N') = 'N')\n" +
                        "order by to_number(mat_pers)", nativeQuery = true)
        List<PersonnelProjection> getPersBultMutDeb(@Param("soc") String soc, @Param("mat_fin") String mat_fin);

        @Query(value = "select mat_pers,mat_int,dat_nais,num_retr,nom_pers||' '||pren_pers nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and etat_act = 'A'\n" +
                        "and to_number(mat_pers) >= nvl(to_number(:mat_deb),to_number(mat_pers))\n" +
                        "and mat_pers in(select mat_pers from bult_soin where cod_soc = :soc and  nat_bult = 'M'\n" +
                        "and cod_bord is null\n" +
                        "and nvl(envoi,'N') = 'N')\n" +
                        "order by to_number(mat_pers)", nativeQuery = true)
        List<PersonnelProjection> getPersBultMutFin(@Param("soc") String soc, @Param("mat_deb") String mat_fin);

        @Query(value = "select mat_pers,mat_int,dat_nais,num_retr,nom_pers||' '||pren_pers nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and etat_act = 'A'\n" +
                        "and to_number(mat_pers) <= nvl(to_number(:mat_fin),to_number(mat_pers))\n" +
                        "and mat_pers in(select mat_pers from bult_soin where cod_soc = :soc and  nat_bult = 'C'\n" +
                        "and cod_bord is null\n" +
                        "and nvl(envoi,'N') = 'N')\n" +
                        "order by to_number(mat_pers)", nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamDeb(@Param("soc") String soc, @Param("mat_fin") String mat_fin);

        @Query(value = "select mat_pers,mat_int,dat_nais,num_retr,nom_pers||' '||pren_pers nom\n" +
                        "from personnel\n" +
                        "where cod_soc = :soc\n" +
                        "and etat_act = 'A'\n" +
                        "and to_number(mat_pers) >= nvl(to_number(:mat_deb),to_number(mat_pers))\n" +
                        "and mat_pers in(select mat_pers from bult_soin where cod_soc = :soc and  nat_bult = 'C'\n" +
                        "and cod_bord is null\n" +
                        "and nvl(envoi,'N') = 'N')\n" +
                        "order by to_number(mat_pers)", nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamFin(@Param("soc") String soc, @Param("mat_deb") String mat_fin);
}
