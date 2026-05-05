package com.arabsoft.reports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arabsoft.reports.entities.NatureDon;

import java.util.List;

public interface NatureDonRepository extends JpaRepository<NatureDon, String> {

    @Query(value = """
            select *
            from nature_don
            where typ_don = 'I'
            order by nat_don
            """, nativeQuery = true)
    List<NatureDon> getNatureDon();

}
