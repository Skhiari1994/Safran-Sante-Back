package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CNAM_FICH_DATA")
@IdClass(CnamFichData.CnamFichDataId.class)
public class CnamFichData {

    @Id
    @Column(name = "seq_")
    private Long seq;

    @Id
    @Column(name = "ligne", columnDefinition = "VARCHAR2(1000)")
    private String ligne;

    @Column(name = "status", columnDefinition = "VARCHAR2(20)")
    private String status;

    public Long getSeq() { return seq; }
    public void setSeq(Long seq) { this.seq = seq; }
    public String getLigne() { return ligne; }
    public void setLigne(String ligne) { this.ligne = ligne; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static class CnamFichDataId implements Serializable {
        private Long seq;
        private String ligne;

        public CnamFichDataId() {}
        public CnamFichDataId(Long seq, String ligne) {
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
            if (!(o instanceof CnamFichDataId)) return false;
            CnamFichDataId that = (CnamFichDataId) o;
            return Objects.equals(seq, that.seq) && Objects.equals(ligne, that.ligne);
        }

        @Override
        public int hashCode() {
            return Objects.hash(seq, ligne);
        }
    }
}
