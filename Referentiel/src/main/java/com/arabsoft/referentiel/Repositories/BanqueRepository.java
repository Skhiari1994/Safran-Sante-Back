package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Banque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanqueRepository extends JpaRepository<Banque, String> {

}
