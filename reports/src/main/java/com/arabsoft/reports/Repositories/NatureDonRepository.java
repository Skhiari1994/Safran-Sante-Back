package com.arabsoft.reports.Repositories;

import com.arabsoft.reports.Entities.NatureDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NatureDonRepository extends JpaRepository<NatureDon,String> {

    @Query(value="select * \n" +
            "from nature_don\n" +
            "where typ_don = 'I'\n" +
            "order by nat_don",nativeQuery = true)
    List<NatureDon> getNatureDon();
}
