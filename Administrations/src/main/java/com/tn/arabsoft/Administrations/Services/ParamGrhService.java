package com.tn.arabsoft.administrations.services;

import com.tn.arabsoft.administrations.entities.ParamGrh;
import com.tn.arabsoft.administrations.repositories.ParamGrhDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParamGrhService {

    private final ParamGrhDao paramGrhDao;

    @Transactional(readOnly = true)
    public List<ParamGrh> getAll() {
        return paramGrhDao.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ParamGrh> getById(Integer id) {
        return paramGrhDao.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ParamGrh> getByLibParam(String libParam) {
        return paramGrhDao.findByLibParam(libParam);
    }

    @Transactional
    public ParamGrh add(ParamGrh param) {
        // Validation du format selon le type
        verifierTypeEtValeur(param);

        // Vérification de l'unicité du libellé
        if (param.getLibParam() != null && paramGrhDao.findByLibParam(param.getLibParam()).isPresent()) {
            throw new IllegalArgumentException("Un paramètre avec le code '" + param.getLibParam() + "' existe déjà.");
        }

        // Attribution automatique du prochain ID
        if (param.getIdParam() == null) {
            param.setIdParam(paramGrhDao.getNextId());
        }

        return paramGrhDao.save(param);
    }

    @Transactional
    public Optional<ParamGrh> update(Integer id, ParamGrh form) {
        // Validation du format selon le type
        verifierTypeEtValeur(form);

        return paramGrhDao.findById(id).map(existing -> {
            existing.setCommentaireParam(form.getCommentaireParam());
            existing.setValeur1(form.getValeur1());
            existing.setValeur2(form.getValeur2());
            existing.setValeur3(form.getValeur3());
            existing.setType(form.getType());
            return paramGrhDao.save(existing);
        });
    }

    @Transactional
    public void delete(Integer id) {
        paramGrhDao.deleteById(id);
    }

    /**
     * Contrôle de conformité des valeurs (valeur1, valeur2, valeur3) par rapport au type sélectionné.
     */
    private void verifierTypeEtValeur(ParamGrh param) {
        if (param.getType() == null) {
            param.setType(1);
        }

        // 1. Contrôle obligatoire sur la valeur 1
        if (param.getValeur1() == null || param.getValeur1().trim().isEmpty()) {
            throw new IllegalArgumentException("La valeur 1 est obligatoire.");
        }
        validerFormatValeur(param.getValeur1(), param.getType(), "valeur 1");

        // 2. Contrôle sur la valeur 2 (si renseignée)
        if (param.getValeur2() != null && !param.getValeur2().trim().isEmpty()) {
            validerFormatValeur(param.getValeur2(), param.getType(), "valeur 2");
        }

        // 3. Contrôle sur la valeur 3 (si renseignée)
        if (param.getValeur3() != null && !param.getValeur3().trim().isEmpty()) {
            validerFormatValeur(param.getValeur3(), param.getType(), "valeur 3");
        }
    }

    private void validerFormatValeur(String valeur, int type, String nomChamp) {
        String val = valeur.trim();

        switch (type) {
            case 1: // 1 - Nombre entier
                if (!val.matches("^-?\\d+$")) {
                    throw new IllegalArgumentException("Pour un type 'Nombre entier', la " + nomChamp + " doit contenir uniquement des chiffres sans virgule (ex: 120).");
                }
                break;

            case 2: // 2 - Montant / Décimal
                if (!val.matches("^-?\\d+(\\.\\d+)?$")) {
                    throw new IllegalArgumentException("Pour un type 'Montant / Décimal', la " + nomChamp + " doit être un nombre valide (ex: 150.500).");
                }
                break;

            case 4: // 4 - Booléen
                if (!val.equals("1") && !val.equals("0")) {
                    throw new IllegalArgumentException("Pour un type 'Booléen', la " + nomChamp + " doit obligatoirement valoir 1 (Oui) ou 0 (Non).");
                }
                break;

            case 5: // 5 - Date (AAAA-MM-JJ)
                if (!val.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    throw new IllegalArgumentException("Pour un type 'Date', la " + nomChamp + " doit respecter le format AAAA-MM-JJ (ex: 2026-12-31).");
                }
                break;

            case 3: // 3 - Texte libre
            default:
                break;
        }
    }
}
