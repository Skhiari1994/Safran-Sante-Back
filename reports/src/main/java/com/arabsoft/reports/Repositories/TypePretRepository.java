package com.arabsoft.reports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arabsoft.reports.entities.TypePret;
import com.arabsoft.reports.entities.cle.CleTypePret;

import java.util.List;

public interface TypePretRepository extends JpaRepository<TypePret, CleTypePret> {

    @Query(value = "select * from type_pret order by COD_GRP_PRET,TYP_PRET", nativeQuery = true)
    List<TypePret> getTypePret();

}
