package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.PhotoPers;

public interface PhotoPersRepository extends JpaRepository<PhotoPers, Long> {

    @Query(value = "select * from photo_pers where cod_soc = :soc and mat_pers = :mat", nativeQuery = true)
    PhotoPers getPhotoPersonnel(@Param("soc") String soc, @Param("mat") String mat);

}
