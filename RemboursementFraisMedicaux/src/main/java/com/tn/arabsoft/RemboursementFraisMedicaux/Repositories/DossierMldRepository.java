package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleDossierMld;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.DossierMld;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.DossierMldProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DossierMldRepository extends JpaRepository<DossierMld, CleDossierMld> {

    @Query(value="SELECT t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_dos_mld,\n" +
            "       t.cod_malad,\n" +
            "       t.dat_doss_mld,\n" +
            "       t.etat_dos_mld,\n" +
            "       t.num_fam,\n" +
            "       (SELECT lib_malad \n" +
            "        FROM maladie \n" +
            "        WHERE cod_malad = t.cod_malad) AS libMalad,\n" +
            "       CASE \n" +
            "         WHEN t.num_fam <> 0 THEN (\n" +
            "           SELECT nom_pren\n" +
            "           FROM famille\n" +
            "           WHERE cod_soc = t.cod_soc\n" +
            "             AND mat_pers = t.mat_pers\n" +
            "             AND num_fam = t.num_fam\n" +
            "         )\n" +
            "         ELSE 'Adhérent'\n" +
            "       END AS nom\n" +
            "FROM dossier_mld t where t.mat_pers=:mat ORDER BY t.dat_doss_mld DESC \n",nativeQuery = true)
    List<DossierMldProjection> getDossierMll(@Param("mat")String mat);

    @Query(value="select nvl(max(NUM_DOS_MLD),0) + 1 \n" +
            "from dossier_mld \n" +
            "where cod_soc = :soc\n" +
            "and mat_pers = :mat",nativeQuery = true)
    Long getNumDoss(@Param("soc")String soc,@Param("mat")String mat);
}
