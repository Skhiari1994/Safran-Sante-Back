package com.arabsoft.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="ADMUSERPROFIL")
public class Admuserprofil {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
  @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_ADMUSERPROFIL", allocationSize = 1)
  private Long  use_id;
    private Long pru_id;
  private String usp_granted	 ;
    private LocalDate   usp_startdt	;
    private LocalDate  usp_enddt;
    private LocalDate  usp_credt;
    private LocalDate  usp_updtdt;
    private Long  usp_updtby;
    private Long  dvt_id;
    private Long  usp_id;


}
