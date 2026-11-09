package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.CarteSoinsCnam;
import com.arabsoft.gestion_adherent.entities.cle.CleCarteSoinsCnam;

import java.util.List;

public interface CarteSoinsCnamRepository extends JpaRepository<CarteSoinsCnam, CleCarteSoinsCnam> {

    @Query(value = "select * from carte_soins_cnam where cod_soc = :codSoc and mat_pers = :mat order by date_fin desc", nativeQuery = true)
    public List<CarteSoinsCnam> getCarteSoinsCnam(@Param("codSoc") String codSoc, @Param("mat") String mat);

    @Query(value = "select * from carte_soins_cnam where mat_pers = :mat order by date_fin desc", nativeQuery = true)
    public List<CarteSoinsCnam> getCarteSoinsCnamParMatricule(@Param("mat") String mat);
}
