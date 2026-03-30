package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.VirFichDataId;
import jakarta.persistence.*;
import java.util.Date;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "VIR_FICH_DATA")
public class VirFichData {
    @EmbeddedId
    private VirFichDataId id;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "file_name", length = 100)
    private String fileName;

    @Column(name = "created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    public VirFichDataId getId() { return id; }
    public void setId(VirFichDataId id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}
