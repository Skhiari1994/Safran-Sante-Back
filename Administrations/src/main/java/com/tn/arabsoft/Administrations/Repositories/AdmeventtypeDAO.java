package com.tn.arabsoft.administrations.repositories;

import java.util.List;

import com.tn.arabsoft.administrations.entities.Admeventtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdmeventtypeDAO extends JpaRepository<Admeventtype, Long> {

	@Query(value = """
			select a.evt_id,
			       a.evt_name,
			       a.evt_evt_id,
			       a.sum_id,
			       a.evt_action,
			       a.evt_rank,
			       a.evt_maint
			from admeventtype a
			where a.sum_id = :sumId
			""", nativeQuery = true)
	List<Admeventtype> getMenuBySumId(@Param("sumId") Long sumId);

	@Query(value = """
			select a.evt_id,
			       a.evt_name,
			       a.evt_evt_id,
			       a.sum_id,
			       a.evt_action,
			       a.evt_rank,
			       a.evt_maint
			from admeventtype a
			where a.evt_maint = 'O'
			order by a.evt_rank asc
			""", nativeQuery = true)
	List<Admeventtype> getListAdmeventtype();

	@Query(value = """
			select a.evt_id,
			       a.evt_name,
			       a.evt_evt_id,
			       a.sum_id,
			       a.evt_action,
			       a.evt_rank,
			       a.evt_maint
			from admeventtype a
			where a.evt_id in (
			    select distinct evt_id
			    from admstorageautorisation a
			    where a.role_id = :roleId
			)
			and a.evt_maint = 'O'
			order by a.evt_rank asc
			""", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeByRole(@Param("roleId") Long roleId);

	@Query(value = """
			SELECT evt_id,
			       evt_name,
			       evt_evt_id,
			       sum_id,
			       evt_action,
			       evt_rank,
			       evt_maint
			FROM Admeventtype
			WHERE sum_id = :sum
			  AND evt_evt_id IS NULL
			ORDER BY EVT_RANK ASC
			""", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeNiv(@Param("sum") String sum);

	@Query(value = """
			SELECT evt_id,
			       evt_name,
			       evt_evt_id,
			       sum_id,
			       evt_action,
			       evt_rank,
			       evt_maint
			FROM Admeventtype
			WHERE evt_evt_id IS NULL
			  AND evt_id = :evt
			ORDER BY EVT_RANK ASC
			""", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeNiv1(@Param("evt") String evt);

	@Query(value = """
			SELECT evt_id,
			       evt_name,
			       evt_evt_id,
			       sum_id,
			       evt_action,
			       evt_rank,
			       evt_maint
			FROM Admeventtype
			WHERE evt_evt_id = :evt
			ORDER BY EVT_RANK ASC
			""", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeNiv2(@Param("evt") String evt);

	@Modifying
	@Query(value = """
			DELETE FROM Admeventtype
			WHERE evt_id = :evtId
			""", nativeQuery = true)
	void deleteByEvtId(@Param("evtId") Long evtId);

	@Query(value = """
			SELECT a.evt_id,
			       a.evt_action,
			       a.evt_evt_id,
			       a.evt_maint,
			       a.evt_name,
			       a.evt_rank,
			       a.sum_id
			FROM admeventtype a,
			     admsubmodule b,
			     admstorageautorisation c
			WHERE a.sum_id = b.sum_id
			  AND a.evt_id = c.evt_id
			  AND b.sum_id = c.sum_id
			  AND role_id = :profil
			  AND a.evt_action IS NOT NULL
			""", nativeQuery = true)
	List<Admeventtype> getActionsByRole(@Param("profil") Long profil);
}