package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arabsoft.gestion_adherent.entities.AdrPers;
import com.arabsoft.gestion_adherent.entities.cle.CleAdrPers;
import com.arabsoft.gestion_adherent.projections.AdrPersProjection;

import java.util.List;

@Repository
public interface AdrPersRepository extends JpaRepository<AdrPers, CleAdrPers> {

  @Query(value = """
      select t.cod_gouv codGouv,
             t.cod_poste codPoste,
             t.cod_soc codSoc,
             t.mat_pers matPers,
             t.num_adr numAdr,
             t.rue,
             t.tel_pers telPers,
             t.fax_pers faxPers,
             t.adr_courant adrCourant,
             t.rue_a rueA,
             (select lib_gouv
                from gouvernorat
               where cod_gouv = t.cod_gouv) libGouv,
             (select lib_post
                from poste
               where cod_gouv = t.cod_gouv
                 and cod_poste = t.cod_poste) libPoste
        from adr_pers t
       where t.cod_soc = :soc
         and t.mat_pers = :mat
      """, nativeQuery = true)
  List<AdrPersProjection> getAdresse(@Param("soc") String soc,
      @Param("mat") String mat);

  @Query(value = "select coalesce(max(coalesce(num_adr,0) ),0) + 1 from adr_pers where cod_soc = :soc and mat_pers = :mat", nativeQuery = true)
  Long getMaxNumAdr(@Param("soc") String soc, @Param("mat") String mat);

}
