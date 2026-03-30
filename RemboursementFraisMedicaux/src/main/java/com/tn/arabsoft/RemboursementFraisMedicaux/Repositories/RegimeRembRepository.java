package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.RegimeRemb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegimeRembRepository extends JpaRepository<RegimeRemb,String> {

    @Query(value="select * from regime_remb order by lib_remb",nativeQuery = true)
    List<RegimeRemb> getRegimeRemb();
}
