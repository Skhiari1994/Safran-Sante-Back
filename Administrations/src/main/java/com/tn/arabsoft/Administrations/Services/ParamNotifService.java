package com.tn.arabsoft.administrations.services;

import com.tn.arabsoft.administrations.entities.ParamNotif;
import com.tn.arabsoft.administrations.repositories.ParamNotifDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ParamNotifService {

    private static final int LONGUEUR_TEXTE = 500;
    private static final int LONGUEUR_PARAM = 30;
    private static final int LONGUEUR_TYPE = 20;
    /** ID_NOTIF est un tinyint : 255 au maximum. */
    private static final int ID_MAX = 255;
    private static final Set<String> NATURES = Set.of("MAIL", "SMS");

    private final ParamNotifDao paramNotifDao;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<ParamNotif> getAll() {
        return paramNotifDao.findAll().stream()
                .sorted(Comparator.comparing(ParamNotif::getIdNotif))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCorps() {
        return jdbcTemplate.queryForList(
                "SELECT RTRIM(cod_corps) AS codCorps, lib_corps AS libCorps FROM corps ORDER BY cod_corps");
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSocietes() {
        return jdbcTemplate.queryForList(
                "SELECT RTRIM(cod_soc) AS codSoc, lib_soc AS libSoc FROM societe ORDER BY cod_soc");
    }

    @Transactional
    public ParamNotif add(ParamNotif demande) {
        ParamNotif notif = new ParamNotif();
        remplir(notif, demande);
        verifier(notif, -1);

        Integer id = paramNotifDao.getNextId();
        if (id > ID_MAX) {
            throw new IllegalArgumentException(
                    "Nombre maximum de notifications atteint (" + ID_MAX + ") : supprimez-en une avant d'en ajouter.");
        }
        notif.setIdNotif(id);
        return paramNotifDao.save(notif);
    }

    @Transactional
    public Optional<ParamNotif> update(Integer id, ParamNotif demande) {
        return paramNotifDao.findById(id).map(notif -> {
            // contrôle sur une copie : l'entité suivie par JPA est écrite en base dès la première requête
            ParamNotif candidat = new ParamNotif();
            remplir(candidat, demande);
            verifier(candidat, id);
            remplir(notif, candidat);
            return paramNotifDao.save(notif);
        });
    }

    /** @return true si la notification existait et a été supprimée. */
    @Transactional
    public boolean delete(Integer id) {
        if (!paramNotifDao.existsById(id)) {
            return false;
        }
        paramNotifDao.deleteById(id);
        return true;
    }

    private void remplir(ParamNotif notif, ParamNotif demande) {
        notif.setTextNotif(texte(demande.getTextNotif()));
        notif.setParam1(texte(demande.getParam1()));
        notif.setParam2(texte(demande.getParam2()));
        notif.setParam3(texte(demande.getParam3()));
        notif.setParam4(texte(demande.getParam4()));
        notif.setParam5(texte(demande.getParam5()));
        notif.setCorpsNotif(texte(demande.getCorpsNotif()));
        notif.setCodSoc(texte(demande.getCodSoc()));
        String type = texte(demande.getTypeNotif());
        notif.setTypeNotif(type == null ? null : type.toUpperCase());
        String nature = texte(demande.getNatNotif());
        notif.setNatNotif(nature == null ? null : nature.toUpperCase());
    }

    private void verifier(ParamNotif n, Integer idExclu) {
        if (n.getTextNotif() == null) {
            throw new IllegalArgumentException("Le texte de la notification est obligatoire.");
        }
        if (n.getTextNotif().length() > LONGUEUR_TEXTE) {
            throw new IllegalArgumentException("Le texte ne doit pas dépasser " + LONGUEUR_TEXTE + " caractères.");
        }
        if (n.getNatNotif() == null || !NATURES.contains(n.getNatNotif())) {
            throw new IllegalArgumentException("La nature doit être MAIL ou SMS.");
        }
        if (n.getTypeNotif() == null) {
            throw new IllegalArgumentException("Le type de notification est obligatoire.");
        }
        if (n.getTypeNotif().length() > LONGUEUR_TYPE) {
            throw new IllegalArgumentException("Le type ne doit pas dépasser " + LONGUEUR_TYPE + " caractères.");
        }
        String[] params = {n.getParam1(), n.getParam2(), n.getParam3(), n.getParam4(), n.getParam5()};
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null && params[i].length() > LONGUEUR_PARAM) {
                throw new IllegalArgumentException(
                        "Le paramètre " + (i + 1) + " ne doit pas dépasser " + LONGUEUR_PARAM + " caractères.");
            }
        }
        if (n.getCorpsNotif() != null && !existe("SELECT COUNT(*) FROM corps WHERE cod_corps = ?", n.getCorpsNotif())) {
            throw new IllegalArgumentException("Le corps « " + n.getCorpsNotif() + " » n'existe pas.");
        }
        if (n.getCodSoc() != null && !existe("SELECT COUNT(*) FROM societe WHERE cod_soc = ?", n.getCodSoc())) {
            throw new IllegalArgumentException("La société « " + n.getCodSoc() + " » n'existe pas.");
        }
        if (paramNotifDao.existeDoublon(n.getNatNotif(), n.getTypeNotif(), n.getTextNotif(), idExclu)) {
            throw new IllegalArgumentException("Une notification de nature " + n.getNatNotif() + " et de type "
                    + n.getTypeNotif() + " avec ce même texte existe déjà.");
        }
    }

    private boolean existe(String sql, String valeur) {
        Integer nombre = jdbcTemplate.queryForObject(sql, Integer.class, valeur);
        return nombre != null && nombre > 0;
    }

    /** Texte sans espaces autour ; une valeur vide devient « pas de valeur ». */
    private static String texte(String valeur) {
        if (valeur == null) {
            return null;
        }
        String propre = valeur.trim();
        return propre.isEmpty() ? null : propre;
    }

}
