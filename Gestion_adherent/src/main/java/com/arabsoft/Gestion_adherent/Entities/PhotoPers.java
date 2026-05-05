package com.arabsoft.gestion_adherent.entities;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photo_pers")
@SuppressWarnings({ "java:S116" })
public class PhotoPers {

    private String cod_soc;

    private String mat_pers;

    private byte[] photo;

    private String file_name;

    private String content_type;

    private String path;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPhoto")
    @SequenceGenerator(name = "seqPhoto", sequenceName = "PHOTO_PERS_SEQ", allocationSize = 1)
    private Long id_photo_pers;

}
