package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Assurance;
import com.arabsoft.referentiel.Entities.RefFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefFiliereRepository extends JpaRepository<RefFiliere,String> {


}
