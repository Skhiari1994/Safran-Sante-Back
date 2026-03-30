package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Maladie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaladieRepository extends JpaRepository<Maladie,String> {
}
