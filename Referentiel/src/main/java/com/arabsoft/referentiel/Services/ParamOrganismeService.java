package com.arabsoft.referentiel.services;

import com.arabsoft.referentiel.entities.ParamOrganisme;
import com.arabsoft.referentiel.repositories.ParamOrganismeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ParamOrganismeService {

    private static final int LONGUEUR_ABREVIATION = 20;
    private static final int LONGUEUR_LIBELLE = 100;
    private static final int LONGUEUR_ADRESSE = 100;
    private static final int LONGUEUR_NATURE = 10;
    private static final int LONGUEUR_FAX = 20;
    private static final int LONGUEUR_TEL = 40;
    private static final int LONGUEUR_EMAIL = 40;
    private static final int LONGUEUR_IDENT_FISCALE = 20;
    private static final int LONGUEUR_GSM = 20;
    /** COD_ORG est un tinyint : 255 au maximum. */
    private static final int CODE_MAX = 255;

    private static final Pattern TELEPHONE = Pattern.compile("^[0-9 +()./,-]+$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern IDENT_FISCALE = Pattern.compile("^[A-Z0-9/ .-]+$");

    private final ParamOrganismeRepository repository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public List<ParamOrganisme> getAll() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(ParamOrganisme::getCodOrg))
                .toList();
    }

    @Transactional
    public ParamOrganisme add(ParamOrganisme demande) {
        ParamOrganisme organisme = new ParamOrganisme();
        remplir(organisme, demande);
        verifier(organisme, -1);

        Integer code = repository.getNextId();
        if (code > CODE_MAX) {
            throw new IllegalArgumentException(
                    "Nombre maximum d'organismes atteint (" + CODE_MAX + ") : supprimez-en un avant d'en ajouter.");
        }
        organisme.setCodOrg(code);
        return repository.saveAndFlush(organisme);
    }

    @Transactional
    public Optional<ParamOrganisme> update(Integer code, ParamOrganisme demande) {
        return repository.findById(code).map(organisme -> {
            // contrôle sur une copie : l'entité suivie par JPA est écrite en base dès la première requête
            ParamOrganisme candidat = new ParamOrganisme();
            remplir(candidat, demande);
            verifier(candidat, code);
            remplir(organisme, candidat);
            return repository.saveAndFlush(organisme);
        });
    }

    /**
     * @return true si l'organisme existait et a été supprimé.
     * @throws IllegalStateException si l'organisme est encore rattaché à des assurances.
     */
    @Transactional
    public boolean delete(Integer code) {
        if (!repository.existsById(code)) {
            return false;
        }
        Integer assurances = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM assurance WHERE cod_org = ?", Integer.class, code);
        if (assurances != null && assurances > 0) {
            throw new IllegalStateException("Impossible de supprimer cet organisme : il est rattaché à " + assurances
                    + (assurances == 1 ? " assurance." : " assurances."));
        }
        repository.deleteById(code);
        repository.flush();
        return true;
    }

    private void remplir(ParamOrganisme cible, ParamOrganisme source) {
        String abreviation = texte(source.getAbrvOrg());
        cible.setAbrvOrg(abreviation == null ? null : abreviation.toUpperCase(Locale.ROOT));
        cible.setLibOrg(texte(source.getLibOrg()));
        cible.setAdrOrg(texte(source.getAdrOrg()));
        cible.setNatOrg(texte(source.getNatOrg()));
        cible.setFaxOrg(texte(source.getFaxOrg()));
        cible.setTelOrg(texte(source.getTelOrg()));
        cible.setEmailOrg(texte(source.getEmailOrg()));
        String identFiscale = texte(source.getIdentFsc());
        cible.setIdentFsc(identFiscale == null ? null : identFiscale.toUpperCase(Locale.ROOT));
        cible.setGsm(texte(source.getGsm()));
    }

    private void verifier(ParamOrganisme o, Integer idExclu) {
        if (o.getLibOrg() == null) {
            throw new IllegalArgumentException("Le libellé de l'organisme est obligatoire.");
        }
        longueur(o.getLibOrg(), LONGUEUR_LIBELLE, "Le libellé");
        longueur(o.getAbrvOrg(), LONGUEUR_ABREVIATION, "L'abréviation");
        longueur(o.getAdrOrg(), LONGUEUR_ADRESSE, "L'adresse");
        longueur(o.getNatOrg(), LONGUEUR_NATURE, "La nature");
        longueur(o.getIdentFsc(), LONGUEUR_IDENT_FISCALE, "L'identifiant fiscal");
        telephone(o.getTelOrg(), LONGUEUR_TEL, "Le téléphone");
        telephone(o.getFaxOrg(), LONGUEUR_FAX, "Le fax");
        telephone(o.getGsm(), LONGUEUR_GSM, "Le GSM");
        longueur(o.getEmailOrg(), LONGUEUR_EMAIL, "L'email");
        if (o.getEmailOrg() != null && !EMAIL.matcher(o.getEmailOrg()).matches()) {
            throw new IllegalArgumentException("L'email n'est pas valide.");
        }
        if (o.getIdentFsc() != null && !IDENT_FISCALE.matcher(o.getIdentFsc()).matches()) {
            throw new IllegalArgumentException(
                    "L'identifiant fiscal ne peut contenir que des lettres, des chiffres et les signes / . -");
        }
        if (repository.existeLibelle(o.getLibOrg(), idExclu)) {
            throw new IllegalArgumentException("Un organisme nommé « " + o.getLibOrg() + " » existe déjà.");
        }
    }

    private static void longueur(String valeur, int max, String champ) {
        if (valeur != null && valeur.length() > max) {
            throw new IllegalArgumentException(champ + " ne doit pas dépasser " + max + " caractères.");
        }
    }

    private static void telephone(String valeur, int max, String champ) {
        longueur(valeur, max, champ);
        if (valeur != null && !TELEPHONE.matcher(valeur).matches()) {
            throw new IllegalArgumentException(champ + " ne peut contenir que des chiffres, des espaces et les signes + ( ) . , / -");
        }
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
