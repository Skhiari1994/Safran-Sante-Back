package com.tn.arabsoft.remboursement_frais_medicaux.service;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tn.arabsoft.remboursement_frais_medicaux.ControleBareme;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "java:S117", "java:S107", "java:S112", "java:S4507", "java:S2077" })
public class BulletinSoinCnamService {

    private static final String ACTE_MANDATORY = "Il faut saisir l'acte d'abord";

    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public ResponseProcedure verifIndice(String abrvAct, String codFil, String codAssur, String indice) {

        ResponseProcedure reponse = new ResponseProcedure();

        if (abrvAct == null || abrvAct.isBlank() || abrvAct.trim().isEmpty()) {
            reponse.setReponse(ACTE_MANDATORY);
            return reponse;
        }

        // L'indice n'a besoin d'être contrôlé que s'il est vide
        if ((indice == null || indice.isBlank() || indice.trim().isEmpty())
                && baremeExige(ControleBareme.INDICE, codFil, codAssur, abrvAct)) {
            reponse.setReponse("Il faut saisir le nombre des unités");
        }

        return reponse; // reponse == null : tout est valide
    }

    @Transactional(readOnly = true)
    public ResponseProcedure verifVign(String abrvAct, String codFil, String codAssur,
            BigDecimal mntHonor, String nbrVign) {

        ResponseProcedure reponse = new ResponseProcedure();

        if (abrvAct == null || abrvAct.isBlank() || abrvAct.trim().isEmpty()) {
            reponse.setReponse(ACTE_MANDATORY);
            return reponse;
        }

        if (mntHonor == null) {
            reponse.setReponse("Il faut saisir les honoraires");
            return reponse;
        }

        // Le nombre de vignettes n'est contrôlé en base que s'il est vide
        if ((nbrVign == null || nbrVign.isBlank() || nbrVign.trim().isEmpty())
                && baremeExige(ControleBareme.VIGNETTE, codFil, codAssur, abrvAct)) {
            reponse.setReponse("Il faut saisir le nombre des vignettes");
        }

        return reponse;
    }

    @Transactional(readOnly = true)
    public ResponseProcedure verifPiece(String abrvAct, String codFil, String codAssur,
            BigDecimal mntHonor, String nbrPiece) {

        ResponseProcedure reponse = new ResponseProcedure();

        if (abrvAct == null || abrvAct.isBlank() || abrvAct.trim().isEmpty()) {
            reponse.setReponse(ACTE_MANDATORY);
            return reponse;
        }

        if (mntHonor == null) {
            reponse.setReponse("Il faut saisir les honoraires");
            return reponse;
        }

        // Le nombre de pièces n'a besoin d'être contrôlé que s'il est vide
        if ((nbrPiece == null || nbrPiece.isBlank() || nbrPiece.trim().isEmpty())
                && baremeExige(ControleBareme.PIECE, codFil, codAssur, abrvAct)) {
            reponse.setReponse("Il faut saisir le nombre des pièces");
        }

        return reponse;
    }

    /** Vrai si le barème de cet acte impose la saisie (colonne = 'O'). */
    private boolean baremeExige(ControleBareme controle, String codFil, String codAssur, String abrvAct) {
        Integer nb = jdbcTemplate.queryForObject("""
                select count(*)
                  from bareme_remb
                 where cod_fil   = ?
                   and cod_assur = ?
                   and abrv_act  = ?
                   and %s = 'O'
                """.formatted(controle.getColonne()), Integer.class, codFil, codAssur, abrvAct);
        return nb != null && nb > 0;
    }

    @Transactional(readOnly = true)
    public ReponseCalculMntNet calculerMontantNetVisit(String soc, String mat, String codVisit,
            BigDecimal mntHnor, String datSoin, BigDecimal prixVisit, BigDecimal tauxRemb, BigDecimal sauvNet) {

        ReponseCalculMntNet reponse = new ReponseCalculMntNet();

        if (codVisit == null || codVisit.isBlank() || codVisit.trim().isEmpty()) {
            reponse.setMessage("Il faut saisir la visite d'abord");
            return reponse;
        }

        BigDecimal honor = nz(mntHnor);
        BigDecimal prix = nz(prixVisit);
        BigDecimal taux = nz(tauxRemb);
        BigDecimal sauv = nz(sauvNet);

        // 1) Net brut : taux appliqué sur le plus petit de (honoraires, prix de la
        // visite)
        BigDecimal net = honor.min(prix).multiply(taux).movePointLeft(2); // × taux / 100

        // 2) Plafond de l'adhérent (null si aucun plafond pour l'année du soin)
        PlafondCnam p = lirePlafond(soc, mat, datSoin);
        if (p == null) {
            reponse.setMessage("Plafond non encore initialisé pour cet adhérant");
            return reponse;
        }

        // Déjà consommé, hors la ligne en cours de modification
        BigDecimal dejaConsomme = p.consomme().subtract(sauv);

        // 3) Plafonnement
        reponse.setP_mnt_net(appliquerPlafond(net, honor, dejaConsomme, p.plafond()));
        return reponse;
    }

