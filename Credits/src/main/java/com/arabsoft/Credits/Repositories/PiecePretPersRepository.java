package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.ClePiecePRetPers;
import com.arabsoft.Credits.Entities.PiecePretPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PiecePretPersRepository extends JpaRepository<PiecePretPers, ClePiecePRetPers> {


    @Query(value="select * from piece_pret_pers where cod_soc=:soc and mat_pers =:mat\n" +
            "and cod_pret =:pret",nativeQuery = true)
    List<PiecePretPers> getPiecePretPers(@Param("soc") String soc,@Param("mat") String mat,@Param("pret") String pret);
}
