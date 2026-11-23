USE [ramidb]
GO

/*
Retour arriere de trg_lig_bult.sql : supprime le trigger unique et recree les 3 triggers d'origine
(insert_lig_bult_insert, insert_lig_bult_update, insert_lig_bult_delete), tels qu'exportes le 23/11/2026.
*/

BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.trg_lig_bult', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_lig_bult;
IF OBJECT_ID(N'dbo.insert_lig_bult_insert', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_lig_bult_insert;
IF OBJECT_ID(N'dbo.insert_lig_bult_update', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_lig_bult_update;
IF OBJECT_ID(N'dbo.insert_lig_bult_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_lig_bult_delete;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [insert_lig_bult_insert] on [LIG_BULT] 
for insert 
as
begin

DECLARE
	@v_indic      varchar(1),	  
	@v_nat_act    varchar(1),
	@v_plafonne   varchar(1),
	@v_taux_act   NUMERIC(5,2),
	@v_mtt_acte   NUMERIC(10,3),
	@v_plafond    NUMERIC(10,3),
	@w_plafond     NUMERIC(10,3),
	@w_sold_plaf   NUMERIC(10,3),
	@mut_mnt_net  NUMERIC(10,3),
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@vprix_act NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		

	@v_proratat varchar(1),
	

	
	--for cursor
	@NEW_cod_soc varchar(max),
	@NEW_mat_pers varchar(max),
	@NEW_NUM_FAM  int,
	@NEW_dat_soin   DATE,
	@NEW_ABRV_ACT varchar(max),
	@v_ctr_duree varchar(1),
	@NEW_indice numeric(15,3),
	@NEW_mnt_honor numeric(15,3),
	@NEW_mnt_net numeric(15,3),
	@v_duree_act numeric(6),
	@v_imput_plaf varchar(1),
	@NEW_NUM_SOIN varchar(max),
	@NEW_mnt_remb  varchar(max),
	@NEW_dat_act varchar(max),
	@NEW_MUT_MNT_NET numeric(15,3),
	@NEW_NUM_LIG numeric(6),
	@NEW_anc_mnt_honor numeric(15,3),
	@NEW_COD_APP varchar(max),
	
	--- for conjoint----------------
	@new_mnt_net_conj numeric(15,3),
	@Mat_Pers_Conj varchar(max),
	@Cod_Soc_Conj varchar(max),
	@numfamC int
	Print N'No erreur found';
	
		begin 
		
			select @NEW_NUM_LIG = NUM_LIG from inserted i ;
			select @NEW_indice = indice from inserted i ;
			select @NEW_mnt_honor = mnt_honor from inserted i ;
			select @NEW_mnt_net = mnt_net from inserted i ;
		
			select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_NUM_FAM = NUM_FAM from inserted i ;
		
			select @NEW_dat_soin = DAT_SOIN from inserted i ;
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @NEW_NUM_SOIN = NUM_SOIN from inserted i;
		
			select @NEW_dat_act = dat_act from inserted i;
			
			select @new_mnt_net_conj = MNT_NET_CONJ from inserted i;  -- recuperer mnt_net_conj
			
				if(@NEW_NUM_FAM = 0 )
			begin
			set @numfamC = 99
			end
			else
			begin
			if(@NEW_NUM_FAM = 99)
			begin 
			set @numfamC = 0
			end
			else
			begin
			set @numfamC = @NEW_NUM_FAM
			end
			end
			
			 --- select matConj && Cod_soc_conj--------------------------------------------------------------------
    select @Mat_Pers_Conj=f.MAT_PERS_CONJ,@Cod_Soc_Conj=f.cod_soc_conj from FAMILLE f 
    where f.MAT_PERS=@NEW_mat_pers
     and f.COD_SOC=@NEW_cod_soc 
     and f.NUM_FAM=@numfamC 
     and f.PARENTE='C'
     
   ------------------------------------------------------------------------------------------------------
   
			
			select @v_indic= ISNULL(a_indice,'N'),
			@v_nat_act =ISNULL(nat_act,'M'),
			@v_plafonne =ISNULL(plafonne,'N'),
			@v_taux_act =ISNULL(taux_act,0)
			,@v_mtt_acte = ISNULL(mtt_acte,0),
			@v_plafond = ISNULL(plafond,0)
			,@v_proratat=ISNULL(proratat,'N')
			from bareme_remb
			where COD_FIL = (select cod_fil 
			from pers_affil where cod_soc = @NEW_cod_soc 
			and mat_pers = @NEW_mat_pers 
			and courant = 'O')
			and COD_ASSUR = (select cod_assur 
			from personnel where cod_soc = @NEW_cod_soc 
			and mat_pers = @NEW_mat_pers)
			and ABRV_ACT = @NEW_ABRV_ACT;
			
			begin try
			select @v_ctr_duree=isnull(ctr_duree,'N') ,
			@v_duree_act=isnull(duree_act,0),
			@v_imput_plaf=isnull(IMPUT_PLAF,'O')  
			from acte
			where abrv_act = @new_abrv_act;
			end try
			begin catch
			
			end catch
            --to_number(to_char(@datsoin ,'yyyy'));
			

     
			
			Print N' 11  @@@v_indic' + cast( @v_indic as varchar ); 
		
				---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
/*TODO*/		
			
				Print N'@@@NEW_mnt_net' + cast ( @NEW_mnt_net as varchar );
				if @v_imput_plaf = 'O' 
				begin
				
		----- mise a jour du plafond conjoin---------------------------------
					
					update plafond_assur set SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM,0)+@new_mnt_net_conj
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj 
					and num_fam=@numfamC
					--and annee_assur  = to_number(to_char(@OLD_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj
					and num_fam=@numfamC);
					
		---------------------------------------------------------------------
				
				
				
				
					update plafond_assur set SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM,0) + ISNULL(@NEW_mnt_net,0)
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers 
					and num_fam = @NEW_NUM_FAM
					--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers
					and num_fam = @NEW_NUM_FAM);
					
					
					if(@v_proratat = 'O')
					begin
					
						----- mise a jour du plafond conjoin---------------------------------
					
				update plafond_assur_detail set SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM,0) 
					+ ISNULL(@new_mnt_net_conj,0)
				
					
					where cod_soc = @Cod_Soc_Conj and mat_pers = @Mat_Pers_Conj 
					and num_fam = @numfamC 
					and ABRV_ACT = @NEW_ABRV_ACT and COD_FIL = (select cod_fil 
					from pers_affil where cod_soc = @Cod_Soc_Conj 
					and mat_pers = @Mat_Pers_Conj 
					and courant = 'O')
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur 
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj
					and num_fam = @numfamC ); 
					
		---------------------------------------------------------------------
						
					
					update plafond_assur_detail set SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM,0) 
					+ ISNULL(@NEW_mnt_net,0)
				
					
					where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam 
					and ABRV_ACT = @NEW_ABRV_ACT and COD_FIL = (select cod_fil 
					from pers_affil where cod_soc = @NEW_cod_soc 
					and mat_pers = @NEW_mat_pers 
					and courant = 'O')
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers
					and num_fam = @NEW_num_fam ); 
						
						
						
					end
					
					
			---------------------------------------------------------------------		
					
					
				end ;
					disable trigger lig_bult.insert_lig_bult_update on lig_bult;
				
				PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
				update lig_bult   
				set IMPUT_PLAF=@v_imput_plaf,
				 CTR_DUREE=@v_ctr_duree,
				 DUREE_ACT=@v_duree_act,
				 NAT_ACT=@v_nat_act,
				  MTT_ACTE=@v_mtt_acte,
				  TAUX_ACT=@v_taux_act,
				   PLAFONNE=@v_plafonne,
				    PLAFOND=@v_plafond,
				     A_INDICE=@v_indic
				from lig_bult a
				join inserted b on (
				 a.cod_soc =  b.cod_soc
				and a.mat_pers = b.mat_pers
				and a.num_fam  = b.num_fam
				and a.dat_soin = b.dat_soin 
				and a.NUM_SOIN=  b.NUM_SOIN 
				and a.abrv_act = b.abrv_act);
				
					enable trigger lig_bult.insert_lig_bult_update on lig_bult;
				
	END		
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [insert_lig_bult_update] on [LIG_BULT] 
for update 
as
begin