    @Transactional(readOnly = true)
    public ReponseCalculMntNet calculerMontantNetMed(String soc, String mat, String codMed, BigDecimal mntHnor,
            String datSoin, BigDecimal prixRemb, Long indice, BigDecimal cumulNet, BigDecimal sauvNet) {

        ReponseCalculMntNet reponse = new ReponseCalculMntNet();

        if (codMed == null || codMed.isBlank() || codMed.trim().isEmpty()) {
            reponse.setMessage("Il faut saisir le médicament d'abord");
            return reponse;
        }

        BigDecimal honor = nz(mntHnor);
        BigDecimal unites = (indice == null) ? BigDecimal.ZERO : BigDecimal.valueOf(indice);
        BigDecimal cumul = nz(cumulNet); // net des autres lignes médicaments du bulletin
        BigDecimal sauv = nz(sauvNet); // ancien net de la ligne en cours de modification

        // 1) Net brut : prix remboursable × nombre d'unités
        BigDecimal net = nz(prixRemb).multiply(unites);

        // 2) Plafond de l'adhérent
        PlafondCnam p = lirePlafond(soc, mat, datSoin);
        if (p == null) {
            reponse.setMessage("Plafond non encore initialisé pour cet adhérant");
            return reponse;
        }

        // Déjà consommé = solde estimé CNAM - ancien net de cette ligne + cumul des
        // autres lignes
        BigDecimal dejaConsomme = p.consomme().subtract(sauv).add(cumul);
        BigDecimal reste = p.plafond().subtract(dejaConsomme);

        // 3) Plafonnement
        BigDecimal resultat;
        if (net.compareTo(honor.multiply(unites)) > 0) {
            resultat = honor.add(dejaConsomme).compareTo(p.plafond()) > 0 ? reste : honor;
        } else {
            resultat = net.add(dejaConsomme).compareTo(p.plafond()) > 0 ? reste : net;
        }

        reponse.setP_mnt_net(resultat);
        return reponse;
    }

    @Transactional(readOnly = true)
    public ReponseCalculMntNet calculerMontantNetAct(String soc, String mat, String codAct, BigDecimal mntHnor,
            String datSoin, BigDecimal actPrix, BigDecimal tauxAct, BigDecimal cumulNet, BigDecimal sauvNet) {

        ReponseCalculMntNet reponse = new ReponseCalculMntNet();

        if (codAct == null || codAct.isBlank() || codAct.trim().isEmpty()) {
            reponse.setMessage(ACTE_MANDATORY);
            return reponse;
        }

        BigDecimal honor = nz(mntHnor);
        BigDecimal cumul = nz(cumulNet); // net des autres actes du bulletin
        BigDecimal sauv = nz(sauvNet); // ancien net de la ligne en cours de modification

        // 1) Net brut : prix de l'acte × taux / 100
        BigDecimal net = nz(actPrix).multiply(nz(tauxAct)).movePointLeft(2);

        // 2) Plafond de l'adhérent (null si aucun plafond pour l'année du soin)
        PlafondCnam p = lirePlafond(soc, mat, datSoin);
        if (p == null) {
            reponse.setMessage("Plafond non encore initialisé pour cet adhérent");
            return reponse;
        }

        // Déjà consommé = solde estimé CNAM - ancien net de cette ligne + cumul des
        // autres lignes
        BigDecimal dejaConsomme = p.consomme().subtract(sauv).add(cumul);

        // 3) Plafonnement
        reponse.setP_mnt_net(appliquerPlafond(net, honor, dejaConsomme, p.plafond()));
        return reponse;
    }

    private record Appareil(BigDecimal prix, String mois, String devis, int dureeMois) {
    }

