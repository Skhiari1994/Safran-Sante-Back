package com.arabsoft.referentiel.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Organisme (mutuelle ou assurance) de la table PARAM_ORGANISMES.
 * Seules les colonnes gérées par l'écran sont mappées : les autres colonnes de la table ne sont jamais modifiées.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PARAM_ORGANISMES")
public class ParamOrganisme {

    /** COD_ORG est un tinyint : 255 au maximum. */
    @Id
    @Column(name = "COD_ORG")
    private Integer codOrg;

    @Column(name = "ABRV_ORG", length = 20)
    private String abrvOrg;

    @Column(name = "LIB_ORG", length = 100)
    private String libOrg;

    @Column(name = "ADR_ORG", length = 100)
    private String adrOrg;

    @Column(name = "NAT_ORG", length = 10)
    private String natOrg;

    @Column(name = "FAX_ORG", length = 20)
    private String faxOrg;

    @Column(name = "TEL_ORG", length = 40)
    private String telOrg;

    @Column(name = "EMAIL_ORG", length = 40)
    private String emailOrg;

    @Column(name = "IDENT_FSC", length = 20)
    private String identFsc;

    @Column(name = "GSM", length = 20)
    private String gsm;

}
