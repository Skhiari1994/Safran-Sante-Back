package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VirementRepository extends JpaRepository<Virement,Long> {
    List<Virement> findAllByOrderByOrdreAsc();

}
