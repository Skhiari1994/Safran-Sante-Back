package com.arabsoft.reports.Entities;

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
public class RapportRub {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rapport_seq")
	@SequenceGenerator(name = "rapport_seq", sequenceName = "seq_cod_rap", allocationSize = 1)

	private Long cod_rap;
	private String lib_rap;
	private String abrv_rap;
	private String lib_rap_jsp;
	private String bean_name;
	private String path_report;
	private String titre;
	private String iterator_name;
	private Long ndecision;
	private String notif_mail;
	private String message;
	private String subject;
	private String second_envoi;
	private String minute_envoi;
	private String heure_envoi;
	private String jour_envoi;
	private String mois_envoi;
	private String annee_envoi;

	
}
