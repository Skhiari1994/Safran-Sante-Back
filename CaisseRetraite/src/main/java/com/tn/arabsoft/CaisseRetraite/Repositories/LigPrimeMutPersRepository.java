package com.tn.arabsoft.CaisseRetraite.Repositories;

import com.tn.arabsoft.CaisseRetraite.Entities.Cles.CleLigPrimeMutPers;
import com.tn.arabsoft.CaisseRetraite.Entities.LigPrimeMutPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LigPrimeMutPersRepository extends JpaRepository<LigPrimeMutPers, CleLigPrimeMutPers> {

    @Query(value="select cod_soc,\n" +
            "mat_pers,\n" +
            "num_prime,\n" +
            "dat_prime,\n" +
            "mnt_payer from Lig_prime_mut_pers\n",nativeQuery = true)
    List<LigPrimeMutPers> getAll();
    @Query(value="select cod_soc,\n" +
            "mat_pers,\n" +
            "num_prime,\n" +
            "dat_prime,\n" +
            "mnt_payer from Lig_prime_mut_pers where mat_pers=:mat and cod_soc=:soc and num_prime=:num and dat_prime=:dat1\n",nativeQuery = true)
    List<LigPrimeMutPers> getAllByMat(@Param("soc") String soc,@Param("mat") String mat,@Param("num") Long num,@Param("dat1") LocalDate dat1);

}
