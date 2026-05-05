package com.tn.arabsoft.administrations.entities;

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
@Entity
@Table(name = "admeventtype")
@ToString
@SuppressWarnings({ "java:S101", "java:S116" })
public class Admeventtype {

	@Id
	private Long evt_id;
	private String evt_name;
	private Long evt_evt_id;
	private Long sum_id;
	private String evt_action;
	private Long evt_rank;
	private String evt_maint;

}
