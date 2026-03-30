package com.arabsoft.reports.Repositories;

import com.arabsoft.reports.Entities.RapportRub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface RapportRubDao extends JpaRepository<RapportRub, Long>  {
	
	@Query(value="select * from rapport_rub where cod_rap=:codRap",nativeQuery=true)
	public RapportRub getRapport(@Param("codRap") Long codRap);
}
