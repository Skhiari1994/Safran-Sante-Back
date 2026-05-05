package com.tn.arabsoft.administrations.repositories;

import java.util.List;

import com.tn.arabsoft.administrations.entities.Admsubmodule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdmsubmoduleDAO extends JpaRepository<Admsubmodule, Long> {

	@Query(value = "select sum_id, sum_name, sum_rank, mod_id, sum_status, sum_rout, mdl_icon,mdl_icon1,collapseid from Admsubmodule "
			+
			"where sum_id in (select distinct sum_id from admstorageautorisation where role_id=:roleId) and sum_status='O' order by sum_rank asc", nativeQuery = true)
	public List<Admsubmodule> getListSubmoduleByRole(@Param("roleId") Long roleId);

	@Query(value = "select sum_id, sum_name, sum_rank, mod_id, sum_status, sum_rout, mdl_icon,mdl_icon1,collapseid from Admsubmodule order by sum_rank asc", nativeQuery = true)
	public List<Admsubmodule> getListSubmoduleAdminis();
}
