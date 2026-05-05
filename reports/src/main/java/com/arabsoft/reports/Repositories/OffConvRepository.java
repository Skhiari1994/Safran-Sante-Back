package com.arabsoft.reports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.reports.entities.OffConv;
import com.arabsoft.reports.entities.cle.CleOffConv;

import java.util.List;

public interface OffConvRepository extends JpaRepository<OffConv, CleOffConv> {

    @Query(value = "select * from off_conv where cod_conv=:conv", nativeQuery = true)
    List<OffConv> getOffConv(@Param("conv") String conv);
}
