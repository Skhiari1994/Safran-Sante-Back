package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Cle.CleTypePret;
import com.arabsoft.referentiel.Entities.GroupePret;
import com.arabsoft.referentiel.Entities.TypePret;
import com.arabsoft.referentiel.Projections.TypPretProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypePretRepository extends JpaRepository<TypePret, CleTypePret> {

    @Query(value="select * from type_pret where COD_GRP_PRET=:grpPret",nativeQuery = true)
    List<TypePret> getTypePret(@Param("grpPret")String grpPret);
    @Query(value="select t.cod_grp_pret,g.lib_grp_pret,t.typ_pret,t.lib_pret from groupe_pret g , type_pret t where g.cod_grp_pret=t.cod_grp_pret",nativeQuery = true)
    List<TypPretProjection> getAllTypePret();
    @Modifying
    @Transactional
    @Query(value="delete from type_pret where cod_soc=:soc and cod_grp_pret=:grpPret and typ_pret=:typ",nativeQuery = true)
    void deleteTypPret(@Param("soc")String soc,@Param("grpPret")String grpPret,@Param("typ")String typ);
}
