package com.arabsoft.Gestion_adherent.Repositories;

 import com.arabsoft.Gestion_adherent.Entities.PrmLieuGeographique;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrmLieuGeogRepository extends JpaRepository<PrmLieuGeographique,String> {
}
