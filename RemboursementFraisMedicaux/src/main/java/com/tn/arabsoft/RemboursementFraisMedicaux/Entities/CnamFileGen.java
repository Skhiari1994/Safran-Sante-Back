package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "CNAM_FILE_GEN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class CnamFileGen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Ensure ID is auto-generated
    private Long id;

    @Column(name = "cod_soc")  // Maps codSoc to cod_soc in DB
    private String codSoc;

    @Column(name = "cod_bord")  // Maps codBord to cod_bord in DB
    private String codBord;

    @Column(name = "file_name")  // Maps fileName to file_name in DB
    private String fileName;

    @Lob
    @Column(name = "file_content")  // Maps fileContent to file_content in DB
    private String fileContent; // Storing as String because it's a CLOB

    @Column(name = "status")  // Maps status field
    private String status;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @Column(name = "created_at")  // Maps createdAt to created_at in DB
    private LocalDateTime createdAt;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @Column(name = "updated_at")  // Maps updatedAt to updated_at in DB
    private LocalDateTime updatedAt;
}