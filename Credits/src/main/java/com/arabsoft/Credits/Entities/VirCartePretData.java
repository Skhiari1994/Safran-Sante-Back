package com.arabsoft.Credits.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "VIR_CARTE_PRET_DATA")
public class VirCartePretData {
    @Id
    @Column(name = "seq_")
    private Long seq;

    @Column(name = "ligne", columnDefinition = "VARCHAR2(4000)")
    private String ligne;

    @Column(name = "status", columnDefinition = "VARCHAR2(20)")
    private String status;

    public static class VirCartePretDataId implements Serializable {
        private Long seq;
        private String ligne;

        public VirCartePretDataId() {}
        public VirCartePretDataId(Long seq, String ligne) {
            this.seq = seq;
            this.ligne = ligne;
        }

        public Long getSeq() { return seq; }
        public void setSeq(Long seq) { this.seq = seq; }
        public String getLigne() { return ligne; }
        public void setLigne(String ligne) { this.ligne = ligne; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VirCartePretDataId that = (VirCartePretDataId) o;
            return Objects.equals(seq, that.seq) && Objects.equals(ligne, that.ligne);
        }

        @Override
        public int hashCode() {
            return Objects.hash(seq, ligne);
        }
    }
}