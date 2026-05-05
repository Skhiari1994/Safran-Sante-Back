package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.cle.ClePiecePret;
import com.arabsoft.referentiel.entities.PiecesPret;
import com.arabsoft.referentiel.projections.PiecePretProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PiecesPretRepository extends JpaRepository<PiecesPret, ClePiecePret> {

    @Query(value = """
            select p.cod_soc,
                   p.cod_grp_pret,
                   p.typ_pret,
                   p.cod_piece_pret,
                   p.nat_piece,
                   p.oblig_piece,
                   p.nbr_copie,
                   t.lib_piece_pret
              from pieces_pret p
              join param_pieces_pret t
                on t.cod_piece_pret = p.cod_piece_pret
             where p.cod_grp_pret = :grpPret
               and p.typ_pret = :typ
            """, nativeQuery = true)
    List<PiecePretProjection> getListPieces(@Param("grpPret") String grpPret,
            @Param("typ") String typ);

    @Modifying
    @Transactional
    @Query(value = "delete from pieces_pret where cod_grp_pret = :grpPret and typ_pret = :typ and cod_piece_pret = :piecePret", nativeQuery = true)
    void deletePiecePret(@Param("grpPret") String grpPret, @Param("typ") String typ,
            @Param("piecePret") String piecePret);
}
