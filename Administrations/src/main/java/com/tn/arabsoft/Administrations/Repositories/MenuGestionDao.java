package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Admeventtype;
import com.tn.arabsoft.administrations.projections.AutorisationNoeudProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** Requêtes de la gestion du menu (autorisations par noeud, rangs, suppression en cascade). */
public interface MenuGestionDao extends JpaRepository<Admeventtype, Long> {

    @Query(value = "select distinct sum_id as sumId, evt_id as evtId, role_id as roleId from admstorageautorisation",
            nativeQuery = true)
    List<AutorisationNoeudProjection> findAutorisations();

    @Modifying
    @Query(value = "insert into admstorageautorisation (id_autho, role_id, sum_id, evt_id) "
            + "values (ADMSTORAGEAUTORISATION_SEQ.nextval, :role, :sum, :evt)", nativeQuery = true)
    int insertAutorisation(@Param("sum") Long sum, @Param("evt") Long evt, @Param("role") Long role);

    @Modifying
    @Query(value = "delete from admstorageautorisation where sum_id = :sum and role_id = :role "
            + "and coalesce(evt_id, -1) = coalesce(:evt, -1)", nativeQuery = true)
    int deleteAutorisation(@Param("sum") Long sum, @Param("evt") Long evt, @Param("role") Long role);

    @Modifying
    @Query(value = "delete from admstorageautorisation where evt_id in (:evtIds)", nativeQuery = true)
    int deleteAutorisationsEvt(@Param("evtIds") List<Long> evtIds);

    @Modifying
    @Query(value = "delete from admstorageautorisation where sum_id = :sum", nativeQuery = true)
    int deleteAutorisationsModule(@Param("sum") Long sum);

    @Modifying
    @Query(value = "delete from admeventtype where sum_id = :sum", nativeQuery = true)
    int deleteEvenementsModule(@Param("sum") Long sum);

    @Query(value = "select SEQ_ADMSUBMODULE.nextval from dual", nativeQuery = true)
    Long nextModuleId();

    @Modifying
    @Query(value = "insert into admsubmodule (sum_id, sum_name, sum_rank, mod_id, sum_status, sum_rout) "
            + "values (:id, :name, :rank, 1, :status, :rout)", nativeQuery = true)
    int insertModule(@Param("id") Long id, @Param("name") String name, @Param("rank") Long rank,
            @Param("status") String status, @Param("rout") String rout);

    @Modifying
    @Query(value = "update admsubmodule set sum_rank = :rank where sum_id = :id", nativeQuery = true)
    int updateRangModule(@Param("id") Long id, @Param("rank") Long rank);

    @Modifying
    @Query(value = "update admeventtype set evt_rank = :rank where evt_id = :id", nativeQuery = true)
    int updateRangEvenement(@Param("id") Long id, @Param("rank") Long rank);

}