    @Transactional(readOnly = true)
    public ReponseCalculMntNet calculerMontantNetApp(String soc, String mat, String numFam, String datSoin,
            BigDecimal mntHnor, String codApp, Long mutMntNet) {

        ReponseCalculMntNet reponse = new ReponseCalculMntNet();

        // La procédure ne renvoyait rien quand mutMntNet est renseigné (voir la
        // remarque 1)
        if (mutMntNet != null) {
            return reponse;
        }

        // Paramètres de l'appareil (appareil inconnu : aucun calcul, comme la
        // procédure)
        List<Appareil> apps = jdbcTemplate.query("""
                select coalesce(prix_app, 0), coalesce(mois, 'N'), coalesce(devis, 'N'), coalesce(duree_app, 0)
                  from ref_appareil
                 where cod_app = ?
                """,
                (rs, i) -> new Appareil(rs.getBigDecimal(1), rs.getString(2), rs.getString(3), rs.getInt(4)),
                codApp);
        if (apps.isEmpty()) {
            return reponse;
        }
        Appareil app = apps.get(0);

        LocalDate dateSoin = (datSoin == null || datSoin.isBlank())
                ? null
                : convertToSqlDate(datSoin).toLocalDate();

        BigDecimal netApp = nz(mntHnor).min(app.prix()); // le plus petit de (honoraires, prix de l'appareil)

        // Dates des soins déjà saisis pour cet appareil (utiles pour les règles
        // mensuelle et périodique)
        boolean controleDates = dateSoin != null && ("O".equals(app.mois()) || app.dureeMois() != 0);
        List<LocalDate> existantes = !controleDates ? List.of() : jdbcTemplate.query("""
                select dat_soin
                  from lig_bult_app
                 where cod_soc  = ?
                   and mat_pers = ?
                   and num_fam  = ?
                   and cod_app  = ?
                   and dat_soin is not null
                """, (rs, i) -> rs.getDate(1).toLocalDate(), soc, mat, numFam, codApp);

        BigDecimal net = null;
        String message = null;

        // Les blocs suivants s'enchaînent comme dans la procédure : un bloc peut
        // écraser le résultat du précédent

        // 1) Droit mensuel
        if ("O".equals(app.mois())) {
            boolean dejaPris = existantes.stream()
                    .anyMatch(d -> d.getMonthValue() == dateSoin.getMonthValue()); // mois seul, sans l'année
            if (!dejaPris) {
                net = netApp;
            } else {
                message = "Vous avez épuiser votre droit mensuel pour cet appareil ";
                net = BigDecimal.ZERO;
            }
        }

        // 2) Devis
        if ("O".equals(app.devis())) {
            message = "Vous devez saisir le montant du devis le mois disant si non il est zéro";
            net = BigDecimal.ZERO;
        }

        // 3) Droit périodique (durée en mois)
        if (app.dureeMois() != 0) {
            LocalDate debut = (dateSoin == null) ? null : dateSoin.minusMonths(app.dureeMois());
            boolean dejaPris = dateSoin != null && existantes.stream()
                    .anyMatch(d -> !d.isBefore(debut) && !d.isAfter(dateSoin));
            if (!dejaPris) {
                net = netApp;
            } else {
                message = "Vous avez épuiser votre droit périodique pour cet appareil ";
                net = BigDecimal.ZERO;
            }
        }

        // 4) Aucune contrainte
        if ("N".equals(app.mois()) && "N".equals(app.devis()) && app.dureeMois() == 0) {
            net = netApp;
        }

        reponse.setP_mnt_net(net);
        reponse.setMessage(message);
        return reponse;
    }

    /** Limite le net aux honoraires puis au plafond restant. */
    private static BigDecimal appliquerPlafond(BigDecimal net, BigDecimal honor,
            BigDecimal dejaConsomme, BigDecimal plafond) {
        BigDecimal reste = plafond.subtract(dejaConsomme);

        if (net.compareTo(honor) > 0) {
            return honor.add(dejaConsomme).compareTo(plafond) > 0 ? reste : honor;
        }

        return net.add(dejaConsomme).compareTo(plafond) > 0 ? reste : net;
    }

    private record PlafondCnam(BigDecimal plafond, BigDecimal consomme) {
    }

    /**
     * Plafond et consommé estimé de l'adhérent, ou null si aucun plafond n'existe
     * pour l'année du soin.
     */
    private PlafondCnam lirePlafond(String soc, String mat, String datSoin) {

        if (datSoin == null || datSoin.isBlank()) {
            return null;
        }

        int annee = convertToSqlDate(datSoin).toLocalDate().getYear();

        Integer nb = jdbcTemplate.queryForObject("""
                select count(*)
                  from plafond_cnam
                 where cod_soc = ?
                   and mat_pers = ?
                   and annee_cnam = ?
                """, Integer.class, soc, mat, annee);
        if (nb == null || nb == 0) {
            return null;
        }

        return jdbcTemplate.queryForObject("""
                select coalesce(plafond, 0), coalesce(sold_cnam_estim, 0)
                  from plafond_cnam
                 where cod_soc = ?
                   and mat_pers = ?
                   and annee_cnam = (select max(annee_cnam)
                                       from plafond_cnam
                                      where cod_soc = ?
                                        and mat_pers = ?)
                """,
                (rs, i) -> new PlafondCnam(rs.getBigDecimal(1), rs.getBigDecimal(2)),
                soc, mat, soc, mat);
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
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
