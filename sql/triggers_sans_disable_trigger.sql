USE [ramidb]
GO

/*
Triggers LIG_BULT, LIG_BULT_MED, LIG_BULT_APP, LIG_BULT_ACT (LIG_BULT_VISIT est traitee par trg_lig_bult_visit.sql) : suppression des DISABLE / ENABLE TRIGGER.

Problème : les triggers *_insert exécutaient DISABLE TRIGGER / ENABLE TRIGGER sur la table (et insert_med_update un ENABLE TRIGGER).
Ces ordres demandent un verrou de schéma (Sch-M) sur la table, incompatible avec les INSERT/UPDATE d'une autre session
=> deadlocks (graphes relevés le 19/11/2026 dans system_health). De plus DISABLE TRIGGER vaut pour toute la base, pas pour la session.

Correctif : les calculs ne changent pas. Chaque trigger *_update commence par une garde
TRIGGER_NESTLEVEL(OBJECT_ID('dbo.<trigger *_insert>')) > 0 -> RETURN, qui reproduit l'effet voulu (ne pas recalculer
quand la mise à jour vient du trigger d'insertion) sans toucher au schéma.

Retour arrière : triggers_sans_disable_trigger_rollback.sql (définitions d'origine exportées avant modification).
*/

BEGIN TRANSACTION;
GO

-- ===== LIG_BULT_MED =====
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [insert_med_insert] on [LIG_BULT_MED] 
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
ALTER TRIGGER [insert_med_update] on [LIG_BULT_MED] 
for update 
as
begin

	-- Mise à jour faite par le trigger insert_med_insert lui-même (écriture de mut_mnt_net / totaux) : on ne recalcule pas.
	-- Remplace l'ancienne désactivation / réactivation du trigger, qui prenait un verrou de schéma (Sch-M) sur la table et provoquait des deadlocks.
	IF TRIGGER_NESTLEVEL(OBJECT_ID(N'dbo.insert_med_insert')) > 0
		RETURN;

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

