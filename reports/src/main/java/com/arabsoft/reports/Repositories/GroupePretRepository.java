package com.arabsoft.reports.Repositories;

import com.arabsoft.reports.Entities.GroupePret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupePretRepository extends JpaRepository<GroupePret,String> {

    @Query(value="select * from groupe_pret order by cod_grp_pret",nativeQuery = true)
    List<GroupePret> getGroupePret();
}