DECLARE
	@v_indic      varchar(1),	  
	@v_nat_act    varchar(1),
	@v_plafonne   varchar(1),
	@v_taux_act   NUMERIC(5,2),
	@v_mtt_acte   NUMERIC(10,3),
	@v_plafond    NUMERIC(10,3),
	@w_plafond     NUMERIC(10,3),
	@w_sold_plaf   NUMERIC(10,3),
	@mut_mnt_net  NUMERIC(10,3),
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@vprix_act NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		


	@v_proratat varchar(1),
	
	--for cursor
	@NEW_cod_soc varchar(max),
	@NEW_mat_pers varchar(max),
	@NEW_NUM_FAM  int,
	@NEW_dat_soin   DATE,
	@NEW_ABRV_ACT varchar(max),
	@v_ctr_duree varchar(1),
	@NEW_indice numeric(15,3),
	@NEW_mnt_honor numeric(15,3),
	@NEW_mnt_net numeric(15,3),
	@v_duree_act numeric(6),
	@v_imput_plaf varchar(1),
	@NEW_NUM_SOIN varchar(max),
	@NEW_mnt_remb  varchar(max),
	@NEW_dat_act varchar(max),
	@NEW_MUT_MNT_NET numeric(15,3),
	@NEW_NUM_LIG numeric(6),
	@NEW_anc_mnt_honor numeric(15,3),
	@NEW_COD_APP varchar(max),
	
	
	
	@OLD_NUM_SOIN varchar(max),
	@OLD_cod_soc varchar(max),
	@OLD_mat_pers varchar(max),
	@OLD_num_fam int,
	@OLD_dat_soin date,
	@OLD_abrv_act varchar(max),
	@OLD_mnt_honor numeric(15,3),
	@OLD_mut_mnt_net numeric(15,3),
	@vsold_ectim numeric(15,3), 
	@OLD_mnt_net numeric(15,3),
	@OLD_NUM_LIG numeric (6) ,
	
	
	
	-----for conjoint----------------
	@Mat_Pers_Conj varchar(max),
	@Cod_Soc_Conj  varchar(max),
	@vsold_ectim_conj numeric(15,3),
	@old_mnt_net_conj numeric(15,3),
	@new_mnt_net_conj numeric(15,3),
	@numfamC int
	Print N'No erreur found';
	
		begin 
		
			select @NEW_NUM_LIG = NUM_LIG from inserted i ;
			select @NEW_indice = indice from inserted i ;
			select @NEW_mnt_honor = mnt_honor from inserted i ;
			select @NEW_mnt_net = mnt_net from inserted i ;
		
			select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_NUM_FAM = NUM_FAM from inserted i ;
		
			select @NEW_dat_soin = DAT_SOIN from inserted i ;
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @NEW_NUM_SOIN = NUM_SOIN from inserted i;
		
			select @NEW_dat_act = dat_act from inserted i;
			
			
			select @OLD_NUM_SOIN = NUM_SOIN from inserted i ;
			
			select @new_mnt_net_conj = MNT_NET_CONJ from inserted i;
			select @OLD_cod_soc = cod_soc from deleted i ;
			select @OLD_mat_pers = mat_pers from deleted i ;
			select @OLD_num_fam = NUM_FAM from deleted i ;
			select @OLD_dat_soin = DAT_SOIN from deleted i ;
			select @OLD_abrv_act = abrv_act from deleted i ;
			select @OLD_mnt_honor = isnull(mnt_honor,0) from deleted i ;
			select @OLD_mnt_net = isnull(mnt_net,0) from deleted i ;
			
			select @OLD_NUM_LIG = NUM_LIG from deleted i ;
            select @old_mnt_net_conj = MNT_NET_CONJ from deleted i;
			
			
				if(@NEW_NUM_FAM = 0 )
			begin
			set @numfamC = 99
			end
			else
			begin
			if(@NEW_NUM_FAM = 99)
			begin 
			set @numfamC = 0
			end
			else
			begin
			set @numfamC = @NEW_NUM_FAM
			end
			end
			
			
			--- select matConj && Cod_soc_conj--------------------------------------------------------------------
    select @Mat_Pers_Conj=f.MAT_PERS_CONJ,@Cod_Soc_Conj=f.cod_soc_conj from FAMILLE f 
    where f.MAT_PERS=@OLD_mat_pers
     and f.COD_SOC=@OLD_cod_soc 
     and f.NUM_FAM=@numfamC 
     and f.PARENTE='C'
     
   ------------------------------------------------------------------------------------------------------
   
   
			
			select @v_indic= ISNULL(a_indice,'N'),
			@v_nat_act =ISNULL(nat_act,'M'),
			@v_plafonne =ISNULL(plafonne,'N'),
			@v_taux_act =ISNULL(taux_act,0)
			,@v_mtt_acte = ISNULL(mtt_acte,0),
			@v_plafond = ISNULL(plafond,0)
			,@v_proratat=ISNULL(proratat,'N')
			from bareme_remb
			where COD_FIL = (select cod_fil 
			from pers_affil where cod_soc = @NEW_cod_soc 
			and mat_pers = @NEW_mat_pers 
			and courant = 'O')
			and COD_ASSUR = (select cod_assur 
			from personnel where cod_soc = @NEW_cod_soc 
			and mat_pers = @NEW_mat_pers)
			and ABRV_ACT = @NEW_ABRV_ACT;
			
			begin try
			select @v_ctr_duree=isnull(ctr_duree,'N') ,
			@v_duree_act=isnull(duree_act,0),
			@v_imput_plaf=isnull(IMPUT_PLAF,'O')  
			from acte
			where abrv_act = @new_abrv_act;
			end try
			begin catch
			
			end catch
            --to_number(to_char(@datsoin ,'yyyy'));
			

     
			
			Print N' 11  @@@v_indic' + cast( @v_indic as varchar ); 
		
				---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
