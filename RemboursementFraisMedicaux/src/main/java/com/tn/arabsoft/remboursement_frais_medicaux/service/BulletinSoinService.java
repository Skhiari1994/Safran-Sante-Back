package com.tn.arabsoft.remboursement_frais_medicaux.service;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.BultSoin;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultSoinCle;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.BultSoinRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "java:S117", "java:S107", "java:S112" })
public class BulletinSoinService {

    private final JdbcTemplate jdbcTemplate;
    private final BultSoinRepository bultSoinRepository;

    private static final String KEY_ERROR = "error";

    public ReponseDatNais getDatNais(String wcodSoc, String mat_, String numFam) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.get_dat_nais(?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, mat_); // IN parameter
                callableStatement.setString(3, numFam); // IN parameter
                callableStatement.registerOutParameter(4, Types.VARCHAR); // OUT parameter (wdat_nais_fam)

                callableStatement.execute(); // Exécute la procédure stockée
                ReponseDatNais reponseDatNais = new ReponseDatNais();
                String datNais = callableStatement.getString(4);
                reponseDatNais.setDat_nais(datNais);
                return reponseDatNais; // Récupération de la date de naissance
            }
        });
    }

    public ReponseGetSexe getSexe(String wcodSoc, String mat_, String numFam) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.get_sexe(?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, mat_); // IN parameter
                callableStatement.setString(3, numFam); // IN parameter
                callableStatement.registerOutParameter(4, Types.VARCHAR); // OUT parameter (wdat_nais_fam)
                callableStatement.registerOutParameter(5, Types.VARCHAR); // OUT parameter (wdat_nais_fam)

                callableStatement.execute(); // Exécute la procédure stockée
                ReponseGetSexe reponseGetSexe = new ReponseGetSexe();
                String parente = callableStatement.getString(4);
                String sexe = callableStatement.getString(5);

                reponseGetSexe.setParente(parente);
                reponseGetSexe.setSexe(sexe);

                return reponseGetSexe; // Récupération de la date de naissance
            }
        });
    }

    public ResponseProcedure getDureeBulletin(String wdat_soin, String wCOD_ASSUR) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.get_duree_bulletin(?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wdat_soin);
                callableStatement.setString(2, wCOD_ASSUR);
                callableStatement.registerOutParameter(3, Types.VARCHAR);
                callableStatement.execute();
                String message = callableStatement.getString(3);
                ResponseProcedure responseProcedure = new ResponseProcedure();
                responseProcedure.setReponse(message);
                return responseProcedure;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    public ReponseGetPlafondMutuelle getPlafondMutuelle(String soc, String mat, String datSoin, String datNais,
            String datSaisie) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.get_plafond_mutuelle(?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, datSoin);
                callableStatement.setString(4, datNais);
                callableStatement.setString(5, datSaisie);
                callableStatement.registerOutParameter(6, Types.VARCHAR);
                callableStatement.registerOutParameter(7, Types.VARCHAR);
                callableStatement.registerOutParameter(8, Types.VARCHAR);
                callableStatement.registerOutParameter(9, Types.VARCHAR);
                callableStatement.registerOutParameter(10, Types.VARCHAR);
                callableStatement.execute();

                String soldPlafond = callableStatement.getString(6);
                String soldPlafondEstim = callableStatement.getString(7);
                String ageAn = callableStatement.getString(8);
                String ageMois = callableStatement.getString(9);
                String message = callableStatement.getString(10);
                ReponseGetPlafondMutuelle reponseGetPlafondMutuelle = new ReponseGetPlafondMutuelle();

                reponseGetPlafondMutuelle.setSoldPlafond(soldPlafond);
                reponseGetPlafondMutuelle.setSoldPlafondEstim(soldPlafondEstim);
                reponseGetPlafondMutuelle.setAgeAn(ageAn);
                reponseGetPlafondMutuelle.setAgeMois(ageMois);
                reponseGetPlafondMutuelle.setMessage(message);
                return reponseGetPlafondMutuelle;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public ReponseGetPlafondMutuelle getPlafondCnam(String soc, String mat, String datSoin, String datNais,
            String codAssur, String datSaisie) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.get_plafond_cnam(?,?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, datSoin);
                callableStatement.setString(4, datNais);
                callableStatement.setString(5, codAssur);
                callableStatement.setString(6, datSaisie);
                callableStatement.registerOutParameter(7, Types.VARCHAR);
                callableStatement.registerOutParameter(8, Types.VARCHAR);
                callableStatement.registerOutParameter(9, Types.VARCHAR);
                callableStatement.registerOutParameter(10, Types.VARCHAR);
                callableStatement.registerOutParameter(11, Types.VARCHAR);
                callableStatement.execute();

                String soldPlafond = callableStatement.getString(7);
                String soldPlafondEstim = callableStatement.getString(8);
                String ageAn = callableStatement.getString(9);
                String ageMois = callableStatement.getString(10);
                String message = callableStatement.getString(11);
                ReponseGetPlafondMutuelle reponseGetPlafondMutuelle = new ReponseGetPlafondMutuelle();

                reponseGetPlafondMutuelle.setSoldPlafond(soldPlafond);
                reponseGetPlafondMutuelle.setSoldPlafondEstim(soldPlafondEstim);
                reponseGetPlafondMutuelle.setAgeAn(ageAn);
                reponseGetPlafondMutuelle.setAgeMois(ageMois);
                reponseGetPlafondMutuelle.setMessage(message);
                return reponseGetPlafondMutuelle;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public ReponseCalculMntNet calculerMontantNet(String soc, String mat, String codFil, String codAssur,
            String abrvAct, String datSoin, BigDecimal mntHnor, Long indice) {

        Date sqlDatSoin = (datSoin != null && !datSoin.isEmpty()) ? convertToSqlDate(datSoin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.calculer_montant_net(?,?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, codFil);

                callableStatement.setString(4, codAssur);
                callableStatement.setString(5, abrvAct);
                callableStatement.setDate(6, sqlDatSoin);
                callableStatement.setBigDecimal(7, mntHnor);
                callableStatement.setLong(8, indice);
                callableStatement.registerOutParameter(9, Types.NUMERIC);
                callableStatement.registerOutParameter(10, Types.NUMERIC);
                callableStatement.registerOutParameter(11, Types.VARCHAR);
                callableStatement.execute();

                BigDecimal cumul = callableStatement.getBigDecimal(9);
                BigDecimal net = callableStatement.getBigDecimal(10);
                String message = callableStatement.getString(11);
                ReponseCalculMntNet reponse = new ReponseCalculMntNet();

                reponse.setP_mnt_net(cumul);

                reponse.setP_mnt_net(net);
                reponse.setMessage(message);
                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    public ResponseProcedure verifIndice(String abrc_act_, String cod_fil_, String cod_assur_, String indice) {

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.verif_indice(?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, abrc_act_);
                callableStatement.setString(2, cod_fil_);
                callableStatement.setString(3, cod_assur_);

                callableStatement.setString(4, indice);

                callableStatement.registerOutParameter(5, Types.VARCHAR);
                callableStatement.execute();

                String message = callableStatement.getString(5);
                ResponseProcedure reponse = new ResponseProcedure();

                reponse.setReponse(message);

                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    public ResponseProcedure verifVign(String abrc_act_, String cod_fil_, String cod_assur_, BigDecimal mnt_honor_,
            String nbr_vign_) {

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.verif_vign(?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, abrc_act_);
                callableStatement.setString(2, cod_fil_);
                callableStatement.setString(3, cod_assur_);
                callableStatement.setBigDecimal(3, mnt_honor_);
                callableStatement.setString(5, nbr_vign_);

                callableStatement.registerOutParameter(6, Types.VARCHAR);
                callableStatement.execute();

                String message = callableStatement.getString(6);
                ResponseProcedure reponse = new ResponseProcedure();

                reponse.setReponse(message);

                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    public ResponseProcedure verifPiece(String abrc_act_, String cod_fil_, String cod_assur_, BigDecimal mnt_honor_,
            String nbr_piece_) {

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.verif_piece(?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, abrc_act_);
                callableStatement.setString(2, cod_fil_);
                callableStatement.setString(3, cod_assur_);
                callableStatement.setBigDecimal(3, mnt_honor_);
                callableStatement.setString(5, nbr_piece_);

                callableStatement.registerOutParameter(6, Types.VARCHAR);
                callableStatement.execute();

                String message = callableStatement.getString(6);
                ResponseProcedure reponse = new ResponseProcedure();

                reponse.setReponse(message);

                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    public ReponseVetifModRemb vetifModRemb(String soc, String mat, String numFam, String datSoin, String reg_remb_,
            String parente, String sexe) {

        Date sqlDatSoin = (datSoin != null && !datSoin.isEmpty()) ? convertToSqlDate(datSoin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.vetif_mod_remb(?,?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, numFam);

                callableStatement.setDate(4, sqlDatSoin);
                callableStatement.setString(5, reg_remb_);
                callableStatement.setString(6, parente);
                callableStatement.setString(7, sexe);
                callableStatement.registerOutParameter(8, Types.VARCHAR);
                callableStatement.registerOutParameter(9, Types.VARCHAR);
                callableStatement.registerOutParameter(10, Types.VARCHAR);
                callableStatement.registerOutParameter(11, Types.VARCHAR);
                callableStatement.execute();

                String msg_dat_accouch = callableStatement.getString(8);
                String msg_malad = callableStatement.getString(9);
                String msg_prise_charge = callableStatement.getString(10);
                String message = callableStatement.getString(11);
                ReponseVetifModRemb reponse = new ReponseVetifModRemb();

                reponse.setMsg_dat_accouch(msg_dat_accouch);

                reponse.setMsg_malad(msg_malad);
                reponse.setMsg_prise_charge(msg_prise_charge);
                reponse.setMessage(message);
                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    public ReponseCalculMntNet calculerMontantNetApp(String soc, String mat, String numFam, String datSoin,
            BigDecimal mntHnor, String codApp, Long mutMntNet) {

        Date sqlDatSoin = (datSoin != null && !datSoin.isEmpty()) ? convertToSqlDate(datSoin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.calcule_mnt_net_app(?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, numFam);

                callableStatement.setDate(4, sqlDatSoin);
                callableStatement.setBigDecimal(5, mntHnor);
                callableStatement.setString(6, codApp);
                callableStatement.setLong(7, mutMntNet);
                callableStatement.registerOutParameter(8, Types.NUMERIC);
                callableStatement.registerOutParameter(9, Types.VARCHAR);
                callableStatement.execute();

                BigDecimal cumul = callableStatement.getBigDecimal(8);
                String message = callableStatement.getString(9);
                ReponseCalculMntNet reponse = new ReponseCalculMntNet();

                reponse.setP_mnt_net(cumul);
                reponse.setMessage(message);
                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public ReponseCalculMntNet calculerMontantNetVisit(String soc, String mat, String codVisit, BigDecimal mntHnor,
            String datSoin, BigDecimal prixVisit, BigDecimal tauxRemb, BigDecimal sauvNet) {

        Date sqlDatSoin = (datSoin != null && !datSoin.isEmpty()) ? convertToSqlDate(datSoin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.calcul_mnt_net_visit(?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, codVisit);
                callableStatement.setBigDecimal(4, mntHnor);
                callableStatement.setDate(5, sqlDatSoin);

                callableStatement.setBigDecimal(6, prixVisit);
                callableStatement.setBigDecimal(7, tauxRemb);
                callableStatement.setBigDecimal(8, sauvNet);
                callableStatement.registerOutParameter(9, Types.NUMERIC);
                callableStatement.registerOutParameter(10, Types.VARCHAR);
                callableStatement.execute();

                BigDecimal cumul = callableStatement.getBigDecimal(9);
                String message = callableStatement.getString(10);
                ReponseCalculMntNet reponse = new ReponseCalculMntNet();

                reponse.setP_mnt_net(cumul);
                reponse.setMessage(message);
                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public ReponseCalculMntNet calculerMontantNetAct(String soc, String mat, String codAct, BigDecimal mntHnor,
            String datSoin,
            BigDecimal actPrix, BigDecimal tauxAct, BigDecimal cumul_net_, BigDecimal sauv_net_) {

        Date sqlDatSoin = (datSoin != null && !datSoin.isEmpty()) ? convertToSqlDate(datSoin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.calcul_mnt_net_act(?,?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, codAct);
                callableStatement.setBigDecimal(4, mntHnor);
                callableStatement.setDate(5, sqlDatSoin);

                callableStatement.setBigDecimal(6, actPrix);
                callableStatement.setBigDecimal(7, tauxAct);
                callableStatement.setBigDecimal(8, cumul_net_);
                callableStatement.setBigDecimal(9, sauv_net_);
                callableStatement.registerOutParameter(10, Types.NUMERIC);
                callableStatement.registerOutParameter(11, Types.VARCHAR);
                callableStatement.execute();

                BigDecimal cumul = callableStatement.getBigDecimal(10);
                String message = callableStatement.getString(11);
                ReponseCalculMntNet reponse = new ReponseCalculMntNet();

                reponse.setP_mnt_net(cumul);
                reponse.setMessage(message);
                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public ReponseCalculMntNet calculerMontantNetMed(String soc, String mat, String codMed, BigDecimal mntHnor,
            String datSoin,
            BigDecimal prixRemb, Long indice, BigDecimal cumul_net_, BigDecimal sauv_net_) {

        Date sqlDatSoin = (datSoin != null && !datSoin.isEmpty()) ? convertToSqlDate(datSoin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_BULLETIN_SOIN.calcul_mnt_net_med(?,?,?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setString(3, codMed);
                callableStatement.setBigDecimal(4, mntHnor);
                callableStatement.setDate(5, sqlDatSoin);

                callableStatement.setBigDecimal(6, prixRemb);
                callableStatement.setLong(7, indice);
                callableStatement.setBigDecimal(8, cumul_net_);
                callableStatement.setBigDecimal(9, sauv_net_);
                callableStatement.registerOutParameter(10, Types.NUMERIC);
                callableStatement.registerOutParameter(11, Types.VARCHAR);
                callableStatement.execute();

                BigDecimal cumul = callableStatement.getBigDecimal(10);
                String message = callableStatement.getString(11);
                ReponseCalculMntNet reponse = new ReponseCalculMntNet();

                reponse.setP_mnt_net(cumul);
                reponse.setMessage(message);
                return reponse;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
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

    public Map<String, Object> callCreateBordProc(
            String pCodAssur,
            String pCodSoc,
            String pChoix,
            LocalDate pDatSaisie,
            Double pTotHonor,
            Double pTotNet,
            LocalDate pDatSoin,
            Long pNumFam,
            String pMatPers) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_ENVOI.create_bord(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Définition des paramètres d'entrée
                callableStatement.setString(1, pCodAssur);
                callableStatement.setString(2, pCodSoc);
                callableStatement.setString(3, pChoix);
                callableStatement.setDate(4, java.sql.Date.valueOf(pDatSaisie)); // LocalDate -> SQL Date
                callableStatement.setDouble(5, pTotHonor);
                callableStatement.setDouble(6, pTotNet);
                callableStatement.setDate(7, java.sql.Date.valueOf(pDatSoin)); // LocalDate -> SQL Date
                callableStatement.setLong(8, pNumFam);
                callableStatement.setString(9, pMatPers);

                // Définition du paramètre de sortie
                callableStatement.registerOutParameter(12, Types.VARCHAR); // p_bord (VARCHAR2)

                // Exécuter la procédure stockée
                callableStatement.execute();

                // Récupérer les résultats
                Map<String, Object> result = new HashMap<>();
                result.put("p_bord", callableStatement.getString(10)); // Récupérer le code bordereau généré

                return result; // Retourner la map avec le code bordereau
            } catch (SQLException e) {
                e.printStackTrace();
                return Collections.singletonMap(KEY_ERROR, "Failed to execute procedure: " + e.getMessage());
            }
        });
    }

    public String callGenerateBordCodeMut(String codAssur, String codSoc, LocalDate datDeb, LocalDate datFin,
            int nbrBult, double totHonor, double totNet) {

        return jdbcTemplate.execute((Connection con) -> {
            String sql = "{ call PK_ENVOI.gen_bord_mut(?, ?, ?, ?, ?, ?, ?, ?) }";
            java.sql.Date sqlDatDeb = (datDeb != null) ? java.sql.Date.valueOf(datDeb) : null;
            java.sql.Date sqlDatFin = (datFin != null) ? java.sql.Date.valueOf(datFin) : null;

            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.setString(1, codAssur);
                cs.setString(2, codSoc);
                cs.setDate(3, sqlDatDeb); // LocalDate → java.sql.Date
                cs.setDate(4, sqlDatFin);
                cs.setInt(5, nbrBult);
                cs.setDouble(6, totHonor);
                cs.setDouble(7, totNet);

                cs.registerOutParameter(8, Types.VARCHAR); // p_cod_bord OUT

                cs.execute();

                return cs.getString(8); // return generated bord code
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur dans l'appel à gen_bord: " + e.getMessage());
            }
        });
    }

    /* prep cnam */
    public String callGenerateBordCode(String codAssur, String codSoc, LocalDate datDeb, LocalDate datFin,
            int nbrBult, double totHonor, double totNet) {

        return jdbcTemplate.execute((Connection con) -> {
            String sql = "{ call PK_ENVOI.gen_bord(?, ?, ?, ?, ?, ?, ?, ?) }";
            java.sql.Date sqlDatDeb = (datDeb != null) ? java.sql.Date.valueOf(datDeb) : null;
            java.sql.Date sqlDatFin = (datFin != null) ? java.sql.Date.valueOf(datFin) : null;

            try (CallableStatement cs = con.prepareCall(sql)) {
                cs.setString(1, codAssur);
                cs.setString(2, codSoc);
                cs.setDate(3, sqlDatDeb); // LocalDate → java.sql.Date
                cs.setDate(4, sqlDatFin);
                cs.setInt(5, nbrBult);
                cs.setDouble(6, totHonor);
                cs.setDouble(7, totNet);

                cs.registerOutParameter(8, Types.VARCHAR); // p_cod_bord OUT

                cs.execute();

                return cs.getString(8); // return generated bord code
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur dans l'appel à gen_bord: " + e.getMessage());
            }
        });
    }

    public String callCreateBordBultAll(
            String codBord,
            String matPers,
            String codSoc,
            LocalDate datSoin,
            int numFam,
            int ordBult,
            BigDecimal totHonor,
            BigDecimal totNet) {
        final String[] messageOut = new String[1];
        jdbcTemplate.execute((Connection connection) -> {
            String sql = "{ call PK_ENVOI.create_bord_bult_all(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            try (CallableStatement stmt = connection.prepareCall(sql)) {

                stmt.setString(1, codBord);
                stmt.setString(2, matPers);
                stmt.setString(3, codSoc);
                stmt.setDate(4, datSoin != null ? java.sql.Date.valueOf(datSoin) : null);
                stmt.setInt(5, numFam);
                stmt.setInt(6, ordBult);
                stmt.setBigDecimal(7, totHonor);
                stmt.setBigDecimal(8, totNet);

                stmt.registerOutParameter(9, Types.VARCHAR);

                stmt.execute();

                messageOut[0] = stmt.getString(9);

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de l'appel de create_bord_bult_all : " + e.getMessage());
            }
            return null;
        });

        return messageOut[0];
    }

    public void callCreateBordBult(
            String codBord,
            String codSoc,
            String matPers,
            int numFam,
            LocalDate datSoin,
            BigDecimal mntHonor,
            BigDecimal mntNet,
            int ordBult) {
        jdbcTemplate.execute((Connection connection) -> {
            String sql = "{ call PK_ENVOI.create_bord_bult(?, ?, ?, ?, ?, ?, ?, ?) }";
            try (CallableStatement stmt = connection.prepareCall(sql)) {

                stmt.setString(1, codBord);
                stmt.setString(2, codSoc);
                stmt.setString(3, matPers);
                stmt.setInt(4, numFam);
                stmt.setDate(5, java.sql.Date.valueOf(datSoin));
                stmt.setBigDecimal(6, mntHonor);
                stmt.setBigDecimal(7, mntNet);
                stmt.setInt(8, ordBult);

                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erreur lors de l'appel de create_bord_bult : " + e.getMessage());
            }
            return null;
        });
    }

    public void callModifBord(
            String pCodAssur,
            String pCodSoc,
            String pMatPers,
            String pNumFam,
            LocalDate pDatSoin,
            Double pTotHonor,
            Double pTotNet,
            String pChoix,
            String pCodBord,
            int vNbrBult) {
        String procedureCall = "{ call PK_ENVOI.modif_bord_1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }"; // Procedure call with
                                                                                                  // an additional
                                                                                                  // output parameter

        jdbcTemplate.execute((Connection connection) -> {
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, pCodAssur);
                callableStatement.setString(2, pCodSoc);
                callableStatement.setString(3, pMatPers);
                callableStatement.setString(4, pNumFam);
                callableStatement.setDate(5, java.sql.Date.valueOf(pDatSoin)); // Convert LocalDate to SQL Date
                callableStatement.setDouble(6, pTotHonor);
                callableStatement.setDouble(7, pTotNet);
                callableStatement.setString(8, pChoix);
                callableStatement.setString(9, pCodBord);
                callableStatement.setInt(10, vNbrBult);

                // Register the output parameter (e.g., a message indicating success or failure)
                callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR); // Register the output parameter
                                                                                    // (e.g., for success message)

                // Execute stored procedure
                callableStatement.execute();

                // Retrieve the output parameter value
                String successMessage = callableStatement.getString(11);
                if (successMessage != null) {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error executing modif_bord_1: " + e.getMessage());
            }

            return null;
        });
    }

    public Map<String, Object> callCreateBord(
            String pCodAssur,
            String pCodSoc,
            Double pTotHonor,
            Double pTotNet,
            String pChoix,
            Long vNBRBult,
            String pCodBord,
            LocalDate datDeb,
            LocalDate datFin) {
        return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
            try (CallableStatement callableStatement = connection
                    .prepareCall("{call PK_ENVOI.create_bord(?, ?, ?, ?, ?, ?, ?, ?,?)}")) {
                // Définition des paramètres d'entrée
                callableStatement.setString(1, pCodAssur);
                callableStatement.setString(2, pCodSoc);
                callableStatement.setDouble(3, pTotHonor);
                callableStatement.setDouble(4, pTotNet);

                // Gestion de pChoix
                if (pChoix == null || pChoix.trim().isEmpty()) {
                    callableStatement.setNull(5, Types.VARCHAR);
                } else {
                    callableStatement.setString(5, pChoix);
                }

                callableStatement.setLong(6, vNBRBult);
                callableStatement.setString(7, pCodBord);

                // ✅ Correction : Convertir LocalDate en java.sql.Date
                if (datDeb != null) {
                    callableStatement.setDate(8, java.sql.Date.valueOf(datDeb));
                } else {
                    callableStatement.setNull(8, Types.DATE);
                }
                // ✅ Correction : Convertir LocalDate en java.sql.Date
                if (datFin != null) {
                    callableStatement.setDate(9, java.sql.Date.valueOf(datFin));
                } else {
                    callableStatement.setNull(9, Types.DATE);
                }

                // Exécution de la procédure stockée
                callableStatement.executeUpdate();
                return Collections.singletonMap("status", "success");
            } catch (SQLException e) {
                e.printStackTrace();
                return Collections.singletonMap(KEY_ERROR, "An error occurred: " + e.getMessage());
            }
        });
    }

    public Map<String, Object> callCalculateTotalHonor(
            String pCodSoc,
            String pMatPers,
            Long pNumFam,
            LocalDate pDatSoin) {
        return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
            try (CallableStatement callableStatement = connection
                    .prepareCall("{call PK_ENVOI.calculate_total_honor(?, ?, ?, ?, ?, ?, ?, ?)}")) {

                // Set input parameters
                callableStatement.setString(1, pCodSoc);
                callableStatement.setString(2, pMatPers);
                callableStatement.setLong(3, pNumFam);

                // Convert LocalDate to java.sql.Date
                if (pDatSoin != null) {
                    callableStatement.setDate(4, java.sql.Date.valueOf(pDatSoin));
                } else {
                    callableStatement.setNull(4, Types.DATE);
                }

                // Register output parameters
                callableStatement.registerOutParameter(5, Types.NUMERIC); // xmnt_honor
                callableStatement.registerOutParameter(6, Types.NUMERIC); // ymnt_tot
                callableStatement.registerOutParameter(7, Types.NUMERIC); // zMNT_TOT_MUT
                callableStatement.registerOutParameter(8, Types.NUMERIC); // asold

                // Execute the stored procedure
                callableStatement.execute();

                // Retrieve output values
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("xmnt_honor", callableStatement.getBigDecimal(5));
                resultMap.put("ymnt_tot", callableStatement.getBigDecimal(6));
                resultMap.put("zMNT_TOT_MUT", callableStatement.getBigDecimal(7));
                resultMap.put("asold", callableStatement.getBigDecimal(8));

                return resultMap;
            } catch (SQLException e) {
                e.printStackTrace();
                return Collections.singletonMap(KEY_ERROR, "An error occurred: " + e.getMessage());
            }
        });
    }

    public BultSoin updateBultSoin(BultSoinCle id, String envoi) {
        BultSoin existing = bultSoinRepository.findById(id).orElseThrow(() -> new RuntimeException("bultin not found"));
        existing.setEnvoi(envoi);

        return bultSoinRepository.save(existing);
    }

    public String callDeletBord(String pCodSoc, String pCodBord) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{ call PK_ENVOI.supp_bord(?, ?) }"; // Call procedure

            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                // Set input parameters
                callableStatement.setString(1, pCodSoc);
                callableStatement.setString(2, pCodBord);

                // Execute the procedure
                callableStatement.execute();
                return "Suppression réussie.";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Erreur: " + e.getMessage();
            }
        });
    }

    public String callUpdateBultSoin(String codSoc, String matPers, int numFam, LocalDate datSoin, double mntHonor,
            double mntTot) {

        String procedureCall = "{call pk_envoi.update_bult_soin(?, ?, ?, ?, ?, ?)}";

        DataSource dataSource = jdbcTemplate.getDataSource();

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not configured in JdbcTemplate");
        }

        try (Connection connection = dataSource.getConnection();
                CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Set input parameters
            callableStatement.setString(1, codSoc);
            callableStatement.setString(2, matPers);
            callableStatement.setInt(3, numFam);
            callableStatement.setDate(4, java.sql.Date.valueOf(datSoin));
            callableStatement.setDouble(5, mntHonor);
            callableStatement.setDouble(6, mntTot);

            // Execute the procedure
            callableStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Error calling procedure update_bult_soin: " + e.getMessage(), e);
        }

        return procedureCall;
    }

    public void callPrepFichCnam(String codSoc, String codBord) {
        String procedureCall = "{call PK_ENVOI.prep_fich_cnam(?, ?)}";

        DataSource dataSource = jdbcTemplate.getDataSource();

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not configured in JdbcTemplate");
        }

        try (Connection connection = dataSource.getConnection();
                CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Set input parameters
            callableStatement.setString(1, codSoc);
            callableStatement.setString(2, codBord);

            // Execute the procedure
            callableStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'appel de la procédure : " + e.getMessage(), e);
        }
    }

    public void callEnvoiBordEnvoi(String codSoc, String codBord) {
        String procedureCall = "{call PK_ENVOI.envoi_bord_envoi(?, ?)}";

        DataSource dataSource = jdbcTemplate.getDataSource();

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not configured in JdbcTemplate");
        }

        try (Connection connection = dataSource.getConnection();
                CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Set input parameters
            callableStatement.setString(1, codSoc);
            callableStatement.setString(2, codBord);

            // Execute the procedure
            callableStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'appel de envoi_bord_envoi : " + e.getMessage(), e);
        }
    }
}