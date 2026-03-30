package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.AdrPers;
import com.arabsoft.Gestion_adherent.Entities.Cle.CleAdrPers;
import com.arabsoft.Gestion_adherent.Projections.AdrPersProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdrPersRepository extends JpaRepository<AdrPers, CleAdrPers> {

    @Query(value="  select t.cod_gouv codGouv,t.cod_poste codPoste,\n" +
            "       t.cod_soc codSoc,\n" +
            "       t.mat_pers matPers,\n" +
            "       t.num_adr numAdr,\n" +
            "       t.rue,\n" +
            "       t.tel_pers telPers,\n" +
            "       t.fax_pers faxPers,\n" +
            "       t.adr_courant adrCourant,\n" +
            "       t.rue_a rueA,(select lib_gouv from gouvernorat where cod_gouv= t.cod_gouv)libGouv,(select lib_post from poste where cod_gouv= t.cod_gouv and cod_poste=t.cod_poste)libPoste from adr_pers t where t.cod_soc=:soc and t.mat_pers=:mat",nativeQuery = true)
    List<AdrPersProjection> getAdresse(@Param("soc") String soc, @Param("mat") String mat);

    @Query(value="select nvl(max(nvl(num_adr,0)),0) + 1 from adr_pers where cod_soc=:soc and mat_pers=:mat",nativeQuery = true)
    Long getMaxNumAdr(@Param("soc") String soc,@Param("mat") String mat);
}
