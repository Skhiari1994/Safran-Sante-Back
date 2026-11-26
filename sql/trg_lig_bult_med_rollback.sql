USE [ramidb]
GO

/*
Retour arriere de trg_lig_bult_med.sql : supprime le trigger unique et recree les 3 triggers d'origine
(insert_med_insert, insert_med_update, insert_med_delete), tels qu'exportes le 20/11/2026.
*/

BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.trg_lig_bult_med', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_lig_bult_med;
IF OBJECT_ID(N'dbo.insert_med_insert', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_med_insert;
IF OBJECT_ID(N'dbo.insert_med_update', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_med_update;
IF OBJECT_ID(N'dbo.insert_med_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_med_delete;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [insert_med_insert] on [LIG_BULT_MED] 
for insert 
as
begin

DECLARE
	@date_affil_assu date,
	@v_proratat varchar(1),
	@v_indic      varchar(1),	  
	@v_nat_act    varchar(1),
	@v_plafonne   varchar(1),
	@v_taux_act   NUMERIC(5,2),
	@v_mtt_acte   NUMERIC(10,3),
	@v_plafond    NUMERIC(10,3),
	@w_plafond     NUMERIC(10,3),
	@w_sold_plaf   NUMERIC(10,3),
	@v_ctr_duree varchar(1),
	@v_duree_act numeric(6),
	@v_imput_plaf varchar(1),

	
	
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@wval_prix varchar(1),
	@wprix NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		
	@mut_mnt_net  NUMERIC(10,3),
	
	--for cursor
	@NEW_cod_soc varchar(max),
	@NEW_mat_pers varchar(max),
	@NEW_NUM_FAM  int,
	@NEW_dat_soin   DATE,
	@NEW_ABRV_ACT varchar(4),
	@NEW_indice numeric(6),
	@NEW_mnt_honor numeric(15,3),
	@NEW_mnt_net numeric(15,3),
	@NEW_NUM_SOIN varchar(max),

	@NEW_dat_act DATE,

	@NEW_NUM_LIG numeric(6),
	@NEW_anc_mnt_honor numeric(15,3),
	
	@NEW_mdc_prix numeric(15,3),
	@NEW_med_prix numeric(15,3),
	@NEW_prix_remb numeric(15,3),
	
	@w_plafond_detail     NUMERIC(10,3),
	@w_sold_plaf_detail   NUMERIC(10,3),
	@vsold_ectim numeric(15,3),
	----for conjoint-------------------------
	@mnt_net_conj  numeric(15,3),
	@lig_mntNet_calcul  numeric(15,3),
	@lig_sauv_calcul numeric(15,3),
	@bult_cum_calcul  numeric(15,3),
	@COD_FIL_CONJ varchar (max),
	@COD_ASSUR_CONJ varchar (max)
	-----------------------------------------
	
	
		begin 
		
		select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_NUM_FAM = NUM_FAM from inserted i ;
				select @NEW_NUM_SOIN = NUM_SOIN from inserted i;
			select @NEW_dat_soin = DAT_SOIN from inserted i ;
			
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @NEW_NUM_LIG = NUM_LIG from inserted i ;
			select @NEW_indice = indice from inserted i ;
			
			select @NEW_mnt_honor = mnt_honor from inserted i ;
			select @NEW_mnt_net = mnt_net from inserted i ;
			
		
			select @NEW_dat_act = dat_act from inserted i;
			select @NEW_mdc_prix = mdc_prix from inserted i;
			select @NEW_med_prix = med_prix from inserted i;
			select @NEW_prix_remb = prix_remb from inserted i;
			
		
			
			
			
			
			select @w_plafond = ISNULL(PLAFOND,0),@w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM,0) 
            from plafond_assur
            where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers  and num_fam = @NEW_NUM_FAM
			and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers  and num_fam = @NEW_NUM_FAM);    
			
			
			
			
	
-------------------------------------------------
			
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
			
			
			   	
			if(@v_proratat = 'O')
			begin
				select @w_plafond_detail = ISNULL(PLAFOND,0),@w_sold_plaf_detail = ISNULL(SOLD_ASSUR_ESTIM,0) 
				from plafond_assur_detail
				where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam 
				and ABRV_ACT = @NEW_ABRV_ACT and COD_FIL = (select cod_fil 
				from pers_affil where cod_soc = @NEW_cod_soc 
				and mat_pers = @NEW_mat_pers 
				and courant = 'O')
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers); 
				
				if @w_plafond_detail < @w_plafond
					set @w_plafond =@w_plafond_detail;
				if @w_sold_plaf_detail < @w_sold_plaf
					set @w_sold_plaf =@w_sold_plaf_detail;
				
			end
			-------------------------------------------------------------------------------------------------------
			select @wval_prix=val_prix  from ref_fill_act
			where cod_fil = 
			(select cod_fil from pers_affil where cod_soc = @NEW_cod_soc 
			and mat_pers = @NEW_mat_pers and courant = 'O')
			and abrv_act = @NEW_ABRV_ACT;
			
			if @wval_prix = 'P' 
			begin
				set @wprix = isnull(@NEW_mdc_prix,0) ;
			end 
			else begin
				if @wval_prix = 'G' 
				begin
					set @wprix = isnull(@NEW_med_prix,0) ;
				end
				else begin
					set @wprix = isnull(@NEW_prix_remb,0) ;
				end
			end 
            --to_number(to_char(@datsoin ,'yyyy'));
		
			print N' @wprix ' + cast ( @wprix  as varchar  );
			if @v_indic = 'O' 
			begin
				---------------------------calcul du net par rapport au contrat
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net = cast(@NEW_indice as float) * cast(@v_mtt_acte as float);
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						
					END
					else begin
						if @v_mnt_net > @NEW_mnt_honor 
						begin 
							set @mut_mnt_net =  @NEW_mnt_honor ;
						end
						else begin  
							set @mut_mnt_net = @v_mnt_net;
						END
					end
				END
				else begin -----@v_nat_act = 'T'
					SET @v_mnt_net = cast(@NEW_indice as float) * 
					cast(@wprix as float) * (cast(@v_taux_act as float)/100);   
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						
					END
				END
            END    
         ---------------------------------------------------    

			else begin
		   ---------------------------calcul du net par rapport au contrat    
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net =  @NEW_mnt_honor ;
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						
					END
					else begin
						
						if @v_mnt_net > @v_mtt_acte
						begin 
							set @mut_mnt_net = @v_mtt_acte ;
						END
						else begin
							set @mut_mnt_net = @v_mnt_net ;
						end
					END
				END
				else begin -----@v_nat_act = 'T'
					SET @v_mnt_net =  cast(@wprix as float) * 
					(cast(@v_taux_act as float)/100);   
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						
					END
				END   
					-------------------------------------------------------------       
			END
		end
		
		print N'  ' + cast ( @mut_mnt_net  as varchar  );
		print N' @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
		print N' @@NEW_mnt_net ' + cast ( @NEW_mnt_net  as varchar  );
		print N' @@NEW_mnt_honor ' + cast ( @NEW_mnt_honor  as varchar  );
		if  @mut_mnt_net + @NEW_mnt_net <> @NEW_mnt_honor 
		begin
			set @mut_mnt_net = @NEW_mnt_honor -  @NEW_mnt_net;
		end  
		
		
		print N' @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
					------------comparaison avec le plafond de l'assurance
			-------------------------------------------------------
			if @mut_mnt_net + @w_sold_plaf   > @w_plafond 
            BEGIN 
				SET @mut_mnt_net = @w_plafond - @w_sold_plaf ;
			end 
				print N' @w_sold_plaf @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );	
			-----------plafond acte
			begin try
			select @var=isnull(mnt_net,0) from lig_bult
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers
			and num_fam  = @NEW_num_fam 
			and NUM_SOIN= @NEW_NUM_SOIN
			and dat_soin = @NEW_dat_soin 
			and abrv_act = @NEW_abrv_act ;
			end try
			begin catch
			--exception when others then null;
			end catch
			if @v_plafonne = 'O' and isnull(@var,0) + @mut_mnt_net >= @v_plafond
			begin
			set @mut_mnt_net = @v_plafond - isnull(@var,0);
			end         
			-----préparation pour lig_bult
			select @x =count(*) from lig_bult
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers
			and num_fam  = @NEW_num_fam 
			and NUM_SOIN= @NEW_NUM_SOIN
			and dat_soin = @NEW_dat_soin 
			and abrv_act = @NEW_abrv_act ;
			
			
			-----clacul mntconjoint-------------------------------------------------
			
			select @COD_FIL_CONJ =COD_FIL from PERS_AFFIL where MAT_PERS = (select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C') 
			and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')

			select @COD_ASSUR_CONJ=COD_ASSUR from PERSONNEl where MAT_PERS =(select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
			and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
			SET @mnt_net_conj = @NEW_mnt_honor - @NEW_mnt_net - @mut_mnt_net
				
				EXEC    calcul_mntNetConj
		            @soc = @NEW_cod_soc,
		            @mat = @NEW_mat_pers,
					@cod_fil ='03',
					@cod_assur = '01',
					@numfam = @NEW_NUM_FAM,
					@datsoin = @NEW_dat_soin,
					@sauv_net = 0,
					@cumul_net = 0,
					@ACT_vmnt_honor = @mnt_net_conj,
					@ACT_indice = @NEW_indice,
					@ACT_ABRV_ACT = @NEW_ABRV_ACT,
					
					@lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
					@lig_sauv = @lig_sauv_calcul OUTPUT,
					@bult_cum = @bult_cum_calcul OUTPUT
				------------------------------------------------------------------------
				
				
			if @x = 0 
			begin
				select @v_NUM_LIG=isnull(max(isnull(NUM_LIG,0)),0) + 1  
				from lig_bult
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and NUM_FAM = @NEW_NUM_FAM
				and NUM_SOIN= @NEW_NUM_SOIN
				and DAT_SOIN = @NEW_DAT_SOIN;
				print N' @@NEW_mnt_honor ' + cast ( @NEW_mnt_honor  as varchar  );
				print N' @@@mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
				----insertion lig_bult
				begin try
					insert into lig_bult(cod_soc,mat_pers,num_fam,dat_soin,abrv_act,num_lig,
					mnt_honor,mnt_net,dat_act,indice,NUM_SOIN ,MNT_NET_CONJ)
					values(@NEW_cod_soc,@NEW_mat_pers,@NEW_num_fam,@NEW_dat_soin,@NEW_abrv_act,
					@v_NUM_LIG,@NEW_mnt_honor,@mut_mnt_net,@NEW_dat_act,@NEW_indice,@NEW_NUM_SOIN,@lig_mntNet_calcul);
				end try
				begin catch
					--exception when others then raise_application_error(-200001,'hhhhh');
				end catch
			end
			else begin
			------update lig_bult dans le cas de l'existance
			print N' @update @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );	
				update lig_bult set mnt_honor = isnull(mnt_honor,0) + @NEW_mnt_honor,
				mnt_net = isnull(mnt_net,0) + @mut_mnt_net,indice = indice +@NEW_indice,
				MNT_NET_CONJ = ISNULL(MNT_NET_CONJ,0)+@lig_mntNet_calcul,DAT_ACT = @NEW_dat_act
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_num_fam 
				and NUM_SOIN= @NEW_NUM_SOIN
				and dat_soin = @NEW_dat_soin 
				and abrv_act = @NEW_abrv_act ;
			end ;
			---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
			PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
			disable trigger lig_bult_med.insert_med_update on lig_bult_med;
			
			update lig_bult_med   set mut_mnt_net=@mut_mnt_net,mnt_net_conj=@lig_mntNet_calcul
			from lig_bult_med a
			join inserted b on (
			 a.cod_soc =  b.cod_soc
			and a.mat_pers = b.mat_pers
			and a.num_fam  = b.num_fam
			and a.dat_soin = b.dat_soin 
			and a.NUM_SOIN=  b.NUM_SOIN 
			and a.COD_MED = b.COD_MED
			and a.DAT_ACT = b.DAT_ACT
			);
			enable trigger lig_bult_med.insert_med_update on lig_bult_med;
			PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
			
			-------update plafon_cnam
			----------------------------------------------------------
			update plafond_cnam set sold_cnam_estim = isnull(sold_cnam_estim,0) + isnull(@NEW_mnt_net,0)
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers 
			and annee_cnam  = (select max(annee_cnam) from plafond_cnam
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers);
			

END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [insert_med_update] on [LIG_BULT_MED] 
for update 
as
begin

DECLARE
	@date_affil_assu date,
	@v_proratat varchar(1),
	@v_indic      varchar(1),	  
	@v_nat_act    varchar(1),
	@v_plafonne   varchar(1),
	@v_taux_act   NUMERIC(5,2),
	@v_mtt_acte   NUMERIC(10,3),
	@v_plafond    NUMERIC(10,3),
	@w_plafond     NUMERIC(10,3),
	@w_sold_plaf   NUMERIC(10,3),
	@v_ctr_duree varchar(1),
	@v_duree_act numeric(6),
	@v_imput_plaf varchar(1),

	
	
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@wval_prix varchar(1),
	@wprix NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		
	@mut_mnt_net  NUMERIC(10,3),
	
	--for cursor
	@NEW_cod_soc varchar(max),
	@NEW_mat_pers varchar(max),
	@NEW_NUM_FAM  int,
	@NEW_dat_soin   DATE,
	@NEW_ABRV_ACT varchar(4),
	@NEW_indice numeric(6),
	@NEW_mnt_honor numeric(15,3),
	@NEW_mnt_net numeric(15,3),
	
	@NEW_mnt_remb  varchar(max),
	@NEW_dat_act date,
	@NEW_MUT_MNT_NET numeric(15,3),
	@NEW_NUM_LIG numeric(6),
	@NEW_anc_mnt_honor numeric(15,3),
	
	@NEW_mdc_prix numeric(15,3),
	@NEW_med_prix numeric(15,3),
	@NEW_prix_remb numeric(15,3),
	@NEW_NUM_SOIN varchar(max),
	
	
	@w_plafond_detail     NUMERIC(10,3),
	@w_sold_plaf_detail   NUMERIC(10,3),
	
	
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
	@OLD_indice numeric(6),
	
	----for conjoint------------
	@mnt_net_conj_update numeric(15,3),
	@lig_mntNet_calcul numeric(15,3),
	@lig_sauv_calcul numeric(15,3),
	@bult_cum_calcul numeric(15,3),
	@OLD_mnt_conj numeric(15,3),
	@COD_FIL_CONJ varchar(max),
	@COD_ASSUR_CONJ varchar(max)
	----------------------------
	
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
			select @NEW_dat_act = dat_act from inserted i;
			select @NEW_mdc_prix = mdc_prix from inserted i;
			select @NEW_med_prix = med_prix from inserted i;
			select @NEW_prix_remb = prix_remb from inserted i;
			
			select @NEW_NUM_SOIN = NUM_SOIN from deleted i ;
			
			select @OLD_NUM_SOIN = NUM_SOIN from inserted i ;
			select @OLD_cod_soc = cod_soc from deleted i ;
			select @OLD_mat_pers = mat_pers from deleted i ;
			select @OLD_num_fam = NUM_FAM from deleted i ;
			select @OLD_dat_soin = DAT_SOIN from deleted i ;
			select @OLD_abrv_act = abrv_act from deleted i ;
			select @OLD_mnt_honor = mnt_honor from deleted i ;
			select @OLD_mut_mnt_net = mut_mnt_net from deleted i ;
			select @OLD_indice = indice from deleted i ;
			select @OLD_mnt_net = mnt_net from deleted i ;
			
			
			select @w_plafond = ISNULL(PLAFOND,0),@w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM,0) 
            from plafond_assur
            where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers and num_fam = @NEW_NUM_FAM
			and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers  and num_fam = @NEW_NUM_FAM);    
			
			
			
			
			
	
