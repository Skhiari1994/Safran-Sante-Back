package com.arabsoft.referentiel.Repositories;

 import com.arabsoft.referentiel.Entities.PrmLieuGeographique;
 import org.springframework.data.jpa.repository.JpaRepository;

public interface PrmLieuGeogRepository extends JpaRepository<PrmLieuGeographique,String> {
}
