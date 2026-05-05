package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.cle.CleRefFillAct;
import com.arabsoft.referentiel.entities.RefFillAct;
import com.arabsoft.referentiel.projections.RefFillActProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefFillActRepository extends JpaRepository<RefFillAct, CleRefFillAct> {

    @Query(value = """
            select r.cod_fil,
                   r.abrv_act,
                   r.val_prix,
                   (select a.lib_act
                      from acte a
                     where a.abrv_act = r.abrv_act) as libActe
              from ref_fill_act r
             where r.cod_fil = :codFil
            """, nativeQuery = true)
    List<RefFillActProjection> getFillAct(@Param("codFil") String codFil);

}
