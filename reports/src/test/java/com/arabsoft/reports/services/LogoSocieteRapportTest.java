package com.arabsoft.reports.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LogoSocieteRapportTest {

	@BeforeAll
	static void modeSansEcran() {
		System.setProperty("java.awt.headless", "true");
	}

	@Test
	void trouveLeCodeSocieteSousLesNomsUtilisesParLesRapports() {
		for (String nom : new String[] { "cod_soc", "COD_SOC", "soc", "codSoc", "soc_", "cod" }) {
			assertEquals("01", LogoSocieteRapport.codeSociete(Map.of(nom, "01", "bord", "C2026700")), nom);
		}
	}

	@Test
	void ignoreLesRapportsSansCodeSocieteOuAvecUneValeurVide() {
		assertNull(LogoSocieteRapport.codeSociete(null));
		assertNull(LogoSocieteRapport.codeSociete(Map.of()));
		assertNull(LogoSocieteRapport.codeSociete(Map.of("bord", "C2026700", "mat_deb", "1")));
		Map<String, String> vide = new HashMap<>();
		vide.put("cod_soc", "  ");
		vide.put("soc", null);
		assertNull(LogoSocieteRapport.codeSociete(vide));
	}

	@Test
	void decodeUnVraiLogoPng() throws Exception {
		BufferedImage source = new BufferedImage(120, 40, BufferedImage.TYPE_INT_ARGB);
		ByteArrayOutputStream png = new ByteArrayOutputStream();
		ImageIO.write(source, "png", png);

		Image image = LogoSocieteRapport.decoder(png.toByteArray());

		assertNotNull(image);
		assertEquals(120, image.getWidth(null));
		assertEquals(40, image.getHeight(null));
	}

	@Test
	void unFichierQuiNEstPasUneImageDonnePasDeLogo() {
		assertNull(LogoSocieteRapport.decoder(null));
		assertNull(LogoSocieteRapport.decoder(new byte[0]));
		assertNull(LogoSocieteRapport.decoder("pas une image".getBytes()));
	}

	private static final String JRXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
			+ "xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" "
			+ "language=\"groovy\" name=\"essai\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">"
			+ "<parameter name=\"LOGO_SOC\" class=\"java.awt.Image\"/>"
			+ "<title><band height=\"60\">"
			+ "<image scaleImage=\"RetainShape\" hAlign=\"Left\" vAlign=\"Middle\" isUsingCache=\"true\" onErrorType=\"Blank\">"
			+ "<reportElement x=\"0\" y=\"0\" width=\"120\" height=\"50\"/>"
			+ "<imageExpression class=\"java.awt.Image\"><![CDATA[$P{LOGO_SOC}]]></imageExpression>"
			+ "</image></band></title></jasperReport>";

	private static byte[] imprimer(Image logo) throws Exception {
		net.sf.jasperreports.engine.JasperReport rapport = net.sf.jasperreports.engine.JasperCompileManager
				.compileReport(new java.io.ByteArrayInputStream(JRXML.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
		Map<String, Object> parametres = new HashMap<>();
		if (logo != null) {
			parametres.put(LogoSocieteRapport.PARAMETRE, logo);
		}
		net.sf.jasperreports.engine.JasperPrint impression = net.sf.jasperreports.engine.JasperFillManager
				.fillReport(rapport, parametres, new net.sf.jasperreports.engine.JREmptyDataSource(1));
		return net.sf.jasperreports.engine.JasperExportManager.exportReportToPdf(impression);
	}

	@Test
	void lImageDuParametreApparaitDansLePdfEtUnLogoAbsentNeFaitPasEchouerLEtat() throws Exception {
		BufferedImage source = new BufferedImage(240, 100, BufferedImage.TYPE_INT_RGB);
		java.awt.Graphics2D g = source.createGraphics();
		g.setPaint(new java.awt.GradientPaint(0, 0, java.awt.Color.BLUE, 240, 100, java.awt.Color.ORANGE));
		g.fillRect(0, 0, 240, 100);
		g.dispose();
		ByteArrayOutputStream png = new ByteArrayOutputStream();
		ImageIO.write(source, "png", png);

		byte[] avecLogo = imprimer(LogoSocieteRapport.decoder(png.toByteArray()));
		byte[] sansLogo = imprimer(null);

		assertEquals("%PDF", new String(avecLogo, 0, 4));
		assertEquals("%PDF", new String(sansLogo, 0, 4));
		org.junit.jupiter.api.Assertions.assertTrue(avecLogo.length > sansLogo.length,
				"le PDF avec logo doit contenir l'image (" + avecLogo.length + " octets contre " + sansLogo.length + ")");
	}
}
