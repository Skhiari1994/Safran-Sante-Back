package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePlafondAssur;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.PlafondAssur;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.PlafondCnam;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.PlafondAssurProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlafondAssurRepository extends JpaRepository<PlafondAssur, ClePlafondAssur> {

    @Query(value="select t.annee_assur,\n" +
            "       t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_assur,\n" +
            "       t.plafond,\n" +
            "       t.sold_plaf,\n" +
            "       t.sold_assur_estim,\n" +
            "       (select lib_assur from assurance where cod_assur=t.cod_assur)libAssur from plafond_assur t where t.mat_pers=:mat  order by t.annee_assur desc\n",nativeQuery = true)
    List<PlafondAssurProjection> getPlafondAssur(@Param("mat")String mat);
}
