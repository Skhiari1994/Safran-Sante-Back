package com.arabsoft.reports.repositories;

import com.arabsoft.reports.entities.PretPers;
import com.arabsoft.reports.entities.cle.ClePretPers;
import com.arabsoft.reports.projections.PretProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PretPersRepository extends JpaRepository<PretPers, ClePretPers> {

    @Query(value = """
            select p.typ_pret, p.cod_pret, to_char(p.prt_dat_deb, 'dd/mm/yyyy') date_deb, p.prt_mnt_glb, g.lib_pret
            from pret_pers p, type_pret g
            where p.typ_pret = g.typ_pret
            and p.cod_grp_pret = g.cod_grp_pret
            and p.mat_pers = :mat
            order by p.cod_pret desc
            """, nativeQuery = true)
    List<PretProjection> getPretPers(@Param("mat") String mat);

}
