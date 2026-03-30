package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.AssurFil;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.AssurFilCle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AssurFilRepository extends JpaRepository<AssurFil,AssurFilCle> {

}
