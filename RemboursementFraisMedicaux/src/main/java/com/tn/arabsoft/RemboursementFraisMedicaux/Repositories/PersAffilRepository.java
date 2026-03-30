package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePersAffil;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.PersAffil;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.PersAffilProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.RefEtablisProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersAffilRepository extends JpaRepository<PersAffil, ClePersAffil> {


    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_fil,\n" +
            "       t.courant,\n" +
            "       t.annee_aff,\n" +
            "       t.prf_typ,\n" +
            "       t.prf_cod,\n" +
            "       (select LIB_FILL from ref_filliere r where r.cod_fil=t.cod_fil)lib_fil from pers_affil t where t.cod_soc=:soc and t.mat_pers=:mat \n",nativeQuery = true)
    List<PersAffilProjection> getPersAffil(@Param("soc")String soc,@Param("mat")String mat);

    @Query(value="select PRF_TYP,PRF_COD,ETAB_RSOC||' '||PR_RSOC nom\n" +
            "from ref_etablis\n" +
            "where PRF_TYP = '1'\n" +
            "and COD_ACTIV in(select COD_ACTIV from ref_filliere \n" +
            "  where COD_FIL = '2')\n" +
            "order by to_number(prf_cod)",nativeQuery = true)
    List<RefEtablisProjection> getRefEtablis();
}
