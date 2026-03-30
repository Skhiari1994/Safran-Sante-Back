package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.VirCarte;
import com.arabsoft.Credits.Entities.VirCarteLine;
import com.arabsoft.Credits.Entities.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VirCarteRepository extends JpaRepository<VirCarte,Long> {

    List<VirCarte> findAllByOrderByOrdreAsc();


    @Query("select v from VirCarteLine v order by v.fileId, v.ordre")
    List<VirCarteLine> findAllOrdered();
}
