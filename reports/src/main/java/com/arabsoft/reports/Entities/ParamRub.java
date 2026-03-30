package com.arabsoft.reports.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "param_rapport_rub")

public class ParamRub {

	@Column(name = "COD_RAP", nullable = false)
	@JsonProperty("codRap")
	private Long cod_rap;
	private Long cod_param;
	private String lib_param;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametre_seq")
	@SequenceGenerator(name = "parametre_seq", sequenceName = "seq_id_rap", allocationSize = 1)
	private Long id_rap;
	private String lib_attr	;
	private String corres_col;
	private String defaultt;


}
