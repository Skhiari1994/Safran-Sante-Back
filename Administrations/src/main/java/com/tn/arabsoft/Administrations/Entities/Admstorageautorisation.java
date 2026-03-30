package com.tn.arabsoft.Administrations.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;



@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "admstorageautorisation")
public class Admstorageautorisation {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqLibre")
    @SequenceGenerator(name = "seqLibre", sequenceName = "ADMSTORAGEAUTORISATION_SEQ", allocationSize = 1)
    private Long id_autho;
	@ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
 
    @ManyToOne
    @JoinColumn(name = "sum_id", nullable = true)
    private Admsubmodule admsubmodule;
    
    @ManyToOne
    @JoinColumn(name = "evt_id", nullable = true)
    private Admeventtype admeventtype;


}