-- ===== LIG_BULT_APP =====
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [insert_app_insert] on [LIG_BULT_APP] 
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
	@mut_mnt_net  NUMERIC(10,3),
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@vprix_act NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		

	@v_TAUX_ACT_CNAM   NUMERIC(5,2),
	@v_PLAF_ACTE_CNAM  NUMERIC(10,3),
	@w_sold_plaf_cnam NUMERIC(10,3),
	@v_plafond_cnam    NUMERIC(10,3),
	
	@w_plafond_detail     NUMERIC(10,3),
	@w_sold_plaf_detail   NUMERIC(10,3),

	
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
	
	
	---for conjoint----------
	@mnt_net_conj numeric(15,3),
	@lig_mntNet_calcul numeric(15,3),
	@lig_sauv_calcul numeric(15,3),
	@bult_cum_calcul numeric(15,3),
	@COD_FIL_CONJ varchar(max),
	@COD_ASSUR_CONJ varchar(max)
	-------------------------
	Print N'No erreur found';
	
		begin 
		
			select @NEW_NUM_LIG = NUM_LIG from inserted i ;
			select @NEW_indice = indice from inserted i ;
			select @NEW_mnt_honor = mnt_honor from inserted i ;
			select @NEW_mnt_net = mnt_net from inserted i ;
			select @NEW_MUT_MNT_NET = MUT_MNT_NET from inserted i ;
			select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_NUM_FAM = NUM_FAM from inserted i ;
			select @NEW_COD_APP = COD_APP from inserted i ;
			select @NEW_dat_soin = DAT_SOIN from inserted i ;
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @NEW_NUM_SOIN = NUM_SOIN from inserted i;
		
			select @NEW_dat_act = dat_act from inserted i;
			Print N'No erreur found'+ @NEW_ABRV_ACT;
			
			Print N'HELLO';
			
			
			select @w_plafond = ISNULL(PLAFOND,0),@w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM,0) 
            from plafond_ASSUR
            where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers
			and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers);      
            --to_number(to_char(@datsoin ,'yyyy'));
			
			
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
					SET @v_mnt_net = cast(@NEW_indice as float) * cast(@NEW_mnt_honor as float) * (cast(@v_taux_act as float)/100);   
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
					SET @v_mnt_net =  cast(@NEW_mnt_honor as float) * (cast(@v_taux_act as float)/100);   
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
			
				
			Print N' @w_sold_plaf = >'+ cast ( @w_sold_plaf as varchar );
			Print N' @w_plafond = >'+ cast ( @w_plafond as varchar );
			------------comparaison avec le plafond de l'assurance
			-------------------------------------------------------
			if @mut_mnt_net + @w_sold_plaf   > @w_plafond 
            BEGIN 
				SET @mut_mnt_net = @w_plafond - @w_sold_plaf ;
			end 
			Print N' BEFORE @mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
			--------------------
		if  @mut_mnt_net + @NEW_mnt_net > @NEW_mnt_honor 
		begin
			set @mut_mnt_net = @NEW_mnt_honor -  @NEW_mnt_net;
		end  
			Print N' AFTER @@mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
			
	
			
			-------------------------------------------
				if @v_ctr_duree = 'O' 
				begin
					select @x=SUM(isnull(mut_mnt_net,0))  from LIG_BULT_APP
					where COD_SOC = @NEW_cod_soc
					and MAT_PERS = @NEW_mat_pers
					and NUM_FAM  = @NEW_NUM_FAM
					and ABRV_ACT  = @NEW_ABRV_ACT
					and DAT_SOIN between DATEADD(MONTH,-@v_duree_act,CONVERT(date,@NEW_DAT_SOIN)) and @NEW_DAT_SOIN
					
						Print N' insertion @@@x = >'+ cast ( @x as varchar );
						Print N' insertion @@@@mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
						Print N' insertion @@@@v_plafond = >'+ cast ( @v_plafond as varchar );
						if(@mut_mnt_net + @x >@v_plafond)
						begin	
						set @mut_mnt_net = @v_plafond - @x;
						Print N' insertion @mut_mnt_net + @x >@v_plafond = >'+ cast ( @mut_mnt_net as varchar );
						end
						if ( @mut_mnt_net <0 )
						begin	
						set @mut_mnt_net = 0;
						Print N' insertion @mut_mnt_net <0 = >'+ cast ( @mut_mnt_net as varchar );
						end
					
				end 
				---------------------------------------------------,
				select @x=count(*) from lig_bult
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_num_fam 
				and dat_soin = @NEW_dat_soin 
				and NUM_SOIN= @NEW_NUM_SOIN
				and abrv_act = @NEW_abrv_act ;
				
				
				
				
				
				-----clacul mntconjoint-------------------------------------------------
				
				select @COD_FIL_CONJ =COD_FIL from PERS_AFFIL where MAT_PERS = (select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C') 
and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')

select @COD_ASSUR_CONJ=COD_ASSUR from PERSONNEl where MAT_PERS =(select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
 and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
				SET @mnt_net_conj = @NEW_mnt_honor - @NEW_mnt_net - @mut_mnt_net
				
				EXEC    [dbo].[calcul_mntNetConj]
		            @soc = @NEW_cod_soc,
		            @mat = @NEW_mat_pers,
					@cod_fil =@COD_FIL_CONJ,
					@cod_assur = @COD_ASSUR_CONJ,
					@numfam = @NEW_NUM_FAM,
					@datsoin = @NEW_dat_soin,
					@sauv_net = 0,
					@cumul_net = 0,
					@ACT_vmnt_honor = @mnt_net_conj,
					@ACT_indice = null,
					@ACT_ABRV_ACT = @NEW_ABRV_ACT,
					
					@lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
					@lig_sauv = @lig_sauv_calcul OUTPUT,
					@bult_cum = @bult_cum_calcul OUTPUT
				------------------------------------------------------------------------
				
				
				
				
				if @x = 0
				begin
					select @v_NUM_LIG=ISNULL(max(ISNULL(NUM_LIG,0)),0) + 1  
					from lig_bult
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers
					and NUM_FAM = @NEW_NUM_FAM
					and NUM_SOIN= @NEW_NUM_SOIN
					and DAT_SOIN = @NEW_DAT_SOIN;
				
				Print N' insertion @@mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
					----insertion lig_bult
					begin try
					insert into lig_bult(NUM_SOIN,cod_soc,mat_pers,num_fam,dat_soin,abrv_act,num_lig,mnt_honor,mnt_net,dat_act,MNT_NET_CONJ )
					values(@NEW_NUM_SOIN,@NEW_cod_soc,@NEW_mat_pers,@NEW_num_fam,@NEW_dat_soin,@NEW_abrv_act,@v_NUM_LIG,@NEW_mnt_honor,@mut_mnt_net,@NEW_dat_act,@lig_mntNet_calcul);
					end try
					begin catch
					--exception when others then raise_application_error(-200001,'hhhhh');
					end catch
				end
				else begin
					------update lig_bult dans le cas de l'existance

					update lig_bult set mnt_honor = ISNULL(mnt_honor,0) + @NEW_mnt_honor,mnt_net = ISNULL(mnt_net,0) + @mut_mnt_net,
					MNT_NET_CONJ = ISNULL(MNT_NET_CONJ,0)+@lig_mntNet_calcul,DAT_ACT=@NEW_dat_act
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers
					and num_fam  = @NEW_num_fam 
					and dat_soin = @NEW_dat_soin 
					and NUM_SOIN= @NEW_NUM_SOIN
					and abrv_act = @NEW_abrv_act ;
				end 
				
				---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
/*TODO*/		set @NEW_mut_mnt_net = @mut_mnt_net;
				PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
				update lig_bult_app   set mut_mnt_net=@mut_mnt_net,mnt_net_conj = @lig_mntNet_calcul
				from lig_bult_app a
				join inserted b on (
				 a.cod_soc =  b.cod_soc
				and a.mat_pers = b.mat_pers
				and a.num_fam  = b.num_fam
				and a.dat_soin = b.dat_soin 
				and a.NUM_SOIN=  b.NUM_SOIN 
				and a.COD_APP = b.COD_APP
				and a.DAT_ACT = b.DAT_ACT);
				
			
				
				/*if @v_imput_plaf = 'O' 
				begin
					update plafond_cnam set sold_cnam_estim = ISNULL(sold_cnam_estim,0) + ISNULL(@NEW_mnt_net,0)
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers 
					--and annee_assur  = to_number(to_char(@NEW_dat_soin,'yyyy'));
					and annee_cnam = (select max(annee_cnam) from plafond_cnam
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers);
					
				end */
	END		
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [insert_app_update] on [LIG_BULT_APP] 
for update 
as
begin

	-- Mise à jour faite par le trigger insert_app_insert lui-même (écriture de mut_mnt_net / totaux) : on ne recalcule pas.
	-- Remplace l'ancienne désactivation / réactivation du trigger, qui prenait un verrou de schéma (Sch-M) sur la table et provoquait des deadlocks.
	IF TRIGGER_NESTLEVEL(OBJECT_ID(N'dbo.insert_app_insert')) > 0
		RETURN;

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
	@mut_mnt_net  NUMERIC(10,3),
	@x             NUMERIC(10,3),
	@v_NUM_LIG     NUMERIC(10,3),
	@v_mnt_net  NUMERIC(10,3),
	@vprix_act NUMERIC(10,3),
	@anc_net_mut NUMERIC(10,3),
    @var NUMERIC(10,3),
	@vmut_mnt_net NUMERIC(10,3),		

	@v_TAUX_ACT_CNAM   NUMERIC(5,2),
	@v_PLAF_ACTE_CNAM  NUMERIC(10,3),
	@w_sold_plaf_cnam NUMERIC(10,3),
	@v_plafond_cnam    NUMERIC(10,3),
	@w_plafond_detail     NUMERIC(10,3),
	@w_sold_plaf_detail   NUMERIC(10,3),

	
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

	@NEW_mnt_remb  varchar(max),
	@NEW_dat_act varchar(max),
	@NEW_MUT_MNT_NET numeric(15,3),
	@NEW_NUM_LIG numeric(6),
	@NEW_anc_mnt_honor numeric(15,3),
	@NEW_COD_APP varchar(max),
	@NEW_NUM_SOIN varchar(max),
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
	-------for conjoint---------------------
	@mnt_net_conj_update numeric(15,3),
	@lig_mntNet_calcul numeric(15,3),
	@lig_sauv_calcul numeric(15,3),
	@bult_cum_calcul numeric(15,3),
	@OLD_mnt_conj  numeric(15,3),
	@COD_FIL_CONJ varchar(max),
	 @COD_ASSUR_CONJ varchar(max)
	
	----------------------------------------
	Print N'No erreur found';
	
		begin 
		
			select @NEW_NUM_LIG = NUM_LIG from inserted i ;
			select @NEW_indice = indice from inserted i ;
			select @NEW_mnt_honor = mnt_honor from inserted i ;
			select @NEW_mnt_net = mnt_net from inserted i ;
			select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_NUM_FAM = NUM_FAM from inserted i ;
			select @NEW_COD_APP = COD_APP from inserted i ;
			select @NEW_dat_soin = DAT_SOIN from inserted i ;
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @NEW_dat_act = dat_act from inserted i;
			select @NEW_mut_mnt_net = mut_mnt_net from deleted i ;
			
			select @NEW_NUM_SOIN = NUM_SOIN from deleted i ;
			
			select @OLD_NUM_SOIN = NUM_SOIN from inserted i ;
			select @OLD_cod_soc = cod_soc from deleted i ;
			select @OLD_mat_pers = mat_pers from deleted i ;
			select @OLD_num_fam = NUM_FAM from deleted i ;
			select @OLD_dat_soin = DAT_SOIN from deleted i ;
			select @OLD_abrv_act = abrv_act from deleted i ;
			select @OLD_mnt_honor = mnt_honor from deleted i ;
			select @OLD_mut_mnt_net = mut_mnt_net from deleted i ;
			select @OLD_mnt_net = mnt_net from deleted i ;
			Print N'No erreur found'+ @NEW_ABRV_ACT;
			
			
			select @w_plafond = ISNULL(PLAFOND,0),@w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM,0) 
            from plafond_assur
            where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers  and num_fam = @NEW_NUM_FAM
			and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers and num_fam = @NEW_NUM_FAM);    
			
			
			
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
				and mat_pers = @NEW_mat_pers  and num_fam = @NEW_NUM_FAM); 
				
				if @w_plafond_detail < @w_plafond
					set @w_plafond =@w_plafond_detail;
				if @w_sold_plaf_detail < @w_sold_plaf
					set @w_sold_plaf =@w_sold_plaf_detail;
				
			end
			
			
			
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
					cast(@NEW_mnt_honor as float) * (cast(@v_taux_act as float)/100);   
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
					SET @v_mnt_net =  cast(@NEW_mnt_honor as float) * 
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
			
					Print N' @w_sold_plaf = >'+ cast ( @w_sold_plaf as varchar );
			Print N' @w_plafond = >'+ cast ( @w_plafond as varchar );
			------------comparaison avec le plafond de l'assurance
			-------------------------------------------------------
			if @mut_mnt_net + @w_sold_plaf   > @w_plafond 
            BEGIN 
				SET @mut_mnt_net = @w_plafond - @w_sold_plaf ;
			end 
			Print N' BEFORE @mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
			--------------------
		if  @mut_mnt_net + @NEW_mnt_net > @NEW_mnt_honor 
		begin
			set @mut_mnt_net = @NEW_mnt_honor -  @NEW_mnt_net;
		end  
			Print N' AFTER @@mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
			
				
				
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
					@ACT_indice = null,
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

					update lig_bult 
					set mnt_honor = ISNULL(mnt_honor,0) + @NEW_mnt_honor - @OLD_mnt_honor,
					mnt_net = ISNULL(mnt_net,0) + @mut_mnt_net - @OLD_mut_mnt_net,
					MNT_NET_CONJ=ISNULL(MNT_NET_CONJ,0)+ @lig_mntNet_calcul - @OLD_mnt_conj,
					DAT_ACT = @NEW_dat_act
					where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers
					and num_fam  = @OLD_num_fam 
					and dat_soin = @OLD_dat_soin 
					and NUM_SOIN= @NEW_NUM_SOIN
					and abrv_act = @OLD_abrv_act ; 
				 
				---------insertion dans lig_bult_visit le champs mut_mnt_net pour pouvoir réutiliser la valeur
				
				update lig_bult_app  set mut_mnt_net=@mut_mnt_net,mnt_net_conj=@lig_mntNet_calcul
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_NUM_FAM
				and dat_soin = @NEW_dat_soin 
				and NUM_SOIN= @NEW_NUM_SOIN
				and abrv_act = @NEW_ABRV_ACT
				and DAT_ACT = @NEW_dat_act
				; 
				
				
				
