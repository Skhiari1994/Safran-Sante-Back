package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.NatureDon;
import com.arabsoft.referentiel.Projections.AffectationProjection;
import com.arabsoft.referentiel.Projections.CompteIndParamProjection;
import com.arabsoft.referentiel.Projections.NatureDonProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NatureDonRepository  extends JpaRepository<NatureDon,String> {

    @Query(value="SELECT  C.NUM_CPT, C.LIB_CPT\n" +
            "FROM COMPTE C where length(num_cpt)>3 and num_cpt like '6%'",nativeQuery = true)
    List<CompteIndParamProjection> getListCompte();
    @Query(value="SELECT COD_AFFECT,LIB_AFFECT,LIB_AFFECT_A\n" +
            "FROM AFFECTATION\n" +
            "order by  COD_AFFECT",nativeQuery = true)
    List<AffectationProjection> getAffectation();

    @Query(value="SELECT n.nat_don, n.cod_affect,a.lib_affect,a.lib_affect_a\n" +
            "FROM nature_don_motif n,affectation a\n" +
            "where n.cod_affect=a.cod_affect\n" +
            "and nat_don=:nat",nativeQuery = true)
    List<NatureDonProjection> getAffectationByNat(@Param("nat")String nat);




}
