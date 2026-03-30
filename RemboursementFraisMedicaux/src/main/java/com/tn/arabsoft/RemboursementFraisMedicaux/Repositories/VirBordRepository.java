package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.VirBord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VirBordRepository extends JpaRepository<VirBord,String> {
    List<VirBord> findAllByOrderByOrdreAsc();
}
