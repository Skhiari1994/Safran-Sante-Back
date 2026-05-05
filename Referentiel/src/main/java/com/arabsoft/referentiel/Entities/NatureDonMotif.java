package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.CleNatureDonMotif;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "nature_don_motif")
@ToString
@IdClass(CleNatureDonMotif.class)
@SuppressWarnings({ "java:S116" })
public class NatureDonMotif {

   @Id
   private String nat_don;

   @Id
   private String cod_affect;

}
