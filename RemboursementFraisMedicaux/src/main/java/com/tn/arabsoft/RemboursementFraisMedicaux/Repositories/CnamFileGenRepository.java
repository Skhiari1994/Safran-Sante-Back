package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.CnamFileGen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CnamFileGenRepository extends JpaRepository<CnamFileGen, Long> {
    Optional<CnamFileGen> findByCodSocAndCodBord(String cod_soc, String cod_bord);
}
