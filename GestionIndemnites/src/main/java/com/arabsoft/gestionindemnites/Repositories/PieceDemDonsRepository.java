package com.arabsoft.gestionindemnites.Repositories;

import com.arabsoft.gestionindemnites.Entities.Cle.ClePieceDemDons;
import com.arabsoft.gestionindemnites.Entities.PieceDemDons;
import com.arabsoft.gestionindemnites.Projections.PieceDemDonsProjection;
import com.arabsoft.gestionindemnites.Projections.PieceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PieceDemDonsRepository extends JpaRepository<PieceDemDons, ClePieceDemDons> {

    @Query(value = "SELECT    N.NAT_DON, ND.LIB_NAT_DON,cod_affect,CONCERNE,max_mnt_don\n" +
            "FROM NATURE_DON_MOTIF N, NATURE_DON ND\n" +
            "WHERE nd.TYP_DON =:typDon and N.NAT_DON = ND.NAT_DON\n" +
            "and cod_affect=:codAffect\n" +
            "order by  N.NAT_DON",nativeQuery = true)
    List<PieceDemDonsProjection> getListTypDons(
            @Param("typDon") String typDon,
            @Param("codAffect") String codAffect
    );
    @Query(value = " SELECT    N.NAT_DON, ND.LIB_NAT_DON,cod_affect,CONCERNE,max_mnt_don\n" +
            "FROM NATURE_DON_MOTIF N, NATURE_DON ND\n" +
            "WHERE nd.TYP_DON ='I' and N.NAT_DON = ND.NAT_DON\n" +
            "order by  N.NAT_DON",nativeQuery = true)
    List<PieceDemDonsProjection> getListNatDons();
    @Query(value = "select p.cod_piece,p.lib_piece ,p.lib_piece_a\n" +
            "from pieces p\n" +
            "order by cod_piece",nativeQuery = true)
    List<PieceProjection> getListPiece();
}
