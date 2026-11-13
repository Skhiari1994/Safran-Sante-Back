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
                        SELECT mat_pers,
                               dat_nais,
                               num_retr,
                               CONCAT(CONCAT(COALESCE(nom_pers, ''), ' '), COALESCE(pren_pers, '')) AS nom
                        FROM   personnel
                        WHERE  cod_soc = :soc
                          AND  etat_act = 'A'
                          AND  (CAST(:mat_pers AS DECIMAL(18,0)) IS NULL
                                OR CAST(mat_pers AS DECIMAL(18,0)) <= CAST(:mat_pers AS DECIMAL(18,0)))
                        ORDER BY CAST(mat_pers AS DECIMAL(18,0))
                                                """, nativeQuery = true)
        List<PersonnelProjection> getPersonnel(@Param("soc") String soc, @Param("mat_pers") String mat_pers);

        @Query(value = """
                        SELECT mat_pers, dat_nais, num_retr,
                               CONCAT(CONCAT(COALESCE(nom_pers, ''), ' '), COALESCE(pren_pers, '')) AS nom
                        FROM   personnel
                        WHERE  cod_soc = :soc
                          AND  (mat_pers IN (SELECT mat_pers FROM bult_soin
                                             WHERE cod_soc = :soc AND ANN_PLAF_IMP = :annee)
                                OR mat_pers IN (SELECT mat_pers FROM bult_arriver
                                                WHERE cod_soc = :soc AND ANN_PLAF_IMP = :annee))
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersRecap(@Param("soc") String soc, @Param("annee") String annee);

        @Query(value = """
                        SELECT mat_pers, dat_nais, num_retr,
                               CONCAT(CONCAT(COALESCE(nom_pers, ''), ' '), COALESCE(pren_pers, '')) AS nom
                        FROM   personnel
                        WHERE  cod_soc = :soc
                          AND  etat_act = 'A'
                          AND  (CAST(:mat_fin AS DECIMAL(18,0)) IS NULL
                                OR CAST(mat_pers AS DECIMAL(18,0)) <= CAST(:mat_fin AS DECIMAL(18,0)))
                          AND  mat_pers IN (SELECT mat_pers FROM bult_soin
                                            WHERE cod_soc = :soc
                                              AND nat_bult = 'M'
                                              AND cod_bord IS NULL
                                              AND COALESCE(envoi, 'N') = 'N')
                        ORDER BY CAST(mat_pers AS DECIMAL(18,0))
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultMutDeb(@Param("soc") String soc, @Param("mat_fin") String mat_fin);

        @Query(value = """
                        SELECT mat_pers, dat_nais, num_retr,
                               CONCAT(CONCAT(COALESCE(nom_pers, ''), ' '), COALESCE(pren_pers, '')) AS nom
                        FROM   personnel
                        WHERE  cod_soc = :soc
                          AND  etat_act = 'A'
                          AND  (CAST(:mat_deb AS DECIMAL(18,0)) IS NULL
                                OR CAST(mat_pers AS DECIMAL(18,0)) >= CAST(:mat_deb AS DECIMAL(18,0)))
                          AND  mat_pers IN (SELECT mat_pers FROM bult_soin
                                            WHERE cod_soc = :soc
                                              AND nat_bult = 'M'
                                              AND cod_bord IS NULL
                                              AND COALESCE(envoi, 'N') = 'N')
                        ORDER BY CAST(mat_pers AS DECIMAL(18,0))
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultMutFin(@Param("soc") String soc, @Param("mat_deb") String mat_fin);

        @Query(value = """
                        SELECT mat_pers, dat_nais, num_retr,
                               CONCAT(CONCAT(COALESCE(nom_pers, ''), ' '), COALESCE(pren_pers, '')) AS nom
                        FROM   personnel
                        WHERE  cod_soc = :soc
                          AND  etat_act = 'A'
                          AND  (CAST(:mat_fin AS DECIMAL(18,0)) IS NULL
                                OR CAST(mat_pers AS DECIMAL(18,0)) <= CAST(:mat_fin AS DECIMAL(18,0)))
                          AND  mat_pers IN (SELECT mat_pers FROM bult_soin
                                            WHERE cod_soc = :soc
                                              AND nat_bult = 'C'
                                              AND cod_bord IS NULL
                                              AND COALESCE(envoi, 'N') = 'N')
                        ORDER BY CAST(mat_pers AS DECIMAL(18,0))
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamDeb(@Param("soc") String soc, @Param("mat_fin") String mat_fin);

        @Query(value = """
                        SELECT mat_pers, dat_nais, num_retr,
                               CONCAT(CONCAT(COALESCE(nom_pers, ''), ' '), COALESCE(pren_pers, '')) AS nom
                        FROM   personnel
                        WHERE  cod_soc = :soc
                          AND  etat_act = 'A'
                          AND  (CAST(:mat_deb AS DECIMAL(18,0)) IS NULL
                                OR CAST(mat_pers AS DECIMAL(18,0)) >= CAST(:mat_deb AS DECIMAL(18,0)))
                          AND  mat_pers IN (SELECT mat_pers FROM bult_soin
                                            WHERE cod_soc = :soc
                                              AND nat_bult = 'C'
                                              AND cod_bord IS NULL
                                              AND COALESCE(envoi, 'N') = 'N')
                        ORDER BY CAST(mat_pers AS DECIMAL(18,0))
                        """, nativeQuery = true)
        List<PersonnelProjection> getPersBultCnamFin(@Param("soc") String soc, @Param("mat_deb") String mat_fin);

}