/*TODO*/		set @NEW_mut_mnt_net = @mut_mnt_net;
				--------------------------------------------
				if @v_imput_plaf = 'O' 
				begin
					
					
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
					--and annee_assur  = to_number(to_char(@OLD_dat_soin,'yyyy'));
					and annee_cnam = (select max(annee_cnam) from plafond_cnam
					where cod_soc = @NEW_cod_soc
					and mat_pers = @NEW_mat_pers);
					
				end 
			
END
GO

-- ===== LIG_BULT_ACT =====
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [insert_act_insert] on [LIG_BULT_ACT] 
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
	@w_plafond_detail     NUMERIC(10,3),
	@w_sold_plaf_detail   NUMERIC(10,3),
	
	--for cursor
	@NEW_cod_soc varchar(max),
	@NEW_mat_pers varchar(max),
	@NEW_num_fam  int,
	@ACT_datsoin   DATE,
	@NEW_ABRV_ACT varchar(max),
	@ACT_cod_act varchar(max),
	@ACT_indice numeric(15,3),
	@ACT_mnt_honor numeric(15,3),
	@ACT_mnt_net numeric(15,3),
	@ACT_act_prix numeric(15,3),
	@ACT_vmnt_honor numeric(15,3),
	@ACT_mnt_remb  varchar(max),
	@NEW_NUM_SOIN varchar(max),
	@ACT_dat_act date,
	@ACT_MUT_MNT_NET numeric(15,3),
	@ACT_NUM_LIG numeric(6),
	@ACT_anc_mnt_honor numeric(15,3),
	
	--- for conjoint----------
	@mnt_net_conj  numeric(15,3),
	@lig_mntNet_calcul numeric(15,3),
	@lig_sauv_calcul numeric(15,3),
	@bult_cum_calcul numeric(15,3),
	@COD_FIL_CONJ varchar(max),
	@COD_ASSUR_CONJ varchar(max)
	--------------------------
	Print N'No erreur found';
	
		begin 
		
		
			select @ACT_mnt_honor = mnt_honor from inserted i ;
			select @ACT_NUM_LIG = NUM_LIG from inserted i ;
			select @ACT_indice = isnull(indice,1) from inserted i ;
			select @ACT_vmnt_honor = mnt_honor from inserted i ;
			select @ACT_mnt_net = mnt_net from inserted i ;
			select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_num_fam = NUM_FAM from inserted i ;
			select @ACT_cod_act = COD_ACT from inserted i ;
			select @ACT_datsoin = DAT_SOIN from inserted i ;
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @ACT_act_prix = act_prix from inserted i;
			select @NEW_NUM_SOIN = NUM_SOIN from inserted i;
			
			select @ACT_dat_act = dat_act from inserted i;
			Print N'No erreur found'+ @NEW_ABRV_ACT;
			
			Print N'HELLO';
			
			select @v_indic= ISNULL(a_indice,'N'),@v_nat_act =ISNULL(nat_act,'M'),
			@v_plafonne =ISNULL(plafonne,'N'),@v_taux_act =ISNULL(taux_act,0)
			,@v_mtt_acte = ISNULL(mtt_acte,0),@v_plafond = ISNULL(plafond,0)
			,@v_proratat=ISNULL(proratat,'N')
			from bareme_remb
			where COD_FIL = (select cod_fil from pers_affil where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers 
			and courant = 'O')
			and COD_ASSUR = (select cod_assur from personnel where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers)
			and ABRV_ACT = @NEW_ABRV_ACT;
			
			select @w_plafond = ISNULL(PLAFOND,0),@w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM,0) 
            from plafond_assur
            where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam
			and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam);      
            --to_number(to_char(@datsoin ,'yyyy'));
			
			 
			
				------------------------------------
			   	
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
			----------------------
		
		
			Print N' 11  @@@v_indic' + cast( @v_indic as varchar ); 
			
			if @v_indic = 'O' 
			begin
				---------------------------calcul du net par rapport au contrat
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net = cast(@ACT_indice as float) * cast(@v_mtt_acte as float);
					Print N' @v_mnt_net' + cast( @v_mnt_net as varchar ); 
					Print N' @v_mnt_net' + cast( @ACT_indice as varchar ); 
					Print N' @v_mnt_net' + cast( @v_mtt_acte as varchar ); 
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						Print N' 1 '
					END
					else begin
						if @v_mnt_net > @ACT_vmnt_honor 
						begin 
							set @mut_mnt_net =  @ACT_vmnt_honor ;
							Print N' 2 '
						end
						else begin  
							set @mut_mnt_net = @v_mnt_net;
							Print N' 3 '
						END
					end
				END
				else begin -----@v_nat_act = 'T'
					SET @v_mnt_net = cast(@ACT_indice as float) * cast(@ACT_vmnt_honor as float) * (cast(@v_taux_act as float)/100);   
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						Print N' 4 '
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						Print N' 5 '
						
					END
				END
            END    
         ---------------------------------------------------    

			else begin
		   ---------------------------calcul du net par rapport au contrat    
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net =  @ACT_vmnt_honor ;
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						Print N' 6 '
						
					END
					else begin
						
						if @v_mnt_net > @v_mtt_acte
						begin 
							set @mut_mnt_net = @v_mtt_acte ;
							Print N' 7 '
						END
						else begin
							set @mut_mnt_net = @v_mnt_net ;
							Print N' 8 '
						end
					END
				END
				else begin -----@v_nat_act = 'T'
					SET @v_mnt_net =  cast(@ACT_act_prix as float) * (cast(@v_taux_act as float)/100);   
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						set @mut_mnt_net = @v_plafond;
						Print N' 9 '
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						Print N' 10 '
						
					END
				END   
					-------------------------------------------------------------       
			END
			
			
			Print N' @mut_mnt_net '+cast ( @mut_mnt_net as varchar );
			Print N' @@v_plafond '+cast ( @v_plafond as varchar );
			Print N' @@@w_plafond '+cast ( @w_plafond as varchar );
			Print N' @@@@ACT_mnt_net '+cast ( @ACT_mnt_net as varchar );
			------------comparaison avec le plafond de l'assurance
			-------------------------------------------------------
			if @mut_mnt_net + @w_sold_plaf   > @w_plafond 
            BEGIN 
				SET @mut_mnt_net = @w_plafond - @w_sold_plaf ;
			end 
			
				Print N' @@mut_mnt_net '+cast ( @mut_mnt_net as varchar );
				Print N' @@@ACT_mnt_net '+cast ( @ACT_mnt_net as varchar );
				Print N' @@@@ACT_mnt_honor '+cast ( @ACT_mnt_honor as varchar );
				--------------------
		if  @mut_mnt_net + @ACT_mnt_net > @ACT_mnt_honor 
		begin
			set @mut_mnt_net = @ACT_mnt_honor -  @ACT_mnt_net;
		end  
			
			-----------plafond acte
			begin try
				select @var = ISNULL(mnt_net,0)  from lig_bult
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_num_fam
				and dat_soin = @ACT_datsoin
				and NUM_SOIN= @NEW_NUM_SOIN
				and abrv_act = @NEW_ABRV_ACT ;
			end try
			begin catch
				Print N'when others begin nullplafond acte ';
			end catch;
		
			Print N' @mut_mnt_net '+cast ( @mut_mnt_net as varchar );
			Print N' @@v_plafond '+cast ( @v_plafond as varchar );
			if @v_plafonne = 'O' and ISNULL(@var,0) + @mut_mnt_net >= @v_plafond
			begin
				set @mut_mnt_net = @v_plafond - ISNULL(@var,0);
			end  
			
			select @x = count(*)  from lig_bult
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers
			and num_fam  = @NEW_num_fam
			and dat_soin = @ACT_datsoin
			and NUM_SOIN= @NEW_NUM_SOIN
			and abrv_act = @NEW_ABRV_ACT ;
			
			Print N'No erreur  xx found  '+cast ( @x as varchar );
			
