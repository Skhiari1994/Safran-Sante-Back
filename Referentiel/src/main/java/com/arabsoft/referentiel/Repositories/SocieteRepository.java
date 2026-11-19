package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Societe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SocieteRepository extends JpaRepository<Societe, String> {

	/** Image du logo (colonne LOGO_SOC), {@code null} si la société n'en a pas. */
	@Query(value = "select logo_soc from societe where cod_soc = :cod", nativeQuery = true)
	byte[] findLogo(@Param("cod") String cod);

	@Transactional
	@Modifying
	@Query(value = "update societe set logo_soc = :logo where cod_soc = :cod", nativeQuery = true)
	int updateLogo(@Param("cod") String cod, @Param("logo") byte[] logo);

	@Transactional
	@Modifying
	@Query(value = "update societe set logo_soc = null where cod_soc = :cod", nativeQuery = true)
	int supprimerLogo(@Param("cod") String cod);
}
