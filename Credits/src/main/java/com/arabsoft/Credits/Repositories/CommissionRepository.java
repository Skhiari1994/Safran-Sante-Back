package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleCommission;
import com.arabsoft.Credits.Entities.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, CleCommission> {

    @Query(value="select * from commission t where cod_soc=:soc and nvl(cod_etat_pret,'P') ='P' order by t.num_comm desc",nativeQuery = true)
    List<Commission> getCommissionPreparer(@Param("soc") String soc);
    @Query(value="select * from commission t where cod_soc=:soc and nvl(cod_etat_pret,'P') ='V' order by t.num_comm desc",nativeQuery = true)
    List<Commission> getCommissionValider(@Param("soc") String soc);

    @Query(value="select * from commission t where cod_soc='01' order by t.num_comm desc",nativeQuery = true)
    List<Commission> getCommissionDESC();
}
