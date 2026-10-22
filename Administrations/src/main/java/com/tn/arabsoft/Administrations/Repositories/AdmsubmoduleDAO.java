package com.tn.arabsoft.administrations.repositories;

import java.util.List;

import com.tn.arabsoft.administrations.entities.Admsubmodule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdmsubmoduleDAO extends JpaRepository<Admsubmodule, Long> {

	@Query(value = """
			select a.sum_id,
			       a.sum_name,
			       a.sum_rank,
			       a.mod_id,
			       a.sum_status,
			       a.sum_rout,
			       a.mdl_icon,
			       a.mdl_icon1,
			       a.collapseid
			from admsubmodule a
			where a.sum_id in (
			    select distinct sum_id
			    from admstorageautorisation
			    where role_id = :roleId
			)
			and a.sum_status = 'O'
			order by a.sum_rank asc
			""", nativeQuery = true)
	public List<Admsubmodule> getListSubmoduleByRole(@Param("roleId") Long roleId);

	@Query(value = """
			select a.sum_id,
			       a.sum_name,
			       a.sum_rank,
			       a.mod_id,
			       a.sum_status,
			       a.sum_rout,
			       a.mdl_icon,
			       a.mdl_icon1,
			       a.collapseid
			from admsubmodule a
			order by a.sum_rank asc
			""", nativeQuery = true)
	public List<Admsubmodule> getListSubmoduleAdminis();

}
