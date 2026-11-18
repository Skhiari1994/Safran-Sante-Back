package com.tn.arabsoft.administrations.services;

/**
 * Valeurs optionnelles d'une entrée de menu (route d'un module, action d'un sous-menu) : l'écran envoie un texte vide
 * pour un champ non renseigné, mais la base doit contenir {@code NULL} (plusieurs requêtes testent
 * {@code evt_action IS NOT NULL} pour savoir si une entrée mène à une page).
 */
public final class TexteMenu {

	private TexteMenu() {
	}

	/** @return {@code null} si le texte est absent, vide ou ne contient que des espaces ; sinon le texte inchangé */
	public static String videEnNull(String texte) {
		return texte == null || texte.isBlank() ? null : texte;
	}
}
