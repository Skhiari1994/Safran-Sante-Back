package com.arabsoft.reports.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.reports.entities.ParamRub;

public interface ParamRubDao extends JpaRepository<ParamRub, Long> {

	@Query(value = "select * from param_rapport_rub where cod_rap = :codRap", nativeQuery = true)
	public List<ParamRub> getParams(@Param("codRap") Long codRap);

	@Query(value = "select seq_param_rapport_rub.nextval from dual", nativeQuery = true)
	Long getNextSequenceValue();

}