-------------------------------------------------
			
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
			
			
			   	
			if(@v_proratat = 'O')
			begin
				select @w_plafond_detail = ISNULL(PLAFOND,0),@w_sold_plaf_detail = ISNULL(SOLD_ASSUR_ESTIM,0) 
				from plafond_assur_detail
				where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam 
				and ABRV_ACT = @NEW_ABRV_ACT and COD_FIL = (select cod_fil 
				from pers_affil where cod_soc = @NEW_cod_soc 
				and mat_pers = @NEW_mat_pers 
				and courant = 'O')
				and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers); 
				
				if @w_plafond_detail < @w_plafond
					set @w_plafond =@w_plafond_detail;
				if @w_sold_plaf_detail < @w_sold_plaf
					set @w_sold_plaf =@w_sold_plaf_detail;
				
			end
			
			-------------------------------------------------------------------------------------------------------
			select @wval_prix=val_prix  from ref_fill_act
			where cod_fil = 
			(select cod_fil from pers_affil where cod_soc = @NEW_cod_soc 
			and mat_pers = @NEW_mat_pers and courant = 'O')
			and abrv_act = @NEW_ABRV_ACT;
			if @wval_prix = 'P' 
			begin
				set @wprix = isnull(@NEW_mdc_prix,0) ;
			end 
			else begin
				if @wval_prix = 'G' 
				begin
					set @wprix = isnull(@NEW_med_prix,0) ;
				end
				else begin
					set @wprix = isnull(@NEW_prix_remb,0) ;
				end
			end 
            --to_number(to_char(@datsoin ,'yyyy'));
			
			 
				print N'15 @@NEW_indice ' + cast ( @NEW_indice  as varchar  );
			Print N' 11  @@@@OLD_indice' + cast( @OLD_indice as varchar ); 
			Print N' 11  @@@@wprix' + cast( @wprix as varchar ); 
			
			if @v_indic = 'O' 
			begin
				---------------------------calcul du net par rapport au contrat
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net = cast(@NEW_indice as float) * cast(@v_mtt_acte as float);
					print N' 11@mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						print N' 12 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						
					END
					else begin
						if @v_mnt_net > @NEW_mnt_honor 
						begin 
							set @mut_mnt_net =  @NEW_mnt_honor ;
							print N' 13 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						end
						else begin  
							set @mut_mnt_net = @v_mnt_net;
							print N' 14@mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						END
					end
				END
				else begin -----@v_nat_act = 'T'
					SET @v_mnt_net = cast(@NEW_indice as float) * 
					cast(@wprix as float) * (cast(@v_taux_act as float)/100);   
					print N'15 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
					print N'15 @@NEW_indice ' + cast ( @NEW_indice  as varchar  );
					print N'15 @@@v_taux_act ' + cast ( @v_taux_act  as varchar  );
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						print N'16 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						print N' 17 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						
					END
				END
            END    
         ---------------------------------------------------    

			else begin
		   ---------------------------calcul du net par rapport au contrat    
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net =  @NEW_mnt_honor ;
					print N'7 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						print N'6 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						
					END
					else begin
						
						if @v_mnt_net > @v_mtt_acte
						begin 
							set @mut_mnt_net = @v_mtt_acte ;
							print N'5 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						END
						else begin
							set @mut_mnt_net = @v_mnt_net ;
							print N' 4 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						end
					END
				END
				else begin -----@v_nat_act = 'T'
					SET @v_mnt_net =  cast(@wprix as float) * 
					(cast(@v_taux_act as float)/100);   
					print N'3 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						print N' 2@mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						print N'1 @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
						
					END
				END   
					-------------------------------------------------------------       
			END
		end
		
			print N'  ' + cast ( @mut_mnt_net  as varchar  );
		print N' @mut_mnt_net ' + cast ( @mut_mnt_net  as varchar  );
		print N' @@NEW_mnt_net ' + cast ( @NEW_mnt_net  as varchar  );
		print N' @@NEW_mnt_honor ' + cast ( @NEW_mnt_honor  as varchar  );
		if  @mut_mnt_net + @NEW_mnt_net <> @NEW_mnt_honor 
		begin
			set @mut_mnt_net = @NEW_mnt_honor -  @NEW_mnt_net;
		end  
		
		
		
		
					------------comparaison avec le plafond de l'assurance
			-------------------------------------------------------
			if @mut_mnt_net + @w_sold_plaf   > @w_plafond 
            BEGIN 
				SET @mut_mnt_net = @w_plafond - @w_sold_plaf ;
			end 
				
			-----------plafond acte
			begin try
			select @var=isnull(mnt_net,0) from lig_bult
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers
			and num_fam  = @NEW_num_fam 
			and dat_soin = @NEW_dat_soin 
			and NUM_SOIN= @NEW_NUM_SOIN
			and abrv_act = @NEW_abrv_act ;
			end try
			begin catch
			--exception when others then null;
			end catch
			if @v_plafonne = 'O' and isnull(@var,0) + @mut_mnt_net >= @v_plafond
			begin
			set @mut_mnt_net = @v_plafond - isnull(@var,0);
			end         
			
			
			
			
			
			---------------clacul mnt net conj -------------------------------------------
			
					
			select @COD_FIL_CONJ =COD_FIL from PERS_AFFIL where MAT_PERS = (select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C') 
and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')

select @COD_ASSUR_CONJ=COD_ASSUR from PERSONNEl where MAT_PERS =(select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
 and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
			
			SET @mnt_net_conj_update = @NEW_mnt_honor - @NEW_mnt_net - @mut_mnt_net 
			
			EXEC    [dbo].[calcul_mntNetConj]
		            @soc = @NEW_cod_soc,
		            @mat = @NEW_mat_pers,
					@cod_fil =@COD_FIL_CONJ,
					@cod_assur = @COD_ASSUR_CONJ,
					@numfam = @NEW_NUM_FAM,
					@datsoin = @NEW_dat_soin,
					@sauv_net = 0,
					@cumul_net = 0,
					@ACT_vmnt_honor = @mnt_net_conj_update,
					@ACT_indice = @NEW_indice,
					@ACT_ABRV_ACT = @NEW_ABRV_ACT,
					
					@lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
					@lig_sauv = @lig_sauv_calcul OUTPUT,
					@bult_cum = @bult_cum_calcul OUTPUT
					
					select @OLD_mnt_conj=MNT_NET_CONJ from LIG_BULT where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers
					and num_fam  = @OLD_num_fam 
					and dat_soin = @OLD_dat_soin 
					and NUM_SOIN= @NEW_NUM_SOIN
					and abrv_act = @OLD_abrv_act 
					and DAT_ACT = @NEW_dat_act
			------------------------------------------------------------------------------
			
			
			
				
					------update lig_bult dans le cas de l'existance

					update lig_bult set mnt_honor = ISNULL(mnt_honor,0) + @NEW_mnt_honor - @OLD_mnt_honor,
					mnt_net = ISNULL(mnt_net,0) + @mut_mnt_net - @OLD_mut_mnt_net
					,indice = indice - @OLD_indice + @NEW_indice,
					MNT_NET_CONJ = ISNULL(MNT_NET_CONJ,0)+ @lig_mntNet_calcul - @OLD_mnt_conj,DAT_ACT = @NEW_dat_act
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers
					and NUM_SOIN= @NEW_NUM_SOIN
					and num_fam  = @OLD_num_fam 
					and dat_soin = @OLD_dat_soin 
					and abrv_act = @OLD_abrv_act ; 
				 
				---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
				
				update lig_bult_med  set mut_mnt_net=@mut_mnt_net,MNT_NET_CONJ=@lig_mntNet_calcul  from lig_bult_med a
				where a.cod_soc = @OLD_cod_soc
				and a.mat_pers = @OLD_mat_pers
				and a.num_fam  = @OLD_num_fam
				and a.dat_soin = @OLD_dat_soin 
				and a.NUM_SOIN= @NEW_NUM_SOIN
				and a.abrv_act = @OLD_abrv_act
				and a.DAT_ACT= @NEW_dat_act 
				 
				
				
				
/*TODO*/		set @NEW_mut_mnt_net = @mut_mnt_net;
				
			enable trigger lig_bult_med.insert_med_update on lig_bult_med;
			PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
					
					
					select @vsold_ectim=ISNULL(sold_cnam_estim,0)  from plafond_cnam
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers 
					and annee_cnam  = (select max(annee_cnam) from plafond_cnam
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers);
					set @vsold_ectim = @vsold_ectim - ISNULL(@OLD_mnt_net,0) + ISNULL(@NEW_mnt_net,0);
					if @vsold_ectim < 0 
					begin
						set @vsold_ectim = 0;
					end 
					update plafond_cnam set sold_cnam_estim = @vsold_ectim
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers 
					and ANNEE_CNAM  = YEAR(@OLD_dat_soin);
					
		
				
			

END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [insert_med_delete] on [LIG_BULT_MED] 
for delete 
as
begin

DECLARE

	@anc_net_mut NUMERIC(10,3),
   



	
	--for cursor

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
	@OLD_indice numeric(6),
	
	-----for conjoint---------------------
	@mnt_net_conj_update numeric(15,3),
    @lig_mntNet_calcul numeric(15,3),
	@lig_sauv_calcul numeric(15,3),
	@bult_cum_calcul numeric(15,3),
	@old_mnt_net_conj numeric(15,3)
	
	-----------------------------------------
	Print N'No erreur found';
	
		begin 
		
			
		
		select @OLD_mnt_net = mnt_net  from deleted i ;
			select @OLD_NUM_SOIN = NUM_SOIN  from deleted i ;
			select @OLD_cod_soc = cod_soc from deleted i ;
			select @OLD_mat_pers = mat_pers from deleted i ;
			select @OLD_num_fam = NUM_FAM from deleted i ;
			select @OLD_dat_soin = DAT_SOIN from deleted i ;
			select @OLD_abrv_act = abrv_act from deleted i ;
			select @OLD_mnt_honor = mnt_honor from deleted i ;
			select @OLD_mut_mnt_net = mut_mnt_net from deleted i ;
			select @OLD_indice = indice from deleted i ;
			
			select @old_mnt_net_conj = mnt_net_conj from deleted i;
			Print N' @OLD_mut_mnt_net  =>   '+ cast ( @OLD_mut_mnt_net as varchar )

			
			
			
			--------------
begin try
select @anc_net_mut = isnull(mnt_net,0)  from lig_bult
where cod_soc = @OLD_cod_soc
and mat_pers = @OLD_mat_pers
and num_fam  = @OLD_num_fam 
and NUM_SOIN= @OLD_NUM_SOIN
and dat_soin = @OLD_dat_soin 
and abrv_act = @OLD_abrv_act ;
Print N' @anc_net_mut    ';
Print N' @anc_net_mut  =>   '+ cast ( @anc_net_mut as varchar );
end try
begin catch
set @anc_net_mut = @OLD_mut_mnt_net;
Print N' catch @OLD_mut_mnt_net  =>   '+ cast ( @OLD_mut_mnt_net as varchar );
end catch
if @anc_net_mut = @OLD_mut_mnt_net
begin
delete lig_bult
where cod_soc = @OLD_cod_soc
and mat_pers = @OLD_mat_pers
and num_fam  = @OLD_num_fam 
and NUM_SOIN= @OLD_NUM_SOIN
and dat_soin = @OLD_dat_soin 
and abrv_act = @OLD_abrv_act 
Print N' @delte  => ';
end
else begin
update lig_bult set mnt_honor = isnull(mnt_honor,0) - @OLD_mnt_honor,
mnt_net = isnull(mnt_net,0) - @OLD_mut_mnt_net, indice = indice - @OLD_indice ,MNT_NET_CONJ = ISNULL(MNT_NET_CONJ,0) - ISNULL(@old_mnt_net_conj,0)
where cod_soc = @OLD_cod_soc
and mat_pers = @OLD_mat_pers
and num_fam  = @OLD_num_fam 
and NUM_SOIN= @OLD_NUM_SOIN
and dat_soin = @OLD_dat_soin 
and abrv_act = @OLD_abrv_act ;
Print N' @update   => ';
end 
-------update plafon_cnam
Print N' @@OLD_mnt_net  =>   '+ cast ( @OLD_mnt_net as varchar );
----------------------------------------------------------
select @vsold_ectim =isnull(sold_cnam_estim,0)   from plafond_cnam
  where cod_soc = @OLD_cod_soc
  and mat_pers = @OLD_mat_pers 
  and annee_cnam  = YEAR(@OLD_dat_soin)
  Print N' @@@@vsold_ectim  =>   '+ cast ( @vsold_ectim as varchar );
  set @vsold_ectim = @vsold_ectim - isnull(@OLD_mnt_net,0);
  if @vsold_ectim < 0 
  begin
  set @vsold_ectim = 0;
  end 
  Print N' @@@vsold_ectim  =>   '+ cast ( @vsold_ectim as varchar );
  update plafond_cnam set sold_cnam_estim = @vsold_ectim
  where cod_soc = @OLD_cod_soc
  and mat_pers = @OLD_mat_pers 
  and annee_cnam  = YEAR(@OLD_dat_soin)
				
			

END			
			
END
GO

IF (SELECT COUNT(*) FROM sys.triggers WHERE name IN (N'insert_med_insert', N'insert_med_update', N'insert_med_delete') AND is_disabled = 0) = 3
   AND NOT EXISTS (SELECT 1 FROM sys.triggers WHERE name = N'trg_lig_bult_med')
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;
GO
