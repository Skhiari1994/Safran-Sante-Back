package com.arabsoft.gestionindemnites.Repositories;
import com.arabsoft.gestionindemnites.Entities.VirCarte;
import com.arabsoft.gestionindemnites.Projections.VarCartDataProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirCarteRepository extends JpaRepository<VirCarte, Long> {

    @Procedure(name = "VirCarteEntity.callVirCarteProcedure")
    String callVirCarteProcedure(
            @Param("soc_") String soc,
            @Param("dat_debloc") String datDebloc,
            @Param("wcod_lieu_geog") String wcodLieuGeog,
            @Param("wnat_don") String wnatDon
    );
    @Query(value = "select * from VIR_CARTE_DATA",nativeQuery = true)
    List<VarCartDataProjection> findAllByOrderByOrdreAsc();
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VIR_CARTE_DATA", nativeQuery = true)
    void deleteAllData();
}
