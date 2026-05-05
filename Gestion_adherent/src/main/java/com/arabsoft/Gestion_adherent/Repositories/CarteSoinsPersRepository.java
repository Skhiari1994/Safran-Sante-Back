package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.CarteSoinsPers;
import com.arabsoft.gestion_adherent.entities.cle.CleCarteSoinsPers;

import java.util.List;

public interface CarteSoinsPersRepository extends JpaRepository<CarteSoinsPers, CleCarteSoinsPers> {

    @Query(value = "select * from carte_soins_pers f where cod_soc = :codSoc and mat_pers = :mat", nativeQuery = true)
    public List<CarteSoinsPers> getCarteSoinsPers(@Param("codSoc") String codSoc, @Param("mat") String mat);
}
