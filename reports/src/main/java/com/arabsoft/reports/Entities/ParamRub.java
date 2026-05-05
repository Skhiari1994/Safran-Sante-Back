package com.arabsoft.reports.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.ToString;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "param_rapport_rub")
@ToString
@SuppressWarnings({ "java:S116" })
public class ParamRub {

	@Column(name = "cod_rap", nullable = false)
	@JsonProperty("codRap")
	private Long cod_rap;

	private Long cod_param;

	private String lib_param;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parametre_seq")
	@SequenceGenerator(name = "parametre_seq", sequenceName = "seq_id_rap", allocationSize = 1)
	private Long id_rap;

	private String lib_attr;

	private String corres_col;

	private String defaultt;

}
