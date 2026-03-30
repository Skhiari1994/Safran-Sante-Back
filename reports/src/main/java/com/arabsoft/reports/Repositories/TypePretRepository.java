package com.arabsoft.reports.Repositories;

import com.arabsoft.reports.Entities.Cle.CleTypePret;
import com.arabsoft.reports.Entities.TypePret;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypePretRepository extends JpaRepository<TypePret, CleTypePret> {

    @Query(value="select * from type_pret order by COD_GRP_PRET,TYP_PRET",nativeQuery = true)
    List<TypePret> getTypePret();
}
