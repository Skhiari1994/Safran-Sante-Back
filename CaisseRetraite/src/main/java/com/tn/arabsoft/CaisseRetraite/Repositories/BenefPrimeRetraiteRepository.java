package com.tn.arabsoft.CaisseRetraite.Repositories;

import com.tn.arabsoft.CaisseRetraite.Entities.BenefPrimeRetraite;
import com.tn.arabsoft.CaisseRetraite.Entities.Cles.CleBenefPrimeRetr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BenefPrimeRetraiteRepository extends JpaRepository<BenefPrimeRetraite, CleBenefPrimeRetr> {

    @Query(value="select * from benef_prime_retraite where cod_soc=:soc and mat_pers=:mat and num_remb=:num",nativeQuery = true)
    List<BenefPrimeRetraite> getBenefPrime(@Param("soc")String soc, @Param("mat")String mat, @Param("num")Long num);
}
