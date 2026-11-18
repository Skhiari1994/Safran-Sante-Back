package com.tn.arabsoft.administrations.services;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Icône d'une entrée de menu : nom de classe d'une police d'icônes (ex. {@code ri-team-line} ou
 * {@code bx bx-registered}), jamais une image. Le format est contrôlé avant l'enregistrement car la valeur est
 * ensuite placée dans un attribut {@code class} par le front.
 */
public final class IconeMenu {

	/** Minuscules, chiffres, tirets et espaces ; 2 à 100 caractères, sans espace ni tiret aux extrémités. */
	private static final Pattern FORMAT = Pattern.compile("^[a-z0-9][a-z0-9 -]{0,98}[a-z0-9]$");

	private IconeMenu() {
	}

	/**
	 * @return l'icône nettoyée, ou {@code null} si elle est vide (le front choisit alors une icône par défaut)
	 * @throws ResponseStatusException 400 si le format est invalide
	 */
	public static String normaliser(String icone) {
		if (icone == null) {
			return null;
		}
		String nettoyee = icone.trim();
		if (nettoyee.isEmpty()) {
			return null;
		}
		if (!FORMAT.matcher(nettoyee).matches()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Icône invalide : lettres minuscules, chiffres, tirets et espaces uniquement (ex. ri-team-line).");
		}
		return nettoyee;
	}
}