-----clacul mntconjoint-------------------------------------------------

select @COD_FIL_CONJ =COD_FIL from PERS_AFFIL where MAT_PERS = (select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C') 
and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')


select @COD_ASSUR_CONJ=COD_ASSUR from PERSONNEl where MAT_PERS =(select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
 and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')


				SET @mnt_net_conj = @ACT_vmnt_honor - @ACT_mnt_net - @mut_mnt_net
				
				EXEC    [dbo].[calcul_mntNetConj]
		            @soc = @NEW_cod_soc,
		            @mat = @NEW_mat_pers,
					@cod_fil =@COD_FIL_CONJ,
					@cod_assur = @COD_ASSUR_CONJ,
					@numfam = @NEW_num_fam,
					@datsoin = @ACT_datsoin,
					@sauv_net = 0,
					@cumul_net = 0,
					@ACT_vmnt_honor = @mnt_net_conj,
					@ACT_indice = @ACT_indice,
					@ACT_ABRV_ACT = @NEW_ABRV_ACT,
					
					@lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
					@lig_sauv = @lig_sauv_calcul OUTPUT,
					@bult_cum = @bult_cum_calcul OUTPUT
				------------------------------------------------------------------------
				
				


			if @x = 0 
			begin
				select @v_NUM_LIG = ISNULL(max(ISNULL(NUM_LIG,0)),0) + 1  
				from lig_bult
				where cod_soc = @NEW_cod_soc
				and mat_pers =@NEW_mat_pers
				and NUM_FAM = @NEW_num_fam
				and NUM_SOIN= @NEW_NUM_SOIN
				
				and DAT_SOIN = @ACT_datsoin;

				Print N'No erreur  @v_NUM_LIG found  '+cast ( @v_NUM_LIG as varchar );
			
				----insertion lig_bult
				begin try
				
				insert into lig_bult(cod_soc,mat_pers,num_fam,dat_soin,abrv_act,
				num_lig,mnt_honor,mnt_net
				,dat_act,indice ,NUM_SOIN,mnt_net_conj)
				values(@NEW_cod_soc,@NEW_mat_pers,@NEW_num_fam,@ACT_datsoin,@NEW_ABRV_ACT
				,@v_NUM_LIG,@ACT_vmnt_honor
				,ISNULL(@mut_mnt_net,0),@ACT_dat_act,@ACT_indice,@NEW_NUM_SOIN,@lig_mntNet_calcul);
				end try
				begin catch
				 Print N'when others begin null lig_bult  ' +ERROR_MESSAGE();
				  Print N' @@NEW_cod_soc = > ' +cast ( @NEW_cod_soc as varchar );
				   Print N' @@@NEW_mat_pers = > ' +cast ( @NEW_mat_pers as varchar );
				    Print N' @@@NEW_num_fam = > ' +cast ( @NEW_num_fam as varchar );
				     Print N' @@@ACT_datsoin = > ' +cast ( @ACT_datsoin as varchar );
				end catch;
				
			end
			else begin
				Print N' @@@upd	ate = > ' +cast ( @ACT_vmnt_honor as varchar );
				Print N' @@@mut_mnt_net = > ' +cast ( @mut_mnt_net as varchar );
				------update lig_bult dans le cas de l'existance
				update lig_bult set mnt_honor = ISNULL(mnt_honor,0) + @ACT_vmnt_honor ,
				mnt_net = ISNULL(mnt_net,0) + ISNULL(@mut_mnt_net,0) ,indice = indice + @ACT_indice  , MNT_NET_CONJ = ISNULL(MNT_NET_CONJ,0)+@lig_mntNet_calcul,DAT_ACT  = @ACT_dat_act
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_num_fam
				and dat_soin = @ACT_datsoin 
				and NUM_SOIN= @NEW_NUM_SOIN
				and abrv_act = @NEW_ABRV_ACT;
			end ;
			
			
				PRINT N'insertion @@@mut_mnt_net '+cast (@mut_mnt_net as varchar);	
				update LIG_BULT_ACT   set mut_mnt_net=@mut_mnt_net ,mnt_net_conj =@mnt_net_conj
				from LIG_BULT_ACT a
				join inserted b on (
				 a.cod_soc =  b.cod_soc
				and a.mat_pers = b.mat_pers
				and a.num_fam  = b.num_fam
				and a.dat_soin = b.dat_soin 
				and a.NUM_SOIN=  b.NUM_SOIN 
				and a.abrv_act = b.abrv_act
				and a.DAT_ACT = b.DAT_ACT);
				--------------------------------------------
			
			Print N' @@@ACT_NUM_LIG = > ' +cast ( @ACT_NUM_LIG as varchar );
			
			
			---------insertion dans lig_bult_act le champs @mut_mnt_net pour pouvoir réutiliser la valeur
			-------update plafon_cnam
			----------------------------------------------------------
			--if nvl(:bord_arriver.reg_bord,'N') = 'N' begin
			update plafond_cnam set sold_cnam_estim = ISNULL(sold_cnam_estim,0) + ISNULL(@ACT_mnt_net,0)
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers
			and annee_cnam = (select max(annee_cnam) from plafond_cnam
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers);
			  --and annee_cnam  = to_number(to_char(@datsoin ,'yyyy'));
			--END
	END		
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [insert_act_update] on [LIG_BULT_ACT] 
for update 
as
begin

	-- Mise à jour faite par le trigger insert_act_insert lui-même (écriture de mut_mnt_net / totaux) : on ne recalcule pas.
	-- Remplace l'ancienne désactivation / réactivation du trigger, qui prenait un verrou de schéma (Sch-M) sur la table et provoquait des deadlocks.
	IF TRIGGER_NESTLEVEL(OBJECT_ID(N'dbo.insert_act_insert')) > 0
		RETURN;

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
	@w_plafond_detail     NUMERIC(10,3),
	@w_sold_plaf_detail   NUMERIC(10,3),
	
	--for cursor
	@NEW_cod_soc varchar(max),
	@NEW_mat_pers varchar(max),
	@NEW_num_fam  int,
	@ACT_datsoin   DATE,
	@NEW_ABRV_ACT varchar(max),
	@ACT_cod_act varchar(max),
	@ACT_indice numeric(15,3),
	@ACT_mnt_honor numeric(15,3),
	@ACT_mnt_net numeric(15,3),
	@ACT_act_prix numeric(15,3),
	@NEW_mnt_honor numeric(15,3),
	@NEW_NUM_SOIN varchar(max),
	@ACT_mnt_remb  varchar(max),
	@ACT_dat_act varchar(max),
	@ACT_MUT_MNT_NET numeric(15,3),
	@ACT_NUM_LIG numeric(6),
	@ACT_anc_mnt_honor numeric(15,3),
	@OLD_mut_mnt_net numeric(15,3),
	@vsold_ectim numeric(15,3),
	@OLD_cod_soc varchar(max),
	@OLD_NUM_SOIN varchar(max),
	 @OLD_mnt_net numeric(15,3),
	 @OLD_mat_pers varchar(max),
	 @OLD_INDICE numeric(15,3),
	 @OLD_mnt_honor numeric(15,3),
	 
	 ---- pour conjoint------------
	 @mnt_net_conj_update numeric(15,3),
	 @lig_mntNet_calcul numeric(15,3),
	 @lig_sauv_calcul numeric(15,3),
	 @bult_cum_calcul numeric(15,3),
	 @OLD_mnt_conj numeric(15,3),
	 @COD_FIL_CONJ varchar(max),
	 @COD_ASSUR_CONJ varchar(max)
	 
	 ------------------------------
	 
	Print N'[insert_act_update]';
	
		begin 
			
			
			select @NEW_NUM_SOIN = NUM_SOIN from deleted i ;
			
			select @OLD_NUM_SOIN = NUM_SOIN from inserted i ;
			select @OLD_mut_mnt_net = mut_mnt_net from deleted i ;
			select @OLD_cod_soc = cod_soc from deleted i ;
			select @OLD_mnt_net = mnt_net from deleted i ;
			select @OLD_mat_pers = mat_pers  from deleted i ;
			select @OLD_INDICE = isnull(indice,1) from deleted i ;
			select @OLD_mnt_honor = mnt_honor from deleted i ;
			select @OLD_mnt_conj = mnt_net_conj from inserted i;
			
			
			select @ACT_NUM_LIG = NUM_LIG from inserted i ;
			select @ACT_indice = isnull(indice,1) from inserted i ;
			select @NEW_mnt_honor = mnt_honor from inserted i ;
			select @ACT_mnt_net = mnt_net from inserted i ;
			select @NEW_cod_soc = cod_soc from inserted i ;
			select @NEW_mat_pers = mat_pers from inserted i ;
			select @NEW_num_fam = NUM_FAM from inserted i ;
			select @ACT_cod_act = COD_ACT from inserted i ;
			select @ACT_datsoin = DAT_SOIN from inserted i ;
			select @NEW_ABRV_ACT = abrv_act from inserted i ;
			select @ACT_act_prix = act_prix from inserted i;
			select @ACT_dat_act = dat_act from inserted i;
			Print N'No erreur found'+ @NEW_ABRV_ACT;
			
			Print N' ACT_vmnt_honor = >'+ cast ( @NEW_mnt_honor as varchar );
			Print N' OLD_mnt_honor = >'+ cast ( @OLD_mnt_honor as varchar );
			Print N' @OLD_mut_mnt_net = >'+ cast ( @OLD_mut_mnt_net as varchar );
			Print N' @ACT_mnt_net = >'+ cast ( @ACT_mnt_net as varchar );
			
			Print N'HELLO';
			
			select @v_indic= ISNULL(a_indice,'N'),@v_nat_act =ISNULL(nat_act,'M'),
			@v_plafonne =ISNULL(plafonne,'N'),@v_taux_act =ISNULL(taux_act,0)
			,@v_mtt_acte = ISNULL(mtt_acte,0),@v_plafond = ISNULL(plafond,0)
			,@v_proratat=ISNULL(proratat,'N')
			from bareme_remb
			where COD_FIL = (select cod_fil from pers_affil where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers 
			and courant = 'O')
			and COD_ASSUR = (select cod_assur from personnel where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers)
			and ABRV_ACT = @NEW_ABRV_ACT;
			
			select @w_plafond = ISNULL(PLAFOND,0),@w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM,0)  - @OLD_mut_mnt_net
            from plafond_assur
            where cod_soc = @NEW_cod_soc and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam
			and ANNEE_ASSUR = (select max(ANNEE_ASSUR) from plafond_assur where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers and num_fam = @NEW_num_fam);      
            --to_number(to_char(@datsoin ,'yyyy'));
			
			
					------------------------------------
			   	
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
			----------------------
			
			
			
			
			
			
			
			
			Print N' TETTTT @@v_nat_act = >'+ cast ( @v_nat_act as varchar );
			Print N' @v_indic = >'+ cast ( @v_indic as varchar );
			Print N' @v_plafonne = >'+ cast ( @v_plafonne as varchar );
			
			Print N'   @@@@v_mnt_net' + cast( @v_mnt_net as varchar ); 
			
			Print N'   @@@@mut_mnt_net' + cast( @mut_mnt_net as varchar ); 
			
			if @v_indic = 'O' 
			begin
			
				---------------------------calcul du net par rapport au contrat
				if @v_nat_act = 'M'
				begin 
					SET @v_mnt_net = cast(@ACT_indice as float) * cast(@v_mtt_acte as float);
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
					SET @v_mnt_net = cast(@ACT_indice as float) * cast(@NEW_mnt_honor as float) * (cast(@v_taux_act as float)/100);   
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
					SET @v_mnt_net =  cast(@ACT_act_prix as float) * (cast(@v_taux_act as float)/100);  
					Print N' BEFORE @@v_taux_act  = >'+ cast ( @v_taux_act as varchar );
					if @v_plafonne = 'O' and @v_mnt_net > @v_plafond
					begin 
						Print N' BEFORE @@v_plafonne  = >'+ cast ( @v_plafonne as varchar );
						set @mut_mnt_net = @v_plafond;
						
					END
					else begin
						set @mut_mnt_net = @v_mnt_net;
						
					END
				END   
					-------------------------------------------------------------       
			END
			
			
			Print N' @w_sold_plaf = >'+ cast ( @w_sold_plaf as varchar );
			Print N' @w_plafond = >'+ cast ( @w_plafond as varchar );
			------------comparaison avec le plafond de l'assurance
			-------------------------------------------------------
			if @mut_mnt_net + @w_sold_plaf   > @w_plafond 
            BEGIN 
				SET @mut_mnt_net = @w_plafond - @w_sold_plaf ;
			end 
			Print N' AAAAAA @mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
			Print N' AAAAAA @@ACT_mnt_net = >'+ cast ( @ACT_mnt_net as varchar );
			Print N' AAAAAA @@@NEW_mnt_honor = >'+ cast ( @NEW_mnt_honor as varchar );
			--------------------
		if  @mut_mnt_net + @ACT_mnt_net > @NEW_mnt_honor 
		begin
			set @mut_mnt_net = @NEW_mnt_honor -  @ACT_mnt_net;
		end  
			Print N' AFTER @@mut_mnt_net = >'+ cast ( @mut_mnt_net as varchar );
			
			-----------plafond acte
			begin try
				select @var = ISNULL(mnt_net,0)-ISNULL(@OLD_mut_mnt_net,0) from lig_bult
				where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_num_fam
				and dat_soin = @ACT_datsoin
				and NUM_SOIN= @NEW_NUM_SOIN
				and abrv_act = @NEW_ABRV_ACT ;
			end try
			begin catch
				Print N'when others begin nullplafond acte ';
			end catch;
		
			Print N' @mut_mnt_net '+cast ( @mut_mnt_net as varchar );
			Print N' @@v_plafond '+cast ( @v_plafond as varchar );
			if @v_plafonne = 'O' and ISNULL(@var,0) + @mut_mnt_net >= @v_plafond
			begin
				set @mut_mnt_net = @v_plafond - ISNULL(@var,0);
			end  
			
			Print N' @@@upd	ate = > ' +cast ( @NEW_mnt_honor as varchar );
			Print N' @@@mut_mnt_net = > ' +cast ( @mut_mnt_net as varchar );
			
			
			---------------clacul mnt net conj -------------------------------------------
			
			select @COD_FIL_CONJ =COD_FIL from PERS_AFFIL where MAT_PERS = (select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C') 
and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')

select @COD_ASSUR_CONJ=COD_ASSUR from PERSONNEl where MAT_PERS =(select MAT_PERS_CONJ from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
 and COD_SOC = (select cod_soc_conj from FAMILLE where MAT_PERS=@NEW_mat_pers and COD_SOC = @NEW_cod_soc and NUM_FAM = '99' and PARENTE ='C')
 
			SET @mnt_net_conj_update = @NEW_mnt_honor - @ACT_mnt_net - @mut_mnt_net 
			
			EXEC    [dbo].[calcul_mntNetConj]
		            @soc = @NEW_cod_soc,
		            @mat = @NEW_mat_pers,
					@cod_fil =@COD_FIL_CONJ,
					@cod_assur = @COD_ASSUR_CONJ,
					@numfam = @NEW_num_fam,
					@datsoin = @ACT_datsoin,
					@sauv_net = 0,
					@cumul_net = 0,
					@ACT_vmnt_honor = @mnt_net_conj_update,
					@ACT_indice = @ACT_indice,
					@ACT_ABRV_ACT = @NEW_ABRV_ACT,
					
					@lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
					@lig_sauv = @lig_sauv_calcul OUTPUT,
					@bult_cum = @bult_cum_calcul OUTPUT
					
					
					
					select @OLD_mnt_conj=MNT_NET_CONJ from LIG_BULT where cod_soc = @OLD_cod_soc
					and mat_pers = @OLD_mat_pers
					and num_fam  = @NEW_num_fam 
					and dat_soin = @ACT_datsoin 
					and NUM_SOIN= @NEW_NUM_SOIN
					and abrv_act = @NEW_ABRV_ACT 
					and DAT_ACT = @ACT_dat_act
			------------------------------------------------------------------------------
			
			
			
			------update lig_bult dans le cas de l'existance
			update lig_bult set mnt_honor = ISNULL(mnt_honor,0) + @NEW_mnt_honor-@OLD_mnt_honor ,
			mnt_net = ISNULL(mnt_net,0) + ISNULL(@mut_mnt_net,0) -ISNULL(@OLD_mut_mnt_net,0)  ,
			indice = indice - @OLD_INDICE + @ACT_indice,
			MNT_NET_CONJ = ISNULL(MNT_NET_CONJ,0)+ @lig_mntNet_calcul - ISNULL(@OLD_mnt_conj,0),DAT_ACT = @ACT_dat_act
			where cod_soc = @NEW_cod_soc
			and mat_pers = @NEW_mat_pers
			and num_fam  = @NEW_num_fam
			and dat_soin = @ACT_datsoin 
			and NUM_SOIN= @NEW_NUM_SOIN
			and abrv_act = @NEW_ABRV_ACT;
		
			 
			Print N' @@@@mut_mnt_net = > ' +cast ( @mut_mnt_net as varchar );
			update lig_bult_act  set mut_mnt_net=@mut_mnt_net ,mnt_net_conj = @lig_mntNet_calcul
			where cod_soc = @NEW_cod_soc
				and mat_pers = @NEW_mat_pers
				and num_fam  = @NEW_num_fam
				and dat_soin = @ACT_datsoin 
				and NUM_SOIN= @NEW_NUM_SOIN
				and abrv_act = @NEW_ABRV_ACT
				and NUM_LIG = @ACT_NUM_LIG
				and DAT_ACT =  @ACT_dat_act
			
			Print N' @@@ACT_NUM_LIG = > ' +cast ( @ACT_NUM_LIG as varchar );
			
			--------updat plafond cnam
-----------------------------------------
select @vsold_ectim=ISNULL(sold_cnam_estim,0)   from plafond_cnam
  where cod_soc = @OLD_cod_soc
  and mat_pers = @OLD_mat_pers 
  --and annee_cnam  = to_number(to_char(@OLD_dat_soin,'yyyy'));
  and annee_cnam = (select max(annee_cnam) from plafond_cnam
						                   	where cod_soc = @OLD_cod_soc
							                   and mat_pers = @OLD_mat_pers);
 Print N' @@@@vsold_ectim = > ' +cast ( @vsold_ectim as varchar );
  set @vsold_ectim = @vsold_ectim - ISNULL(@OLD_mnt_net,0) + ISNULL(@ACT_mnt_net,0);
  if @vsold_ectim < 0 
  begin
 set @vsold_ectim = 0;
  end 
  Print N' @@@@@vsold_ectim = > ' +cast ( @vsold_ectim as varchar );
 update plafond_cnam set sold_cnam_estim = @vsold_ectim
  where cod_soc = @OLD_cod_soc
  and mat_pers = @OLD_mat_pers 
  --and annee_cnam  = to_number(to_char(@OLD_dat_soin,'yyyy'));
  and annee_cnam = (select max(annee_cnam) from plafond_cnam
						                   	where cod_soc = @OLD_cod_soc
							                   and mat_pers = @OLD_mat_pers);
			
			
			
	END		
END
GO

-- ===== LIG_BULT =====
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [insert_lig_bult_insert] on [LIG_BULT] 
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
ALTER TRIGGER [insert_lig_bult_update] on [LIG_BULT] 
for update 
as
begin

	-- Mise à jour faite par le trigger insert_lig_bult_insert lui-même (écriture de mut_mnt_net / totaux) : on ne recalcule pas.
	-- Remplace l'ancienne désactivation / réactivation du trigger, qui prenait un verrou de schéma (Sch-M) sur la table et provoquait des deadlocks.
	IF TRIGGER_NESTLEVEL(OBJECT_ID(N'dbo.insert_lig_bult_insert')) > 0
		RETURN;

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

-- Contrôle final : aucune définition ne contient plus DISABLE/ENABLE TRIGGER, et les 8 triggers existent, actifs.
IF ((SELECT COUNT(*) FROM sys.triggers t JOIN sys.sql_modules m ON m.object_id = t.object_id
     WHERE t.name IN (N'insert_med_insert', N'insert_med_update', N'insert_app_insert', N'insert_app_update', N'insert_act_insert', N'insert_act_update', N'insert_lig_bult_insert', N'insert_lig_bult_update') AND t.is_disabled = 0
       AND m.definition NOT LIKE N'%disable trigger%' AND m.definition NOT LIKE N'%enable trigger%') = 8)
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;
GO
