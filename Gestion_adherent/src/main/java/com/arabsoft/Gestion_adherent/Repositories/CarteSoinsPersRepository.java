package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.CarteSoinsPers;
import com.arabsoft.Gestion_adherent.Entities.Cle.CleCarteSoinsPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarteSoinsPersRepository extends JpaRepository<CarteSoinsPers, CleCarteSoinsPers> {

    @Query(value="select * from Carte_soins_pers f where  cod_soc =:codSoc and mat_pers =:mat ",nativeQuery=true)
    public List<CarteSoinsPers> getCarteSoinsPers(@Param("codSoc") String codSoc, @Param("mat") String mat);
}
