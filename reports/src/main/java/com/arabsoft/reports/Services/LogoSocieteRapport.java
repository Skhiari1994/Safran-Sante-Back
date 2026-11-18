package com.arabsoft.reports.services;

import java.awt.Image;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.util.JRImageLoader;

/**
 * Logo de la société dans les rapports. Le service ajoute automatiquement le paramètre {@value #PARAMETRE} (une
 * {@code java.awt.Image}) à tout rapport dont les paramètres contiennent un code société : un état n'a plus qu'à déclarer
 * ce paramètre et à l'afficher, sans modifier sa requête. Le logo est lu une seule fois par impression.
 *
 * Dans le fichier .jrxml :
 * <pre>
 * &lt;parameter name="LOGO_SOC" class="java.awt.Image"/&gt;
 * &lt;image scaleImage="RetainShape" hAlign="Left" vAlign="Middle" isUsingCache="true" onErrorType="Blank"&gt;
 *   &lt;reportElement x="0" y="0" width="120" height="50"/&gt;
 *   &lt;imageExpression class="java.awt.Image"&gt;&lt;![CDATA[$P{LOGO_SOC}]]&gt;&lt;/imageExpression&gt;
 * &lt;/image&gt;
 * </pre>
 */
public final class LogoSocieteRapport {

	/** Nom du paramètre Jasper qui reçoit le logo. */
	public static final String PARAMETRE = "LOGO_SOC";

	/** Noms (en minuscules) sous lesquels les rapports reçoivent le code société. */
	private static final Set<String> NOMS_CODE_SOCIETE = Set.of("cod_soc", "codsoc", "soc", "soc_", "cod");

	private LogoSocieteRapport() {
	}

	/** @return le code société trouvé dans les paramètres du rapport, ou {@code null} s'il n'y en a pas */
	public static String codeSociete(Map<String, String> params) {
		if (params == null) {
			return null;
		}
		for (Map.Entry<String, String> parametre : params.entrySet()) {
			String nom = parametre.getKey();
			String valeur = parametre.getValue();
			if (nom != null && valeur != null && !valeur.isBlank() && NOMS_CODE_SOCIETE.contains(nom.toLowerCase())) {
				return valeur.trim();
			}
		}
		return null;
	}

	/** @return l'image décodée, ou {@code null} si les octets ne sont pas une image lisible */
	public static Image decoder(byte[] octets) {
		if (octets == null || octets.length == 0) {
			return null;
		}
		try {
			return JRImageLoader.loadImage(octets);
		} catch (Exception e) {
			return null;
		}
	}
}
