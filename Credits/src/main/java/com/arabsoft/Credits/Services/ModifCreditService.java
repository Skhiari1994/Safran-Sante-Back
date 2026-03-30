package com.arabsoft.Credits.Services;

import com.arabsoft.Credits.Entities.Response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Map;

@Service
public class ModifCreditService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public ControllerResponse controler(String wcodSoc, String wmatPers, String cod_pret_, String mois_debut, String mois_fin) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.controler(?, ?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, cod_pret_);
                callableStatement.setString(4, mois_debut);
                callableStatement.setString(5, mois_fin);
                callableStatement.registerOutParameter(6, Types.VARCHAR); // message

                callableStatement.execute();



                // Create and populate the response object
                ControllerResponse response = new ControllerResponse();
                response.setMois_debut(callableStatement.getString(4));

                response.setMois_fin(callableStatement.getString(5));
                response.setMessage(callableStatement.getString(6)); // Save formatted value

                return response;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }

    public PeriodeAnticipTotalReponse periode_ant_tot(String wcodSoc, String wmatPers, String cod_pret_) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.periode_ant_tot(?, ?, ?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, cod_pret_);
                callableStatement.registerOutParameter(4, Types.VARCHAR); // message
                callableStatement.registerOutParameter(5, Types.VARCHAR); // message
                callableStatement.registerOutParameter(6, Types.VARCHAR); // message
                callableStatement.registerOutParameter(7, Types.VARCHAR); // message

                callableStatement.execute();



                // Create and populate the response object
                PeriodeAnticipTotalReponse response = new PeriodeAnticipTotalReponse();
                response.setDat_deb_etat_(callableStatement.getString(4));

                response.setDat_fin_etat_(callableStatement.getString(5));
                response.setNbr_mois_(callableStatement.getString(6));

                response.setMessage(callableStatement.getString(7)); // Save formatted value

                return response;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }

    public CalculAnticipPart calcul_ant_part(String wcodSoc, String wmatPers, String cod_pret_, String dat_deb_etat_, String dat_fin_etat_,String typ_anticip_) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.calcul_ant_part(?, ?, ?, ?, ?, ?,?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, cod_pret_);
                callableStatement.setString(4, dat_deb_etat_);
                callableStatement.setString(5, dat_fin_etat_);
                callableStatement.setString(6, typ_anticip_);

                callableStatement.registerOutParameter(7, Types.VARCHAR); // message
                callableStatement.registerOutParameter(8, Types.VARCHAR); // message
                callableStatement.registerOutParameter(9, Types.VARCHAR); // message
                callableStatement.registerOutParameter(10, Types.VARCHAR); // message

                callableStatement.execute();



                // Create and populate the response object
                CalculAnticipPart response = new CalculAnticipPart();
                response.setMnt_capital(callableStatement.getString(7));

                response.setMnt_interet(callableStatement.getString(8));
                response.setInt_grace(callableStatement.getString(9)); // Save formatted value
                response.setMnt_anticipe(callableStatement.getString(10)); // Save formatted value

                return response;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }

    public BigDecimal calcul(String wcodSoc, String wmatPers, String cod_pret_, String dat_deb_, String dat_fin_) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.calcul(?, ?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, cod_pret_);
                callableStatement.setString(4, dat_deb_);
                callableStatement.setString(5, dat_fin_);

                callableStatement.registerOutParameter(6, Types.INTEGER); // message

                callableStatement.execute();



                  BigDecimal mntPrime =callableStatement.getBigDecimal(6);

                return mntPrime;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }

    public ValiderReponse Valider(String wcodSoc, String wmatPers, String cod_pret_,String mod_pay_ ,
                           String nat_etat_,String typ_anticip, String dat_deb_, String dat_fin_,
                                 BigDecimal mnt_capital_,BigDecimal mnt_anticip_,BigDecimal mnt_interet_,
                                  BigDecimal prt_rendu_  ) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.Valider(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, cod_pret_);
                callableStatement.setString(4, mod_pay_);
                callableStatement.setString(5, nat_etat_);
                callableStatement.setString(6, typ_anticip);
                callableStatement.setString(7, dat_deb_);
                callableStatement.setString(8, dat_fin_);
                callableStatement.setBigDecimal(9, mnt_capital_);
                callableStatement.setBigDecimal(10, mnt_anticip_);
                callableStatement.setBigDecimal(11, mnt_interet_);
                callableStatement.setBigDecimal(13, prt_rendu_);

                callableStatement.registerOutParameter(12, Types.INTEGER); // message
                callableStatement.registerOutParameter(13, Types.INTEGER); // message

                callableStatement.registerOutParameter(14, Types.VARCHAR); // message

                callableStatement.execute();
                ValiderReponse reponse=new ValiderReponse();


                reponse.setNbr_retenu(callableStatement.getLong(12));
                reponse.setPrt_rendu(callableStatement.getLong(13));

                reponse.setMessage(callableStatement.getString(14));

                return reponse;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }

    public ResponseProcedure reechelonner(String wcodSoc, String wmatPers,
                                          String codPret, String lPret) {

        return jdbcTemplate.execute((Connection connection) -> {
            String sql = "{call pk_gestion_credit.reechelonner(?, ?, ?, ?, ?)}";

            try (CallableStatement cs = connection.prepareCall(sql)) {

                cs.setString(1, wcodSoc);
                cs.setString(2, wmatPers);
                cs.setString(3, codPret);
                cs.setString(4, lPret);
                cs.registerOutParameter(5, Types.VARCHAR);

                cs.execute();

                ResponseProcedure res = new ResponseProcedure();
                res.setSuccess(true);
                res.setMessage(cs.getString(5));
                return res;

            } catch (SQLException e) {
                ResponseProcedure res = new ResponseProcedure();
                res.setSuccess(false);
                res.setMessage(e.getMessage());
                return res;
            }
        });
    }

    public ResponseProcedure reechelonner_2(String wcodSoc, String wmatPers, String codPret) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.reechelonner_2(?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, codPret);
                 // Register output parameters
                callableStatement.registerOutParameter(4, Types.VARCHAR);

                callableStatement.execute();

                ResponseProcedure response = new ResponseProcedure();
                response.setMessage(callableStatement.getString(4));

                return response;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
            }
        });
    }

    public ReponseCalculDetailCredit calcul_detail_credit_reech(String wcodSoc, String wdatfin, String wdatretr, Double wprtmntglb, Double wprtech, Double wprttaux, BigDecimal wmntreport,String codGrpPret) {
        // Convert input Strings to SQL Date, ensuring null safety
        Date sqlWdatFin = (wdatfin != null && !wdatfin.isEmpty()) ? convertToSqlDate(wdatfin) : null;
        Date sqlWdatRetr = (wdatretr != null && !wdatretr.isEmpty()) ? convertToSqlDate(wdatretr) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.calcul_detail_credit_reech(?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?)}";
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

    public ReponseCalcInteret calcul_interet(String wprt_ech,String wprt_mnt_glb,String wprt_taux,String wprt_mnt_rem, String wPRT_INTERET, String wrem_men) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.calcul_interet(?, ?, ?, ?,?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wprt_ech);
                callableStatement.setString(2, wprt_mnt_glb);
                callableStatement.setString(3, wprt_taux);
                callableStatement.setString(4, wprt_mnt_rem);
                callableStatement.setString(5, wPRT_INTERET);
                callableStatement.setString(6, wrem_men);

                // Register output parameters
                callableStatement.registerOutParameter(4, Types.VARCHAR);
                callableStatement.registerOutParameter(5, Types.VARCHAR);
                callableStatement.registerOutParameter(6, Types.VARCHAR);

                callableStatement.registerOutParameter(7, Types.VARCHAR);

                callableStatement.execute();

                ReponseCalcInteret response = new ReponseCalcInteret();
                response.setWprt_mnt_rem(callableStatement.getString(4));
                response.setWPRT_INTERET(callableStatement.getString(5));
                response.setWrem_men(callableStatement.getString(6));

                response.setMessage(callableStatement.getString(7));

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
    public void susp_pret(String soc, String wmat_pers,String wcod_pret_ant, String wcod_pret) {


        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{CALL pk_gestion_credit.susp_pret(?, ?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, wmat_pers);
                    callableStatement.setString(3, wcod_pret_ant);

                    callableStatement.setString(4, wcod_pret);

                    return callableStatement;
                },
                Collections.emptyList()
        );

        // Récupérer la valeur du paramètre OUT

    }

    public ValidSuspReponse Valider_susp(String wcodSoc, String wmatPers, String cod_pret_,String num_etat_pret ,
                                  String etat_pret,String nat_etat,String typ_anticip, String dat_deb_, String dat_fin_) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.valider_susp(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, wmatPers);
                callableStatement.setString(3, cod_pret_);
                callableStatement.setString(4, num_etat_pret);
                callableStatement.setString(5, etat_pret);

                callableStatement.setString(6, nat_etat);
                callableStatement.setString(7, typ_anticip);
                callableStatement.setString(8, dat_deb_);
                callableStatement.setString(9, dat_fin_);

                callableStatement.registerOutParameter(10, Types.VARCHAR); // message

                callableStatement.registerOutParameter(11, Types.VARCHAR); // message

                callableStatement.execute();
                ValidSuspReponse reponse=new ValidSuspReponse();



                reponse.setMessage(callableStatement.getString(10));
                reponse.setElement(callableStatement.getString(11));

                return reponse;
            } catch (SQLException e) {
                throw new RuntimeException("Error calling procedure: " + e.getMessage(), e);
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
}
