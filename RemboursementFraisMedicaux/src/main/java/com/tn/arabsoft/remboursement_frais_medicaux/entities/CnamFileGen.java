package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "cnam_file_gen")
public class CnamFileGen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_soc")
    private String codSoc;

    @Column(name = "cod_bord")
    private String codBord;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "file_content")
    private String fileContent;

    @Column(name = "status")
    private String status;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}