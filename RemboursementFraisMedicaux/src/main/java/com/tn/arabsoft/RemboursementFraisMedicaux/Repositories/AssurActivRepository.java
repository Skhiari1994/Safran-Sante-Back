package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.AssurActiv;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.AssurActivCle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssurActivRepository extends JpaRepository<AssurActiv, AssurActivCle>{

}