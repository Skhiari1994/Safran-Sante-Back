package com.arabsoft.referentiel.services;

/**
 * Logo d'une société : image PNG, JPEG ou WebP de 5 Mo au plus. Le format est reconnu d'après les premiers octets du
 * fichier (et non d'après son nom ou le type déclaré par le navigateur, falsifiables). Le SVG est volontairement refusé :
 * il peut contenir du script.
 */
public final class LogoSociete {

	/** Taille maximale d'un logo, en octets. */
	public static final int TAILLE_MAX = 5 * 1024 * 1024;

	private LogoSociete() {
	}

	/** @return le type MIME reconnu (image/png, image/jpeg, image/webp) ou {@code null} si le format n'est pas pris en charge */
	public static String typeMime(byte[] octets) {
		if (octets == null) {
			return null;
		}
		if (octets.length >= 8 && (octets[0] & 0xFF) == 0x89 && octets[1] == 'P' && octets[2] == 'N' && octets[3] == 'G'
				&& octets[4] == 0x0D && octets[5] == 0x0A && octets[6] == 0x1A && octets[7] == 0x0A) {
			return "image/png";
		}
		if (octets.length >= 3 && (octets[0] & 0xFF) == 0xFF && (octets[1] & 0xFF) == 0xD8 && (octets[2] & 0xFF) == 0xFF) {
			return "image/jpeg";
		}
		if (octets.length >= 12 && octets[0] == 'R' && octets[1] == 'I' && octets[2] == 'F' && octets[3] == 'F'
				&& octets[8] == 'W' && octets[9] == 'E' && octets[10] == 'B' && octets[11] == 'P') {
			return "image/webp";
		}
		return null;
	}

	/** @return {@code null} si le fichier est un logo valide, sinon le message d'erreur à afficher à l'utilisateur */
	public static String erreur(byte[] octets) {
		if (octets == null || octets.length == 0) {
			return "Le fichier est vide.";
		}
		if (octets.length > TAILLE_MAX) {
			return "Le logo dépasse 5 Mo. Choisissez une image plus légère.";
		}
		if (typeMime(octets) == null) {
			return "Format non pris en charge : utilisez une image PNG, JPEG ou WebP.";
		}
		return null;
	}
}
