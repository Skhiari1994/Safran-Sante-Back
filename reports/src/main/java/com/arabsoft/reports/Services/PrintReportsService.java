package com.arabsoft.reports.Services;

import com.arabsoft.reports.Entities.RapportRub;
import com.arabsoft.reports.Entities.ReponseGenererFich;
import com.arabsoft.reports.Repositories.RapportRubDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.JRExporterParameter;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrintReportsService {

	@Autowired
	private ConnectionBdReport bdReport;
	@Autowired
	RapportRubDao rubDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public byte[] genererRapportPDF(Long numRap, Map<String, String> params) throws JRException, IOException {
		JasperPrint jasperPrint = prepareJasperPrint(numRap, params);
		if (jasperPrint == null)
			return null;

		try {
			byte[] rapportPDF = JasperExportManager.exportReportToPdf(jasperPrint);

			// Facultatif : Sauvegarde sur le disque comme dans le code original
			RapportRub rapport = rubDao.getRapport(numRap);
			String nom = rapport.getLib_rap_jsp() + System.currentTimeMillis();
			String path = "C:/Presidence_Rapport";
			File outputDir = new File(path);
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}
			String outputPath = path + "\\" + nom + ".pdf";
			JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

			return rapportPDF;
		} catch (JRException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] genererRapportExcel(Long numRap, Map<String, String> params) throws JRException, IOException {
		JasperPrint jasperPrint = prepareJasperPrint(numRap, params);
		if (jasperPrint == null)
			return null;

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);

			exporter.exportReport();
			byte[] bytes = baos.toByteArray();

			// Sauvegarde sur le disque pour vérification
			try {
				RapportRub rapport = rubDao.getRapport(numRap);
				String nom = rapport.getLib_rap_jsp() + "_" + System.currentTimeMillis();
				String path = "C:/Presidence_Rapport";
				File outputDir = new File(path);
				if (!outputDir.exists())
					outputDir.mkdirs();
				try (FileOutputStream fos = new FileOutputStream(path + "\\" + nom + ".xlsx")) {
					fos.write(bytes);
				}
				System.out.println("Excel sauvegardé sur : " + path + "\\" + nom + ".xlsx");
			} catch (Exception outEx) {
				System.err.println("Erreur sauvegarde disque : " + outEx.getMessage());
			}

			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private JasperPrint prepareJasperPrint(Long numRap, Map<String, String> params) throws JRException, IOException {
		Connection conn = null;
		try {
			RapportRub rapport = rubDao.getRapport(numRap);
			if (rapport == null) {
				throw new Exception("Rapport non trouvé pour numRap: " + numRap);
			}

			String filePath = rapport.getPath_report() + "\\" + rapport.getLib_rap_jsp() + ".jrxml";
			System.out.println("Chemin du fichier JRXML : " + filePath);

			File jrxmlFile = new File(filePath);
			if (!jrxmlFile.exists()) {
				throw new FileNotFoundException("Fichier JRXML introuvable : " + filePath);
			}

			InputStream jrxmlStream = new FileInputStream(jrxmlFile);
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

			// Construction des paramètres
			Map<String, Object> parameters = new HashMap<>();
			if (params != null) {
				params.forEach(parameters::put);
			}
			if (rapport.getTitre() != null) {
				parameters.put("titre", rapport.getTitre());
			}

			conn = bdReport.BdReport();
			return JasperFillManager.fillReport(jasperReport, parameters, conn);
		} catch (Exception ex) {
			System.err.println("Erreur lors de la préparation du rapport : " + ex.getMessage());
			ex.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ReponseGenererFich generer_fich(String soc, String mois, String corps, String wcodGrpPret, String wtypPret) {

		return jdbcTemplate.execute((Connection connection) -> {
			String procedureCall = "{call pk_gestion_credit.generer_fich(?, ?, ?, ?, ?,?)}";
			try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
				callableStatement.setString(1, soc); // IN parameter
				callableStatement.setString(2, mois); // IN parameter
				callableStatement.setString(3, corps); // IN parameter
				callableStatement.setString(4, wcodGrpPret); // IN parameter
				callableStatement.setString(5, wtypPret); // IN parameter
				callableStatement.registerOutParameter(6, Types.VARCHAR);
				callableStatement.execute();
				String file_name = callableStatement.getString(6);
				ReponseGenererFich responseProcedure = new ReponseGenererFich();

				responseProcedure.setFileName(file_name);
				return responseProcedure;
			}
		});
	}

	public ResponseEntity<Resource> exporterExcel(String filename) throws Exception {
		List<String> lignes = jdbcTemplate.queryForList(
				"SELECT ligne FROM generer_fich ORDER BY ordre",
				String.class);

		Workbook workbook = new HSSFWorkbook(); // Pour .xls
		Sheet sheet = workbook.createSheet("Prêts");

		int rowNum = 0;
		for (String ligne : lignes) {
			Row row = sheet.createRow(rowNum++);
			String[] colonnes = ligne.split("\t");
			for (int i = 0; i < colonnes.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellValue(colonnes[i]);
			}
		}

		// Sauvegarde en mémoire
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);

		ByteArrayResource resource = new ByteArrayResource(bos.toByteArray());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentLength(resource.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

	public String getCron() {
		long codRap = 496;
		RapportRub re = rubDao.getRapport(codRap);
		String ss = re.getSecond_envoi();
		String min = re.getMinute_envoi();
		String hh = re.getHeure_envoi();
		String jj = re.getJour_envoi();
		String mm = re.getMois_envoi();
		String ann = re.getAnnee_envoi();
		String cron = ss + " " + min + " " + hh + " " + jj + " " + mm + " " + ann;
		System.out.println("cron" + cron);
		return cron;
	}

}
