package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.Cle.ClePiecePret;
import com.arabsoft.referentiel.Entities.PiecesPret;
import com.arabsoft.referentiel.Projections.PiecePretProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PiecesPretRepository extends JpaRepository<PiecesPret, ClePiecePret> {

    @Query(value="select p.cod_soc,\n" +
            "       p.cod_grp_pret,\n" +
            "       p.typ_pret,\n" +
            "       p.cod_piece_pret,\n" +
            "       p.nat_piece,\n" +
            "       p.oblig_piece,\n" +
            "       p.nbr_copie,\n" +
            "       t.lib_piece_pret\n" +
            "  from pieces_pret p, param_pieces_pret t\n" +
            " where t.cod_piece_pret = p.cod_piece_pret and p.cod_grp_pret=:grpPret and p.typ_pret=:typ \n ",nativeQuery = true)
    List<PiecePretProjection> getListPieces(@Param("grpPret") String grpPret,@Param("typ") String typ);

    @Modifying
    @Transactional
    @Query(value="delete from pieces_pret where   cod_grp_pret=:grpPret and typ_pret=:typ and cod_piece_pret=:piecePret",nativeQuery = true)
    void deletePiecePret(@Param("grpPret")String grpPret,@Param("typ")String typ,@Param("piecePret")String piecePret);
}
