package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Cle.CleRefFillAct;
import com.arabsoft.referentiel.Entities.RefFillAct;
import com.arabsoft.referentiel.Projections.RefFillActProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefFillActRepository extends JpaRepository<RefFillAct, CleRefFillAct> {

    @Query(value="select r.cod_fil,\n" +
            "       r.abrv_act,\n" +
            "       r.val_prix,\n" +
            "       (select LIB_ACT from acte where r.abrv_act=abrv_act)libActe\n" +
            "from ref_fill_act r where cod_fil=:codFil ",nativeQuery = true)
    List<RefFillActProjection> getFillAct(@Param("codFil") String codFil);
}
