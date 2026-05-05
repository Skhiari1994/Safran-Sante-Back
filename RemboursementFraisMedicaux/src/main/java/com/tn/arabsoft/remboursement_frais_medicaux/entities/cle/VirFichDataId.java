package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VirFichDataId implements Serializable {
    @Column(name = "seq_")
    private Long seq;

    @Column(name = "ligne", length = 5000)
    private String ligne;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getLigne() {
        return ligne;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VirFichDataId that = (VirFichDataId) o;
        return Objects.equals(seq, that.seq) && Objects.equals(ligne, that.ligne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, ligne);
    }
}