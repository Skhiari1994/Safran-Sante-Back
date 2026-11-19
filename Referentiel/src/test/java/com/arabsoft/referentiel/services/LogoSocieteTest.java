package com.arabsoft.referentiel.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LogoSocieteTest {

	private static final byte[] PNG = { (byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A, 0, 0, 0, 0 };
	private static final byte[] JPEG = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0x10 };
	private static final byte[] WEBP = { 'R', 'I', 'F', 'F', 1, 2, 3, 4, 'W', 'E', 'B', 'P', 'V', 'P', '8', ' ' };

	@Test
	void reconnaitLesTroisFormatsAutorises() {
		assertEquals("image/png", LogoSociete.typeMime(PNG));
		assertEquals("image/jpeg", LogoSociete.typeMime(JPEG));
		assertEquals("image/webp", LogoSociete.typeMime(WEBP));
		assertNull(LogoSociete.erreur(PNG));
		assertNull(LogoSociete.erreur(JPEG));
		assertNull(LogoSociete.erreur(WEBP));
	}

	@Test
	void refuseLeSvgLesFichiersTexteEtLesImagesDeguisees() {
		byte[] svg = "<svg xmlns=\"http://www.w3.org/2000/svg\"><script>alert(1)</script></svg>".getBytes();
		byte[] html = "<html><script>alert(1)</script></html>".getBytes();
		byte[] gif = { 'G', 'I', 'F', '8', '9', 'a', 1, 0, 1, 0 };
		for (byte[] interdit : new byte[][] { svg, html, gif }) {
			assertNull(LogoSociete.typeMime(interdit));
			assertNotNull(LogoSociete.erreur(interdit));
		}
	}

	@Test
	void refuseUnFichierVideOuTropGros() {
		assertEquals("Le fichier est vide.", LogoSociete.erreur(new byte[0]));
		assertEquals("Le fichier est vide.", LogoSociete.erreur(null));

		byte[] limite = new byte[LogoSociete.TAILLE_MAX];
		System.arraycopy(PNG, 0, limite, 0, PNG.length);
		assertNull(LogoSociete.erreur(limite));

		byte[] trop = new byte[LogoSociete.TAILLE_MAX + 1];
		System.arraycopy(PNG, 0, trop, 0, PNG.length);
		assertEquals("Le logo dépasse 5 Mo. Choisissez une image plus légère.", LogoSociete.erreur(trop));
	}

	@Test
	void unFichierTropCourtNEstPasReconnu() {
		assertNull(LogoSociete.typeMime(new byte[] { (byte) 0x89, 'P' }));
		assertNull(LogoSociete.typeMime(new byte[] { 'R', 'I', 'F', 'F' }));
	}
}
