package com.arabsoft.Credits.Services;

import com.arabsoft.Credits.DTO.LigPretDTO;
import com.arabsoft.Credits.Entities.*;
import com.arabsoft.Credits.Entities.Response.*;
import com.arabsoft.Credits.Repositories.LigPretRepository;
import com.arabsoft.Credits.Repositories.PretPersRepository;
import com.arabsoft.Credits.Repositories.VirCarteRepository;
import com.arabsoft.Credits.Repositories.VirementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CreditService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PretPersRepository pretPersRepository;
    @Autowired
    private VirementRepository virementRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private VirCarteRepository virCarteRepository;
    @Autowired
    private LigPretRepository ligPretRepository;
    public ReponseDatAcc verif_dat_acc(String wcodSoc, String mat_, String wcodGrpPret, String  wtypPret, String wdatEmb, String  wDatAcc) {

       /* java.sql.Date sqlWdatDebComm = (wdatDebComm != null && !wdatDebComm.isEmpty()) ? java.sql.Date.valueOf(wdatDebComm) : null;
        java.sql.Date sqlWdatFinComm = (wdatFinComm != null && !wdatFinComm.isEmpty()) ? java.sql.Date.valueOf(wdatFinComm) : null;
        System.out.println("sqlWdatDebComm :"+sqlWdatDebComm+"sqlWdatFinComm :"+sqlWdatFinComm);*/
        Date sqlWdatEmb = convertToSqlDate(wdatEmb);
        Date sqlWdatAcc = convertToSqlDate(wDatAcc);

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.verif_dat_acc(?, ?, ?, ?, ?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, mat_); // IN parameter
                callableStatement.setString(3, wcodGrpPret); // IN parameter
                callableStatement.setString(4, wtypPret); // IN parameter
                callableStatement.setDate(5, sqlWdatEmb); // IN parameter
                callableStatement.setDate(6, sqlWdatAcc); // IN parameter
                callableStatement.registerOutParameter(7, Types.VARCHAR); // UT parameter

                callableStatement.registerOutParameter(8, Types.INTEGER); // OUT parameter
                callableStatement.registerOutParameter(9, Types.VARCHAR); // OUT parameter

                callableStatement.execute();
                String anc=callableStatement.getString(7);
                Long nbre=callableStatement.getLong(8);
                String message=callableStatement.getString(9);
                ReponseDatAcc responseProcedure= new ReponseDatAcc();


                responseProcedure.setAnc(anc);
                responseProcedure.setNbre(nbre);
                responseProcedure.setMessage(message);
                return responseProcedure;
            }
        });
    }
    public ReponseDatDeb verif_dat_deb(String wcodSoc, Long wprtEch, String prt_dat_acc, String wdatDeb, String wdatRetr) {
        // Convert input Strings to SQL Date, ensuring null safety
        Date sqlWdatAcc = (prt_dat_acc != null && !prt_dat_acc.isEmpty()) ? convertToSqlDate(prt_dat_acc) : null;
        Date sqlWdatDeb = (wdatDeb != null && !wdatDeb.isEmpty()) ? convertToSqlDate(wdatDeb) : null;
        Date sqlWdatRetr = (wdatRetr != null && !wdatRetr.isEmpty()) ? convertToSqlDate(wdatRetr) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.verif_dat_deb(?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setLong(2, wprtEch); // IN parameter
                callableStatement.setDate(3, sqlWdatAcc); // IN parameter
                callableStatement.setDate(4, sqlWdatDeb); // IN parameter
                callableStatement.setDate(5, sqlWdatRetr); // IN parameter

                // Register output parameters
                callableStatement.registerOutParameter(6, Types.DATE); // OUT parameter for datFin
                callableStatement.registerOutParameter(7, Types.VARCHAR); // OUT parameter for message

                // Execute the procedure
                callableStatement.execute();

                // Retrieve output parameters
                Date sqlDatFin = callableStatement.getDate(6);
                String message = callableStatement.getString(7);

                // Convert SQL Date to LocalDate
                LocalDate datFin = (sqlDatFin != null) ? sqlDatFin.toLocalDate() : null;

                // Populate the response object
                ReponseDatDeb responseProcedure = new ReponseDatDeb();
                responseProcedure.setDatFin(datFin);
                responseProcedure.setMessage(message);

                return responseProcedure;
            } catch (SQLException e) {
                throw new RuntimeException("Error while executing stored procedure: " + e.getMessage(), e);
            }
        });
    }


    public ReponseCalculDetailCredit calcul_detail_credit(String wcodSoc, String wdatfin, String wdatretr, Double wprtmntglb, Double wprtech, Double wprttaux, BigDecimal wmntreport,String codGrpPret) {
        // Convert input Strings to SQL Date, ensuring null safety
        Date sqlWdatFin = (wdatfin != null && !wdatfin.isEmpty()) ? convertToSqlDate(wdatfin) : null;
        Date sqlWdatRetr = (wdatretr != null && !wdatretr.isEmpty()) ? convertToSqlDate(wdatretr) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.calcul_detail_credit(?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setDate(2, sqlWdatFin); // IN parameter
                callableStatement.setDate(3, sqlWdatRetr); // IN parameter
                callableStatement.setDouble(4, wprtmntglb); // IN parameter
                callableStatement.setDouble(5, wprtech); // IN parameter
                callableStatement.setDouble(6, wprttaux); // IN parameter
                callableStatement.setString(7, codGrpPret); // IN parameter

                // Handle null safety for wmntreport
                if (wmntreport != null) {
                    callableStatement.setBigDecimal(8, wmntreport); // IN parameter
                } else {
                    callableStatement.setNull(8, Types.BIGINT); // Handle null value
                }

                // Register output parameters
                callableStatement.registerOutParameter(9, Types.NUMERIC); // OUT parameter for wprInteret
                callableStatement.registerOutParameter(10, Types.NUMERIC); // OUT parameter for wprtMntRem
                callableStatement.registerOutParameter(11, Types.NUMERIC); // OUT parameter for wremMen
                callableStatement.registerOutParameter(12, Types.NUMERIC); // OUT parameter for wdernRemMen
                callableStatement.registerOutParameter(13, Types.NUMERIC); // OUT parameter for wprtRendu
                callableStatement.registerOutParameter(14, Types.NUMERIC); // OUT parameter for wnbrRetenue
                callableStatement.registerOutParameter(15, Types.VARCHAR); // OUT parameter for message

                // Execute the procedure
                callableStatement.execute();

                // Fetch the values of the output parameters
                BigDecimal wprInteret = callableStatement.getBigDecimal(9);
                BigDecimal wprtMntRem = callableStatement.getBigDecimal(10);
                BigDecimal wremMen = callableStatement.getBigDecimal(11);
                BigDecimal wdernRemMen = callableStatement.getBigDecimal(12);
                BigDecimal wprtRendu = callableStatement.getBigDecimal(13);
                BigDecimal wnbrRetenue = callableStatement.getBigDecimal(14);
                String message = callableStatement.getString(15);

                // Create DecimalFormat with custom symbols for dot separator
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.'); // Use dot as the decimal separator

                DecimalFormat decimalFormat = new DecimalFormat("0.000", symbols); // Format with three decimals

                // Format the values
                String formattedWprInteret = decimalFormat.format(wprInteret);
                String formattedWprtMntRem = decimalFormat.format(wprtMntRem);
                String formattedWremMen = decimalFormat.format(wremMen);
                String formattedWdernRemMen = decimalFormat.format(wdernRemMen);
                String formattedWprtRendu = decimalFormat.format(wprtRendu);
                String formattedWnbrRetenue = decimalFormat.format(wnbrRetenue);

                // Populate the response object
                ReponseCalculDetailCredit responseProcedure = new ReponseCalculDetailCredit();
                responseProcedure.setWprInteret(formattedWprInteret);
                responseProcedure.setWremMen(formattedWremMen);
                responseProcedure.setWprtMntRem(formattedWprtMntRem);
                responseProcedure.setWdernRemMen(formattedWdernRemMen);
                responseProcedure.setWprtRendu(formattedWprtRendu);
                responseProcedure.setWnbrRetenue(wnbrRetenue);
                responseProcedure.setMessage(message);

                return responseProcedure;
            } catch (SQLException e) {
                throw new RuntimeException("Error while executing stored procedure: " + e.getMessage(), e);
            }
        });
    }
    public VerifPretResponse calc_mnt_pret_ant(
            String codSoc,
            String matPers,
            String codPret,
            String codPretAnt,
            String codGrpPret,
            String typPret) {

        return jdbcTemplate.execute((Connection connection) -> {

            String procedureCall = "{call pk_gestion_credit.calc_mnt_pret_ant(?, ?, ?, ?, ?, ?, ?)}";

            try (CallableStatement cs = connection.prepareCall(procedureCall)) {

                cs.setString(1, codSoc);
                cs.setString(2, matPers);
                cs.setString(3, codPret);
                cs.setString(4, codPretAnt);
                cs.setString(5, codGrpPret);
                cs.setString(6, typPret);

                cs.registerOutParameter(7, Types.NUMERIC);

                cs.execute();

                BigDecimal montant = cs.getBigDecimal(7);

                VerifPretResponse response = new VerifPretResponse();
                response.setMntReport(
                        montant != null
                                ? new DecimalFormat("0.000",
                                DecimalFormatSymbols.getInstance(Locale.US)).format(montant)
                                : "0.000"
                );

                return response;

            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure", e);
            }
        });
    }
    public VerifPretResponse verifPretCours(String wcodSoc, String wmatPers, String wcodGrpPret, String wtypPret, String prtDatDeb) {
        Date wprt_dat_deb_ = (prtDatDeb != null && !prtDatDeb.isEmpty()) ? convertToSqlDate(prtDatDeb) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.verif_pret_cours(?, ?, ?, ?, ?, ?, ?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                // 1️⃣ Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, wcodGrpPret);
                callableStatement.setString(4, wtypPret);
                callableStatement.setDate(5, wprt_dat_deb_);

                // 2️⃣ Register output parameters
                callableStatement.registerOutParameter(6, Types.NUMERIC); // wcod_pret_ant
                callableStatement.registerOutParameter(7, Types.NUMERIC); // wmnt_report
                callableStatement.registerOutParameter(8, Types.VARCHAR); // message

                // 3️⃣ Execute
                callableStatement.execute();

                // 4️⃣ Fetch output values
                Long codPretAnt = callableStatement.getLong(6);
                BigDecimal rawMntReport = callableStatement.getBigDecimal(7);
                String message = callableStatement.getString(8);

                // 5️⃣ Format amount safely
                String formattedMntReport = (rawMntReport != null)
                        ? new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.US)).format(rawMntReport)
                        : "0.000";

                // 6️⃣ Populate response
                VerifPretResponse response = new VerifPretResponse();
                response.setCodPretAnt(codPretAnt);
                response.setMntReport(formattedMntReport);
                response.setMessage(message);

                return response;

            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }


    public void insert_remb_tranch(String soc, String wmat_pers, String wcod_pret, String wcod_etat_pret, String wprt_dat_acc,String wprt_mnt_rem,Long wnbr_tranche) {

        Date sqlWprt_dat_acc = (wprt_dat_acc != null && !wprt_dat_acc.isEmpty()) ? convertToSqlDate(wprt_dat_acc) : null;

        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{CALL pk_gestion_credit.insert_remb_tranch(?, ?,?,?,?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, wmat_pers);
                    callableStatement.setString(3, wcod_pret);
                    callableStatement.setString(4, wcod_etat_pret);
                    callableStatement.setDate(5, sqlWprt_dat_acc);
                    callableStatement.setString(6, wprt_mnt_rem);
                    callableStatement.setLong(7, wnbr_tranche);

                    return callableStatement;
                },
                Collections.emptyList()
        );

        // Récupérer la valeur du paramètre OUT

    }

    public void insert_lig_pret(String soc, String wmat_pers, String wcod_pret, String wprt_dat_deb, String wprt_dat_fin,BigDecimal wprt_interet,BigDecimal wprt_ech,BigDecimal wprt_mnt_rem,BigDecimal wrem_men,BigDecimal wdern_rem_men) {

        Date sqlWprt_dat_deb = (wprt_dat_deb != null && !wprt_dat_deb.isEmpty()) ? convertToSqlDate(wprt_dat_deb) : null;
        Date sqlWprt_dat_fin = (wprt_dat_fin != null && !wprt_dat_fin.isEmpty()) ? convertToSqlDate(wprt_dat_fin) : null;

        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{CALL pk_gestion_credit.insert_lig_pret(?, ?,?,?,?,?,?,?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, wmat_pers);
                    callableStatement.setString(3, wcod_pret);
                    callableStatement.setDate(4, sqlWprt_dat_deb);
                    callableStatement.setDate(5, sqlWprt_dat_fin);
                    callableStatement.setBigDecimal(6, wprt_interet);
                    callableStatement.setBigDecimal(7, wprt_ech);
                    callableStatement.setBigDecimal(8, wprt_mnt_rem);
                    callableStatement.setBigDecimal(9, wrem_men);
                    callableStatement.setBigDecimal(10, wdern_rem_men);

                    return callableStatement;
                },
                Collections.emptyList()
        );

        // Récupérer la valeur du paramètre OUT

    }

    public void maj_lig_pret(String soc, String wmat_pers, String wcod_grp_pret, String corps_, String mois_) {


        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{CALL pk_gestion_credit.maj_lig_pret(?,?,?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, wmat_pers);
                    callableStatement.setString(3, wcod_grp_pret);
                    callableStatement.setString(4, corps_);
                    callableStatement.setString(5, mois_);


                    return callableStatement;
                },
                Collections.emptyList()
        );

        // Récupérer la valeur du paramètre OUT

    }
    public void vir_carte(String soc, Long numComm, String datDeblc) {
        String procedureCall = "{call pk_gestion_credit.vir_carte(?, ?, ?, ?)}";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Définition des paramètres d'entrée
            callableStatement.setString(1, soc);
            callableStatement.setLong(2, numComm);
            callableStatement.setString(3, datDeblc);

            // Définition du paramètre de sortie (mais non utilisé)
            callableStatement.registerOutParameter(4, Types.VARCHAR);

            // Exécuter la procédure
            callableStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'appel de la procédure : " + e.getMessage(), e);
        }
    }

    public String generateVirementFile(String filePath) {
        List<Virement> virements = virementRepository.findAllByOrderByOrdreAsc();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Virement virement : virements) {
                writer.write(virement.getLigne());
                writer.newLine(); // Nouvelle ligne
            }
            return "Fichier généré avec succès : " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier";
        }
    }
    public String generateVirCarteFile(String filePath) {
        List<VirCarte> virements = virCarteRepository.findAllByOrderByOrdreAsc();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (VirCarte virement : virements) {
                writer.write(virement.getLigne());
                writer.newLine(); // Nouvelle ligne
            }
            return "Fichier généré avec succès : ";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier";
        }
    }
    public void deblocage_carte(String soc,
                                LocalDate datDeblc,
                                String numComm,
                                String refMetier,
                                LocalDate datOp) {

        String procedureCall = "{call pk_gestion_credit.deblocage_carte(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Convert LocalDate → java.sql.Date
            java.sql.Date sqlDatDeblc = java.sql.Date.valueOf(datDeblc);
            java.sql.Date sqlDatOp = java.sql.Date.valueOf(datOp);

            // IN parameters
            callableStatement.setString(1, soc);
            callableStatement.setDate(2, sqlDatDeblc);
            callableStatement.setString(3, numComm);
            callableStatement.setString(4, refMetier);
            callableStatement.setDate(5, sqlDatOp);

            // OUT parameter
            callableStatement.registerOutParameter(6, Types.VARCHAR);

            // Execute the stored procedure
            callableStatement.execute();

            // Optional: retrieve the OUT message
            String resultMessage = callableStatement.getString(6);
            System.out.println("Procedure message: " + resultMessage);

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'appel de la procédure : " + e.getMessage(), e);
        }
    }

    public void CHARGEMENT_RETENUE_MENSUEL(String soc, String mois, String mat) {
        String procedureCall = "{call CHARGEMENT_RETENUE_MENSUEL(?, ?, ?)}";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement stmt = connection.prepareCall(procedureCall)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            YearMonth ym = YearMonth.parse(mois, formatter);

            LocalDate lastDay = ym.atEndOfMonth();
            java.sql.Date sqlDate = java.sql.Date.valueOf(lastDay);

            stmt.setString(1, soc);
            stmt.setDate(2, sqlDate);

            // ⭐ Important: allow null parameter
            if (mat == null || mat.trim().isEmpty()) {
                stmt.setNull(3, java.sql.Types.VARCHAR);
            } else {
                stmt.setString(3, mat);
            }

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'appel de la procédure : " + e.getMessage(), e);
        }
    }

    public ResponseProcedure vir_carte_ret(String wcodSoc, String dat_ret) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_GESTION_CREDIT.vir_carte_ret(?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                YearMonth yearMonth = YearMonth.parse(dat_ret, formatter);
                LocalDate lastDay = yearMonth.atEndOfMonth();

                callableStatement.setString(1, wcodSoc);
                callableStatement.setDate(2, java.sql.Date.valueOf(lastDay));
                callableStatement.registerOutParameter(3, Types.VARCHAR);
                callableStatement.execute();

                ResponseProcedure reponse = new ResponseProcedure();
                reponse.setMessage(callableStatement.getString(3));

                // Fetch the generated file names from the table
                List<String> generatedFiles = jdbcTemplate.queryForList("SELECT DISTINCT file_name FROM vir_carte", String.class);
                reponse.setFiles(generatedFiles);

                return reponse;
            }
        });
    }

    public ResponseProcedure vir_carte_aux(String wcodSoc, String dat_ret) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_GESTION_CREDIT.vir_carte_aux(?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                YearMonth yearMonth = YearMonth.parse(dat_ret, formatter);
                LocalDate lastDay = yearMonth.atEndOfMonth();

                callableStatement.setString(1, wcodSoc);
                callableStatement.setDate(2, java.sql.Date.valueOf(lastDay));
                callableStatement.registerOutParameter(3, Types.VARCHAR);
                callableStatement.execute();

                ResponseProcedure reponse = new ResponseProcedure();
                reponse.setMessage(callableStatement.getString(3));

                // Fetch the generated file names from the table
                List<String> generatedFiles = jdbcTemplate.queryForList("SELECT DISTINCT file_name FROM vir_carte", String.class);
                reponse.setFiles(generatedFiles);

                return reponse;
            }
        });
    }
    private Date convertToSqlDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null; // Return null if the date string is null or empty
        }

        try {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parse the string into a LocalDate
            LocalDate localDate = LocalDate.parse(dateStr, formatter);

            // Convert to java.sql.Date
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr + ". Expected format: dd/MM/yyyy", e);
        }
    }

    public ResponseProcedure deleteCredit(String wcodSoc, String matPers, Integer codPret) {
        return jdbcTemplate.execute((Connection connection) -> {
            String call = "{call PK_GESTION_CREDIT.delete_credit(?, ?, ?, ?)}";

            try (CallableStatement stmt = connection.prepareCall(call)) {
                stmt.setString(1, wcodSoc);
                stmt.setString(2, matPers);
                stmt.setInt(3, codPret);
                stmt.registerOutParameter(4, Types.VARCHAR);

                stmt.execute();

                ResponseProcedure res = new ResponseProcedure();
                res.setMessage(stmt.getString(4));
                return res;
            }
        });
    }


    public void updateListLigPret(List<LigPret> list) {
        for (LigPret dto : list) {
            ligPretRepository.updateRegPret(
                    dto.getCod_soc(),
                    dto.getMat_pers(),
                    dto.getCod_pret(),
                    dto.getL_pret(),
                    dto.getReg_pret()
            );
        }
    }

    public boolean hasPretCondition(String mat, String codGrp) {

        StoredProcedureQuery query =
                entityManager.createStoredProcedureQuery(
                        "PK_GESTION_CREDIT.check_pret_condition"
                );

        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(3, Integer.class, ParameterMode.OUT);

        query.setParameter(1, mat);
        query.setParameter(2, codGrp);

        query.execute();

        Integer result = (Integer) query.getOutputParameterValue(3);
        return result != null && result == 1;
    }
    public byte[] getFileData(String filename) {
        String sql = "SELECT ligne FROM vir_carte";
        if (filename != null && !filename.isEmpty()) {
            sql += " WHERE typ_lig = '" + filename + "'";
        }
        sql += " ORDER BY ordre";

        List<String> lines = jdbcTemplate.queryForList(sql, String.class);
        return String.join("\n", lines).getBytes();
    }



    public byte[] generateVirCarteFiles(String soc, LocalDate datRet) throws IOException {

        // 1️⃣ Call stored procedure
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("vir_carte_ret");

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("soc_", soc)
                .addValue("dat_ret", Date.valueOf(datRet));

        call.execute(params);

        // 2️⃣ Read generated lines
        List<VirCarteLine> lines = virCarteRepository.findAllOrdered();

        // 3️⃣ Group by file name (LIBC + MMYYYY)
        Map<String, List<String>> files = new HashMap<>();

        for (VirCarteLine l : lines) {
            files.computeIfAbsent(l.getFileId(), k -> new ArrayList<>())
                    .add(l.getLigne());
        }

        // 4️⃣ Create ZIP
        return createZip(files);
    }

    private byte[] createZip(Map<String, List<String>> files) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(baos);

        for (Map.Entry<String, List<String>> entry : files.entrySet()) {

            ZipEntry zipEntry = new ZipEntry(entry.getKey() + ".TXT");
            zip.putNextEntry(zipEntry);

            for (String line : entry.getValue()) {
                zip.write((line + "\r\n").getBytes(StandardCharsets.UTF_8));
            }

            zip.closeEntry();
        }

        zip.close();
        return baos.toByteArray();
    }

    public String generateVirCarteFile(String filePath, String fileName) {
        // Crucial: Filter by file_name so each file only contains its own data
        String sql = "SELECT ligne FROM vir_carte WHERE file_name = ? ORDER BY ordre";
        List<String> lines = jdbcTemplate.queryForList(sql, String.class, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            return "Fichier généré avec succès";
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier";
        }
    }
    public void anticipPret(
            String soc,
            String mat,
            Long cod,
            Long codAnt
    ) {
        System.out.println(soc+" test anticip "+mat+"  "+cod+"   "+codAnt);
        pretPersRepository.anticipPret(soc, mat, cod, codAnt);
    }
}
