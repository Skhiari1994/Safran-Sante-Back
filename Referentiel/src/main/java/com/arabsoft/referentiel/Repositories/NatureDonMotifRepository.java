package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Acte;
import com.arabsoft.referentiel.Entities.Cle.CleNatureDonMotif;
import com.arabsoft.referentiel.Entities.NatureDonMotif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NatureDonMotifRepository extends JpaRepository<NatureDonMotif, CleNatureDonMotif> {


}

