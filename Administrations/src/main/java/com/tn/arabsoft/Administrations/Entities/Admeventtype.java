package com.tn.arabsoft.Administrations.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="Admeventtype")
public class Admeventtype {
	
	@Id
	private Long evt_id;
	private String evt_name;
	private Long evt_evt_id;
	private Long 	sum_id;
	private String evt_action;
	private Long evt_rank	;
	private String evt_maint	;

}