/*TODO*/		
			
				if @v_imput_plaf = 'O' 
				begin
				
				----update plafond_cnam
				select @vsold_ectim=isnull(SOLD_ASSUR_ESTIM,0)  from plafond_assur
				where cod_soc = @OLD_cod_soc
				and mat_pers = @OLD_mat_pers 
				and num_fam = @NEW_NUM_FAM
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam = @NEW_NUM_FAM);
				Print N'@@@OLD_mnt_net' + cast ( @OLD_mnt_net as varchar );
				Print N'@@@NEW_mnt_net' + cast ( @NEW_mnt_net as varchar );
				Print N'@@@@vsold_ectim' + cast ( @vsold_ectim as varchar );
				set @vsold_ectim = @vsold_ectim - isnull(@OLD_mnt_net,0) + isnull(@NEW_mnt_net,0);
				if @vsold_ectim < 0 
				begin
					set @vsold_ectim = 0;
				end 
			Print N'@@@@vsold_ectim' + cast ( @vsold_ectim as varchar );
				
					update plafond_assur set SOLD_ASSUR_ESTIM = @vsold_ectim
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers 
					and num_fam = @NEW_NUM_FAM
					--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers
					and num_fam = @NEW_NUM_FAM);
					
					
					
	---- update plafond conjoint-------------------------------------------------------------------------------
	
	
	select @vsold_ectim_conj=isnull(SOLD_ASSUR_ESTIM,0)  from plafond_assur
				where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj 
				and num_fam = @numfamC
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam = @numfamC);
				
				set @vsold_ectim_conj = @vsold_ectim_conj - isnull(@old_mnt_net_conj,0) + isnull(@new_mnt_net_conj,0);
				if @vsold_ectim_conj < 0 
				begin
					set @vsold_ectim_conj = 0;
				end 
			Print N'@@@@vsold_ectim' + cast ( @vsold_ectim as varchar );
				
					update plafond_assur set SOLD_ASSUR_ESTIM = @vsold_ectim_conj
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj 
					and num_fam = @numfamC
					--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj
					and num_fam = @numfamC);
	-----------------------------------------------------------------------------------------------------------
					
					
					
					
					
					
					if(@v_proratat = 'O')
					begin
					
						----- mise a jour du plafond conjoin---------------------------------
					
			----update plafond_cnam
				select @vsold_ectim=isnull(SOLD_ASSUR_ESTIM,0)  from PLAFOND_ASSUR_DETAIL
				where cod_soc = @OLD_cod_soc
				and mat_pers = @OLD_mat_pers 
				and num_fam = @NEW_NUM_FAM
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from PLAFOND_ASSUR_DETAIL
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam = @NEW_NUM_FAM);
				Print N'@@@OLD_mnt_net' + cast ( @OLD_mnt_net as varchar );
				Print N'@@@NEW_mnt_net' + cast ( @NEW_mnt_net as varchar );
				Print N'@@@@vsold_ectim' + cast ( @vsold_ectim as varchar );
				set @vsold_ectim = @vsold_ectim - isnull(@OLD_mnt_net,0) + isnull(@NEW_mnt_net,0);
				if @vsold_ectim < 0 
				begin
					set @vsold_ectim = 0;
				end 
			Print N'@@@@vsold_ectim' + cast ( @vsold_ectim as varchar );
				
					update PLAFOND_ASSUR_DETAIL set SOLD_ASSUR_ESTIM = @vsold_ectim
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers 
					and num_fam = @NEW_NUM_FAM
					--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from PLAFOND_ASSUR_DETAIL
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers
					and num_fam = @NEW_NUM_FAM);
					
		---------------------------------------------------------------------
						
					
					select @vsold_ectim_conj=isnull(SOLD_ASSUR_ESTIM,0)  from PLAFOND_ASSUR_DETAIL
				where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj 
				and num_fam = @numfamC
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from PLAFOND_ASSUR_DETAIL
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam = @numfamC);
				
				set @vsold_ectim_conj = @vsold_ectim_conj - isnull(@old_mnt_net_conj,0) + isnull(@new_mnt_net_conj,0);
				if @vsold_ectim_conj < 0 
				begin
					set @vsold_ectim_conj = 0;
				end 
			Print N'@@@@vsold_ectim' + cast ( @vsold_ectim as varchar );
				
					update PLAFOND_ASSUR_DETAIL set SOLD_ASSUR_ESTIM = @vsold_ectim_conj
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj 
					and num_fam = @numfamC
					--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from PLAFOND_ASSUR_DETAIL
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj
					and num_fam = @numfamC);
						
						
						
					end
					
					
			---------------------------------------------------------------------		
					
						
					
					
					
					
					
					
					
					
					
					
					
				end ;
				
				PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
				update lig_bult   
				set IMPUT_PLAF=@v_imput_plaf,
				 CTR_DUREE=@v_ctr_duree,
				 DUREE_ACT=@v_duree_act,
				 NAT_ACT=@v_nat_act,
				  MTT_ACTE=@v_mtt_acte,
				  TAUX_ACT=@v_taux_act,
				   PLAFONNE=@v_plafonne,
				    PLAFOND=@v_plafond,
				     A_INDICE=@v_indic
				from lig_bult a
				join inserted b on (
				 a.cod_soc =  b.cod_soc
				and a.mat_pers = b.mat_pers
				and a.num_fam  = b.num_fam
				and a.dat_soin = b.dat_soin 
				and a.NUM_SOIN=  b.NUM_SOIN 
				and a.abrv_act = b.abrv_act);
				
				
				
	END		
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [insert_lig_bult_delete] on [LIG_BULT] 
for delete 
as
begin

