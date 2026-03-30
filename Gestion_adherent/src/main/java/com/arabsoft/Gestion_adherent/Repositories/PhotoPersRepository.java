package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.PhotoPers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PhotoPersRepository extends JpaRepository<PhotoPers,Long> {
    @Query(value="select * from photo_pers where cod_soc=:soc and mat_pers=:mat",nativeQuery = true)
    PhotoPers getPhotoPersonnel(@Param("soc") String soc,@Param("mat") String mat);
}
