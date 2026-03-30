package com.tn.arabsoft.Administrations.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="Admsubmodule")
public class Admsubmodule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqSUB")
    @SequenceGenerator(name = "seqSUB", sequenceName = "SEQ_ADMSUBMODULE", allocationSize = 1)
	private Long sum_id;
	private String sum_name;
	private Long sum_rank;
	private Long mod_id;
	private String sum_status;
	private String sum_rout	;
	private byte[] mdl_icon	;
	private String mdl_icon1	;
	private String collapseid;

	

}
