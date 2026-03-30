package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.FichSal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FichSalRepository extends JpaRepository<FichSal,Long> {
    @Query(value="select * from fich_sal order by ordre",nativeQuery = true)
    List<FichSal> getAllFichSal();
}
