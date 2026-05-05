package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleDao extends JpaRepository<Role, Long> {

	@Query(value = "select id ,name  from role where name=:name", nativeQuery = true)
	public Role getRoleName(@Param("name") String name);

	public Role findByName(String name);
}
