package com.arabsoft.gestioncotisation.Entities;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestioncotisation.Entities.Cle.CleLigCotisMutPers;
import com.arabsoft.gestioncotisation.Entities.Cle.CotisMutPersID;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CleLigCotisMutPers.class)
@Table(name="LIG_COTIS_MUT_PERS")
public class LigCotisMutPers {

        @Id
        public String cod_soc;
        @Id
        public String mat_pers;
        @Id
        public Long num_cot;

        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        private LocalDate dat_mut;
        public BigDecimal mnt_payer;
        @Id
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        private LocalDate dat_cot;

}
