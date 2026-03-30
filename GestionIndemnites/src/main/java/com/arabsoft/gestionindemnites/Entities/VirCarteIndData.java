package com.arabsoft.gestionindemnites.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.security.Timestamp;

@Entity
@Table(name = "VIR_CARTE_IND_DATA")
@Data
public class VirCarteIndData {
    @Id
    @Column(name = "seq_")
    private Long seq;
    @Column(name = "ligne")
    private String ligne;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at")
    private Timestamp createdAt;
}

