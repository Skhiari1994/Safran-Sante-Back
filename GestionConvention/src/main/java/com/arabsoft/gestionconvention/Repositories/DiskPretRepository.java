package com.arabsoft.gestionconvention.Repositories;

 import com.arabsoft.gestionconvention.Entities.DiskPret;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiskPretRepository extends JpaRepository<DiskPret,Long> {

    @Query(value="SELECT \n" +
            "    t.dat_disk,\n" +
            "    t.pret_disk,\n" +
            "    t.num_retr,\n" +
            "    t.mat_pers,\n" +
            "    t.montant,\n" +
            "    t.observation,\n" +
            "    t.valid,\n" +
            "    t.cod_grp_pret,\n" +
            "    t.typ_pret,\n" +
            "    t.cod_pret,\n" +
            "    t.id,\n" +
            "    (SELECT p.nom_pers || ' ' || p.pren_pers \n" +
            "     FROM personnel p \n" +
            "     WHERE p.mat_pers = t.mat_pers) AS nom\n" +
            "FROM \n" +
            "    disk_pret t\n" +
            "WHERE \n" +
            "    t.valid = 'I'\n" +
            "    AND t.mat_pers IN (\n" +
            "        SELECT mat_pers \n" +
            "        FROM personnel \n" +
            "        WHERE corps = NVL(:corps, corps)\n" +
            "    )\n" +
            "    AND TO_CHAR(t.dat_disk, 'mm/yyyy') = TO_CHAR(TO_DATE(:mois, 'mm/yyyy'), 'mm/yyyy')\n" +
            "    AND t.pret_disk = '564'",nativeQuery=true)
    List<DiskPret> getDiskPret(@Param("corps") String corps,@Param("mois") String mois);
}
