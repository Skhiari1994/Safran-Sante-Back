package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.Agence;
import com.arabsoft.Gestion_adherent.Entities.Banque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BanqueRepository extends JpaRepository<Banque,String> {

}
