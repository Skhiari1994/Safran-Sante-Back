package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Maladie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaladieRepository extends JpaRepository<Maladie,String> {

    @Query(value="select * \n" +
            "from maladie\n" +
            "where APCI = 'O'",nativeQuery = true)
    List<Maladie> getMaladie();
}
