package com.arabsoft.reports.Repositories;

import com.arabsoft.reports.Entities.Cle.ClePretPers;
import com.arabsoft.reports.Entities.GroupePret;
import com.arabsoft.reports.Entities.PretPers;
import com.arabsoft.reports.Projections.PretProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PretPersRepository extends JpaRepository<PretPers, ClePretPers> {


    @Query(value="select p.typ_pret,p.cod_pret,to_char(p.prt_dat_deb,'dd/mm/yyyy') date_deb,p.prt_mnt_glb,g.lib_pret\n" +
            "from  pret_pers p,type_pret g\n" +
            "where p.typ_pret=g.typ_pret\n" +
            "and p.cod_grp_pret=g.cod_grp_pret\n" +
            "and p.mat_pers= :mat order by p.cod_pret desc",nativeQuery = true)
     List<PretProjection> getPretPers(@Param("mat") String mat);


}
