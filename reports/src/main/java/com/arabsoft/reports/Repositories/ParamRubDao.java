package com.arabsoft.reports.Repositories;

import java.util.List;

import com.arabsoft.reports.Entities.ParamRub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




public interface ParamRubDao extends JpaRepository<ParamRub, Long>{

	
	@Query(value="select * from param_rapport_rub where cod_rap=:codRap",nativeQuery=true)
	public List<ParamRub> getParams(@Param("codRap") Long codRap);
	@Query(value = "SELECT seq_param_rapport_rub.NEXTVAL FROM dual", nativeQuery = true)
	Long getNextSequenceValue();

}
