package com.arabsoft.Gestion_adherent.Services;

import com.arabsoft.Gestion_adherent.DTO.ResponseProcedureCharge;
import com.arabsoft.Gestion_adherent.Entities.FichSal;
import com.arabsoft.Gestion_adherent.Entities.ResponseProcedure;
import com.arabsoft.Gestion_adherent.Repositories.FichSalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChargFichierService {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    FichSalRepository fichSalRepository;

    public ResponseProcedureCharge lireFichierEtAppelerProcedure(String soc, String mois, MultipartFile file)
            throws Exception {
        List<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream()))
                .lines()
                .collect(Collectors.toList());

        int totalLignes = 0;

        for (String line : lines) {
            if (line.trim().startsWith("FIN"))
                break;
            totalLignes += appelerProcedureOraclePourLigne(soc, mois, line);
        }

        ResponseProcedureCharge resp = new ResponseProcedureCharge();
        resp.setMessage("Fichier importé avec succès");
        resp.setLignesTransferees(totalLignes);

        return resp;
    }

    private int appelerProcedureOraclePourLigne(String soc, String mois, String ligne) throws SQLException {
        int lignesTransferees = 0;
        try (Connection conn = dataSource.getConnection()) {
            try (CallableStatement cs = conn
                    .prepareCall("{ call PK_GESTION_CREDIT.lecture_disk_pret_actif_proc(?,?,?,?) }")) {
                cs.setString(1, soc);
                cs.setString(2, mois);
                cs.setString(3, ligne);
                cs.registerOutParameter(4, java.sql.Types.INTEGER); // OUT param pour i
                cs.execute();
                lignesTransferees = cs.getInt(4);
            }
        }
        return lignesTransferees;
    }

    public ResponseProcedure fichier_salarie(String wcodSoc, String nom_fichier, String etat_act) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_GESTION_CREDIT.fichier_salarie(?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, nom_fichier); // IN parameter
                callableStatement.setString(3, etat_act); // IN parameter
                callableStatement.registerOutParameter(4, Types.VARCHAR);
                callableStatement.execute(); // Exécuted la procédure stockée
                ResponseProcedure reponse = new ResponseProcedure();

                String message = callableStatement.getString(4);

                reponse.setMessage(message);
                return reponse; // Récupération de la date de naissance
            }
        });
    }

    public byte[] getVirementFileBytes() {
        List<FichSal> virements = fichSalRepository.getAllFichSal();
        StringBuilder sb = new StringBuilder();
        for (FichSal virement : virements) {
            String line = virement.getLigne();
            if (line != null) {
                sb.append(line).append("\r\n");
            }
        }
        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public File generateVirementFile(String dirPath, String fileName) throws IOException {

        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        File file = dir.resolve(fileName).toFile();

        List<FichSal> virements = fichSalRepository.getAllFichSal();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (FichSal virement : virements) {
                if (virement.getLigne() != null) {
                    writer.write(virement.getLigne());
                    writer.newLine();
                }
            }
        }

        return file;
    }
}
