package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.entities.cle.ClePhotoPers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "photo_pers")
@IdClass(ClePhotoPers.class)
@SuppressWarnings({ "java:S116" })
public class PhotoPers {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    private byte[] photo;

    private String file_name;

    private String content_type;

    private String path;

}
