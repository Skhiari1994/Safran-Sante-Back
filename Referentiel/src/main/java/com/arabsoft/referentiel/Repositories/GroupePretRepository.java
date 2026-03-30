package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.GroupePret;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupePretRepository extends JpaRepository<GroupePret,String> {
    @Modifying
    @Transactional
    @Query(value="delete from groupe_pret where cod_soc=:soc and cod_grp_pret=:grpPret",nativeQuery = true)
    void deleteGroupPret(@Param("soc")String soc, @Param("grpPret")String grpPret);
}
