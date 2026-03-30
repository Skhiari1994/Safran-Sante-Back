package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePlafondAssur;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.PlafondCnam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlafondCnamRepository extends JpaRepository<PlafondCnam, ClePlafondAssur> {

    @Query(value="select * from plafond_cnam where mat_pers=:mat order by annee_cnam DESC",nativeQuery = true)
    List<PlafondCnam> getPlafondCnam(@Param("mat")String mat);
}
