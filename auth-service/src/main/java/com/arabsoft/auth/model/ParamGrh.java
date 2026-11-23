package com.arabsoft.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "PARAM_GRH")
public class ParamGrh {

    @Id
    @Column(name = "id_param")
    private Integer idParam;

    @Column(name = "lib_param", nullable = false, unique = true, length = 100)
    private String libParam;

    @Column(name = "commentaire_param", length = 500)
    private String commentaireParam;

    @Column(name = "valeur1", nullable = false, length = 50)
    private String valeur1;

    @Column(name = "valeur2", length = 50)
    private String valeur2;

    @Column(name = "valeur3", length = 50)
    private String valeur3;

    @Column(name = "type")
    private Integer type;
}
