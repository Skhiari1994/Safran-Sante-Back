package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Assurance;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BordEnvoi;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.ListAssurProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssuranceRepository extends JpaRepository<Assurance,String> {



    @Query(value="select COD_ASSUR,LIB_ASSUR\n" +
            "    from assurance",nativeQuery = true)
    List<ListAssurProjection> getListAssurance();

}
