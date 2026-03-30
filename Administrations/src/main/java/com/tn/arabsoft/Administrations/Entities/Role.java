package com.tn.arabsoft.Administrations.Entities;

import java.time.LocalDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Role")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROLE")
    @SequenceGenerator(name = "SEQ_ROLE", sequenceName = "SEQ_ROLE", allocationSize = 1)
     private Long Id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    private LocalDateTime created_date;
    private LocalDateTime last_modified_date;

//    @Column(name = "comnt")
//    private String comnt;
    
    @OneToMany(mappedBy = "role")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Set<Admstorageautorisation> admstorgeautorisations;
	    
	    
	    
}
