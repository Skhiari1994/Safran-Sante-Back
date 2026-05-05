package com.tn.arabsoft.administrations.repositories;

import java.util.List;

import com.tn.arabsoft.administrations.entities.Admeventtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdmeventtypeDAO extends JpaRepository<Admeventtype, Long> {

	@Query(value = "select evt_id, evt_name, evt_evt_id, sum_id, evt_action, evt_rank, evt_maint from Admeventtype where sum_id=:sumId", nativeQuery = true)
	List<Admeventtype> getMenuBySumId(@Param("sumId") Long sumId);

	@Query(value = "select evt_id, evt_name, evt_evt_id, sum_id, evt_action, evt_rank, evt_maint from Admeventtype where EVT_MAINT='O' order by EVT_RANK asc", nativeQuery = true)
	List<Admeventtype> getListAdmeventtype();

	@Query(value = "select evt_id, evt_name, evt_evt_id, sum_id, evt_action, evt_rank, evt_maint from Admeventtype where "
			+
			"evt_id in (select distinct evt_id from admstorageautorisation where role_id=:roleId) and EVT_MAINT='O' order by EVT_RANK asc", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeByRole(@Param("roleId") Long roleId);

	@Query(value = "select evt_id, evt_name, evt_evt_id, sum_id, evt_action, evt_rank, evt_maint from Admeventtype where sum_id=:sum and evt_evt_id is null order by EVT_RANK asc", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeNiv(@Param("sum") String sum);

	@Query(value = "select evt_id, evt_name, evt_evt_id, sum_id, evt_action, evt_rank, evt_maint from Admeventtype where evt_evt_id is null and evt_id=:evt order by EVT_RANK asc", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeNiv1(@Param("evt") String evt);

	@Query(value = "select evt_id, evt_name, evt_evt_id, sum_id, evt_action, evt_rank, evt_maint from Admeventtype where  evt_evt_id=:evt order by EVT_RANK asc", nativeQuery = true)
	List<Admeventtype> getListAdmeventtypeNiv2(@Param("evt") String evt);

	@Query(value = "delete from Admeventtype where evt_id=:evtId", nativeQuery = true)
	void deleteByEvtId(@Param("evtId") Long evtId);

	@Query(value = """
			select a.evt_id,
			       a.evt_action,
			       a.evt_evt_id,
			       a.evt_maint,
			       a.evt_name,
			       a.evt_rank,
			       a.sum_id
			from admeventtype a,
			     admsubmodule b,
			     admstorageautorisation c
			where a.sum_id = b.sum_id
			  and a.evt_id = c.evt_id
			  and b.sum_id = c.sum_id
			  and role_id = :profil
			  and a.evt_action is not null
			""", nativeQuery = true)
	List<Admeventtype> getActionsByRole(@Param("profil") Long profil);
}
