package com.arabsoft.gestioncotisation.Repositories;

import com.arabsoft.gestioncotisation.DTO.LigCotisMutPersDTO;
import com.arabsoft.gestioncotisation.Entities.Cle.CleLigCotisMutPers;
import com.arabsoft.gestioncotisation.Entities.LigCotisMutPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LigCotisMutPersRepository  extends JpaRepository<LigCotisMutPers, CleLigCotisMutPers> {
    @Query("SELECT c.cod_soc, c.mat_pers, c.num_cot, c.dat_mut, c.mnt_payer, c.dat_cot " +
            "FROM LigCotisMutPers c " +
            "WHERE c.cod_soc = :codSoc AND c.mat_pers = :matPers AND c.dat_cot BETWEEN :startDate AND :endDate order by c.dat_cot")
    List<Object[]> findExistingCotisMutPersSummary(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}