DECLARE
	@v_indic      varchar(1),	  
	@v_nat_act    varchar(1),
	@v_plafonne   varchar(1),
	@v_taux_act   NUMERIC(5,2),
	@v_mtt_acte   NUMERIC(10,3),
	@v_plafond    NUMERIC(10,3),
	@w_plafond     NUMERIC(10,3),
	@w_sold_plaf   NUMERIC(10,3),
	@mut_mnt_net  NUMERIC(10,3),
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@vprix_act NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		



	@v_proratat varchar(1),
	--for cursor
	
	@v_ctr_duree varchar(1),

	@v_duree_act numeric(6),
	@v_imput_plaf varchar(1),

	
	
	@OLD_NUM_SOIN varchar(max),
	@OLD_cod_soc varchar(max),
	@OLD_mat_pers varchar(max),
	@OLD_num_fam int,
	@OLD_dat_soin date,
	@OLD_abrv_act varchar(max),
	@OLD_mnt_honor numeric(15,3),
	@OLD_mut_mnt_net numeric(15,3),
	@vsold_ectim numeric(15,3), 
	@OLD_mnt_net numeric(15,3),
	@OLD_NUM_LIG numeric (6),
	@Mat_Pers_Conj varchar(max),
	@Cod_Soc_Conj  varchar(max),
	@v_proratat_conj varchar(1),
	@vsold_ectim_conj numeric(15,3),
	
	
	---- for conjoint---------------
	
	@OLD_MntNetConj  numeric(15,3),
	@numfamC int
	Print N'No erreur found';
	
		begin 
		
		
			
			
			select @OLD_NUM_SOIN = NUM_SOIN from inserted i ;
			
			select @OLD_cod_soc = cod_soc from deleted i ;
			select @OLD_mat_pers = mat_pers from deleted i ;
			select @OLD_num_fam = NUM_FAM from deleted i ;
			select @OLD_dat_soin = DAT_SOIN from deleted i ;
			select @OLD_abrv_act = abrv_act from deleted i ;
			select @OLD_mnt_honor = isnull(mnt_honor,0) from deleted i ;
			select @OLD_mnt_net = isnull(mnt_net,0) from deleted i ;
			select @v_imput_plaf = isnull(IMPUT_PLAF,'O') from deleted i ;
			select @OLD_NUM_LIG = NUM_LIG from deleted i ;
			select @OLD_MntNetConj = ISNULL(mnt_net_conj,0) from deleted i;            --get mnt_net_conj
   
   	if(@OLD_num_fam = 0 )
			begin
			set @numfamC = 99
			end
			else
			begin
			if(@OLD_num_fam = 99)
			begin 
			set @numfamC = 0
			end
			else
			begin
			set @numfamC = @OLD_num_fam
			end
			end
   --- select matConj && Cod_soc_conj--------------------------------------------------------------------
    select @Mat_Pers_Conj=f.MAT_PERS_CONJ,@Cod_Soc_Conj=f.cod_soc_conj from FAMILLE f 
    where f.MAT_PERS=@OLD_mat_pers
     and f.COD_SOC=@OLD_cod_soc 
     and f.NUM_FAM='99' 
     and f.PARENTE='C'
     
   ------------------------------------------------------------------------------------------------------
			
			
			
		select
			@v_proratat =ISNULL(proratat,'N')
			from bareme_remb
			where COD_FIL = (select cod_fil 
			from pers_affil where cod_soc = @OLD_cod_soc 
			and mat_pers = @OLD_mat_pers 
			and courant = 'O')
			and COD_ASSUR = (select cod_assur 
			from personnel where cod_soc = @OLD_cod_soc 
			and mat_pers = @OLD_mat_pers)
			and ABRV_ACT = @OLD_abrv_act;		
			
		
				---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
