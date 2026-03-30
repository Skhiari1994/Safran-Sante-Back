package com.arabsoft.Credits.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "VIR_CARTE")
public class VirCarteLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ordre;

    private String ligne;

    @Column(name = "ID_FILE")
    private String fileId;

    // getters & setters
}

