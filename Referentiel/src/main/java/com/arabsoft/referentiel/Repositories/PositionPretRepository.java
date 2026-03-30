package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Cle.ClePositionPret;
import com.arabsoft.referentiel.Entities.PositionPret;
import com.arabsoft.referentiel.Projections.PositionPretProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionPretRepository extends JpaRepository<PositionPret, ClePositionPret> {

//    @Query(value="select * from position_pret where cod_grp_pret=:grpPret and typ_pret=:typ",nativeQuery = true)
//    List<PositionPret> getPositionPret(@Param("grpPret")String grpPret,@Param("typ")String typ);

    @Query(value="select p.cod_grp_pret, p.typ_pret, p.cod_motif, p.cod_soc, a.lib_affect\n" +
            "  from position_pret p, affectation a\n" +
            " where a.cod_affect = p.cod_motif and p.cod_grp_pret=:grpPret and p.typ_pret=:typ \n",nativeQuery = true)
    List<PositionPretProjection> getPositionPret(@Param("grpPret")String grpPret,@Param("typ")String typ);

    @Modifying
    @Transactional
    @Query(value="delete from position_pret where   cod_grp_pret=:grpPret and typ_pret=:typ and cod_motif=:motif",nativeQuery = true)
    void deletePositionPret(@Param("grpPret")String grpPret,@Param("typ")String typ,@Param("motif")String motif);
}
