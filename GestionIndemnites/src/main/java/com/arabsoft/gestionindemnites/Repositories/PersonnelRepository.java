package com.arabsoft.gestionindemnites.Repositories;

import com.arabsoft.gestionindemnites.Entities.Cle.ClePersonnel;
import com.arabsoft.gestionindemnites.Entities.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

    @Query("""
        select (count(p) > 0)
        from Personnel p
        where p.cod_soc = :codSoc
          and p.mat_pers = :matPers
    """)
    boolean existsPersonnel(@Param("codSoc") String codSoc,
                            @Param("matPers") String matPers);
}
