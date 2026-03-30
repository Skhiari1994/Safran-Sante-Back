package com.arabsoft.referentiel.Repositories;

  import com.arabsoft.referentiel.Entities.Agence;
  import com.arabsoft.referentiel.Entities.Cle.CleAgence;
  import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgenceRepository extends JpaRepository<Agence, CleAgence> {


    @Query(value="select * from agence where cod_banq=:banq",nativeQuery = true)
    List<Agence> getAgence(@Param("banq") String banq);
}