/*TODO*/		
			
				if @v_imput_plaf = 'O' 
				begin
				
				----update plafond_cnam
				select @vsold_ectim=isnull(SOLD_ASSUR_ESTIM,0)  from plafond_assur
				where cod_soc = @OLD_cod_soc
				and num_fam=@OLD_num_fam
				and mat_pers = @OLD_mat_pers 
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
				where cod_soc = @OLD_cod_soc
				and mat_pers = @OLD_mat_pers
				and num_fam=@OLD_num_fam);
				
				
				--update plafond assurance Conj ------------------------------------------------------------------------------
				select @vsold_ectim_conj=isnull(SOLD_ASSUR_ESTIM,0)  from plafond_assur
				where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj 
				and num_fam=@numfamC
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
				where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj
				and num_fam=@numfamC);
				
				print N'aaaaaaaaaa'+cast(Isnull(@vsold_ectim_conj,0) as varchar)
				set @vsold_ectim_conj = @vsold_ectim_conj - isnull(@OLD_MntNetConj,0) ;
				if @vsold_ectim_conj < 0 
				begin
					set @vsold_ectim_conj = 0;
				end 				
				update plafond_assur set SOLD_ASSUR_ESTIM = @vsold_ectim_conj
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj 
					and num_fam=@numfamC
					--and annee_assur  = to_number(to_char(@OLD_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
					where cod_soc = @Cod_Soc_Conj
					and mat_pers = @Mat_Pers_Conj
					and num_fam=@numfamC);
					
				-------------------------------------------------------------------------------------------------
				
				Print N'@@@OLD_mnt_net' + cast ( @OLD_mnt_net as varchar );
				Print N'@@@OLD_mnt_net' + cast ( @OLD_mnt_net as varchar );
				set @vsold_ectim = @vsold_ectim - isnull(@OLD_mnt_net,0) ;
				if @vsold_ectim < 0 
				begin
					set @vsold_ectim = 0;
				end 
			
				
					update plafond_assur set SOLD_ASSUR_ESTIM = @vsold_ectim
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers 
					and num_fam = @OLD_num_fam 
					--and annee_assur  = to_number(to_char(@OLD_dat_soin,'yyyy'));
					and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers
					and num_fam = @OLD_num_fam );
					
					
					
					
					
					
						if(@v_proratat = 'O')
			begin
			
				select @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM,0) 
				from plafond_assur_detail
				where cod_soc = @OLD_cod_soc and mat_pers = @OLD_mat_pers and num_fam = @OLD_num_fam 
				and ABRV_ACT = @OLD_abrv_act and COD_FIL = (select cod_fil 
				from pers_affil where cod_soc = @OLD_cod_soc 
				and mat_pers = @OLD_mat_pers 
				and courant = 'O')
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @OLD_cod_soc
				and mat_pers = @OLD_mat_pers and num_fam = @OLD_num_fam); 
			
				set @vsold_ectim = @vsold_ectim - isnull(@OLD_mnt_net,0) ;
				if @vsold_ectim < 0 
				begin
					set @vsold_ectim = 0;
				end 
				update plafond_assur_detail set SOLD_ASSUR_ESTIM =@vsold_ectim
				from plafond_assur_detail
				where cod_soc = @OLD_cod_soc
				and mat_pers = @OLD_mat_pers and num_fam = @OLD_num_fam and ABRV_ACT = @OLD_abrv_act
				--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
				where cod_soc = @OLD_cod_soc
				and mat_pers = @OLD_mat_pers and num_fam = @OLD_num_fam)
				
				
				
				select @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM,0) 
				from plafond_assur_detail
				where cod_soc = @Cod_Soc_Conj and mat_pers = @Mat_Pers_Conj and num_fam = @numfamC 
				and ABRV_ACT = @OLD_abrv_act and COD_FIL = (select cod_fil 
				from pers_affil where cod_soc = @Cod_Soc_Conj 
				and mat_pers = @Mat_Pers_Conj 
				and courant = 'O')
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj and num_fam = @numfamC); 
			
				set @vsold_ectim = @vsold_ectim - isnull(@OLD_MntNetConj,0) ;
				if @vsold_ectim < 0 
				begin
					set @vsold_ectim = 0;
				end 
				update plafond_assur_detail set SOLD_ASSUR_ESTIM =@vsold_ectim
				from plafond_assur_detail
				where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj and num_fam = @numfamC and ABRV_ACT = @OLD_abrv_act
				--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur
				where cod_soc = @Cod_Soc_Conj
				and mat_pers = @Mat_Pers_Conj and num_fam = @numfamC)
				
			
			end	
				end ;
				
			
				
				
	END		
END
GO

IF (SELECT COUNT(*) FROM sys.triggers WHERE name IN (N'insert_lig_bult_insert', N'insert_lig_bult_update', N'insert_lig_bult_delete') AND is_disabled = 0) = 3
   AND NOT EXISTS (SELECT 1 FROM sys.triggers WHERE name = N'trg_lig_bult')
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;
GO
