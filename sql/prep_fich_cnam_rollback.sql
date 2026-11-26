/*
  Retour arrière de prep_fich_cnam : version d'origine (avant le réagencement), exportée de la base le 24/11/2026.
*/
USE [ramidb]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[prep_fich_cnam](@codSoc varchar(MAX) ,@codBord varchar(MAX),@returnString varbinary(Max) output,@returnString2 varbinary(Max) output)
AS
BEGIN
DECLARE
@pecCnam varchar(1),
@ligne_bord      varchar(MAX)='',
@ligne_bord1      varchar(MAX)='',
@ligne_bord2      varchar(MAX)='',
@ligne_bult      varchar(MAX) = '',
@ligne_visit     varchar(MAX) = '',
@ligne_act       varchar(MAX)='',
@ligne_med       varchar(MAX)='',
@ligne_app       varchar(MAX)='',
--f               text_io.FILE_TYPE ,
@v_cod_retr      varchar(1),
@p_cod_retr      varchar(1),
@v_num_retr      varchar(10),
@p_num_retr      varchar(10),
@lien_par        varchar(1),
@nom_prenom      varchar(150),
@rang_m          varchar (2),
@p_DAT_NAIS      varchar (10),
@vclef           varchar (2),
--
@bord_cod_soc  varchar (MAX),
@bord_cod_bord varchar (MAX),
@bord_cod_b    varchar (MAX),
@bord_DAT_BORD varchar (MAX),
@bord_nbr_b    varchar (MAX),
@bord_tot_h    varchar (MAX),
@espace_8      varchar (8) = '        ',
@espace_20     varchar (20) ='                    ',
@espace_5      varchar (5) = '     ',
@espace_12     varchar (12)='            ',
@entree        varchar(MAX) = '
'
DECLARE bord CURSOR 
 FOR
	select cod_soc,
		   cod_bord,
		   dbo.LPAD(cod_bord,20,' ') as cod_b,
		   DAT_BORD,
		   dbo.LPAD(NBR_BULT,5,0) as nbr_b,
		   dbo.LPAD(replace(TOT_HONOR,'.',''),12,0) as tot_h
                from bord_envoi
                where cod_soc = @codSoc
                and cod_bord = @codBord;
OPEN bord
 FETCH NEXT FROM bord into
	@bord_cod_soc,
	@bord_cod_bord,
	@bord_cod_b,
	@bord_DAT_BORD,
	@bord_nbr_b,
	@bord_tot_h
 while @@FETCH_STATUS = 0
 BEGIN
	 select 
	 @v_cod_retr = (
	 select CASE when 
	 COD_RETR = 'C' then 1 
	 ELSE 2 END AS LN),
	 @v_num_retr = dbo.LPAD(num_retr,8,'0')
  	 from societe
  	 where cod_soc = @bord_cod_soc;
  
  	SET @ligne_bord = '1'+ISNULL(@v_cod_retr,' ')+
  						  ISNULL(@v_num_retr,@espace_8)+
  						  convert(varchar, getdate(), 112)+
  						  ISNULL(@bord_cod_b,@espace_20)+
  						  ISNULL(@bord_nbr_b,@espace_5)+
  						  ISNULL(@bord_tot_h,@espace_12)+@entree;

 /*******************************************************************/
 DECLARE 
@bult_COD_SOC varchar(MAX),
@bult_MAT_PERS varchar(MAX),
@bult_NUM_FAM varchar(MAX),
@bult_dat_soin varchar(MAX),
@bult_num_soin varchar(MAX),
@bult_dat_s varchar(MAX),
@bult_tot_h varchar(MAX),
@bult_cod_m varchar(MAX),
@bult_reg_r varchar(MAX),
@bult_num_p varchar(MAX),
@bult_ord   varchar(MAX),
@bult_num_s varchar(MAX)
 DECLARE bult CURSOR 
 FOR
	select														COD_SOC		,
																MAT_PERS	,
																NUM_FAM		,
																dat_soin	,
																num_soin	,
		   convert(varchar, DAT_SOIN, 112)						dat_s		,
		   dbo.lpad(replace(TOT_HONOR,'.',''),10,'0')		tot_h		,
		   ISNULL(dbo.lpad(COD_MALAD,4,'0'),'0000')								cod_m		,
		   REG_REMB												reg_r		,
		   ISNULL(dbo.lpad(NUM_PEC,12,'0'),'000000000000')			    num_p		,
           dbo.lpad(ORD_BULT,5,'0')									ord			,
           ISNULL(dbo.RPAD(num_soin,20,' '),'                    ') num_s
      from bult_soin
      where cod_soc = @codSoc
      and cod_bord = @codBord
      order by ORD_BULT;
               
OPEN bult
 FETCH NEXT FROM bult into
@bult_COD_SOC ,
@bult_MAT_PERS ,
@bult_NUM_FAM ,
@bult_dat_soin ,
@bult_num_soin,
@bult_dat_s ,
@bult_tot_h ,
@bult_cod_m ,
@bult_reg_r ,
@bult_num_p ,
@bult_ord   ,
@bult_num_s 
 while @@FETCH_STATUS = 0
 BEGIN   
  		Print N'test '+@bult_MAT_PERS
  		Print N'@bult_ord '+@bult_ord
 		select
 	    @p_cod_retr = cod_retr,
 		@p_num_retr = 
 		 CASE LEN(dbo.LTRIM_STR(num_retr,'0')) 
		  WHEN 10 then num_retr
		  WHEN 9 then dbo.LPAD(dbo.LTRIM_STR(num_retr,'0'),10,'0')
		 ELSE dbo.RPAD(dbo.LPAD(dbo.LTRIM_STR(num_retr,'0'),8,'0'),10,'0') END 
  		from personnel 
 		where cod_soc = @bult_COD_SOC and mat_pers = @bult_MAT_PERS;
  	--execute spWriteStringToFile @ligne_bord,'D:\Utilisateurs\hja','testfile2.txt'
  	--execute spWriteStringToFile 'Hamadi','D:\Utilisateurs\hja','testfile2.txt'
  	    select 
		@lien_par = CASE @bult_NUM_FAM
		  WHEN 0 then '0'
		  WHEN 99 then '1'
		  WHEN 97 then '3'
		  WHEN 98 then '4'
		 ELSE 2 END ;
		 
	if @bult_NUM_FAM = 0 
  	 begin
  		select 
  		@nom_prenom = dbo.RPAD(SUBSTRING(nom_pers + ' ' + pren_pers, 1, 60), 60, ' '),
        @p_DAT_NAIS = ISNULL(convert(varchar,DAT_NAIS, 112), '        ')
		from personnel 
		where cod_soc = @bult_cod_soc and 
		mat_pers = @bult_mat_pers;
  	end
  	else
  	 begin
  		select 
  		@nom_prenom = dbo.RPAD(SUBSTRING(nom_pers + ' ' + NOM_PREN, 1, 60), 60, ' '),
        @p_DAT_NAIS = ISNULL(convert(varchar,DAT_NAISS, 112), '        ')
  		from famille f,
  		     personnel p
  		where 
  		p.cod_soc	   = @bult_COD_SOC 
  		and p.mat_pers = @bult_MAT_PERS
  		and f.cod_soc  = p.cod_soc 
  		and f.mat_pers = p.mat_pers
  		and f.num_fam  =  @bult_NUM_FAM;
  	 end;
    
    
   -- if(len(@nom_prenom)>60)
	set @nom_prenom=LEFT(@nom_prenom,60);
  	
  	select 
	 @rang_m = DBO.LPAD(
	CASE @bult_NUM_FAM		
	WHEN 0  THEN '0'
	WHEN 99 THEN '0'
	WHEN 97 THEN '0'
	WHEN 98 THEN '4'
	ELSE @bult_NUM_FAM END , 2,0);
	
	SET 	@ligne_bult = '2'+
	ISNULL(@bult_ord,'     ')+
	ISNULL(@p_cod_retr,' ')+
	ISNULL(@p_num_retr,'          ')+
	ISNULL(@lien_par,' ')+
	ISNULL(@nom_prenom,'                                                            ')+
	ISNULL(@rang_m,'  ')+
	ISNULL(@p_DAT_NAIS,'        ')+
	ISNULL(@bult_dat_s,'        ')+
	ISNULL(@bult_tot_h,'          ')+
	ISNULL(@bult_cod_m,'    ')+
	@bult_num_s+
	ISNULL(@bult_reg_r,' ')+
	@bult_num_p+@entree;
    SET @ligne_bord = @ligne_bord+@ligne_bult
/************************VISITE***************************************/
  	
  	
  	
DECLARE
 @visit_PRF_TYP1    varchar(MAX),
 @visit_PRF_COD     varchar(MAX),
 @visit_PRF_TYP2    varchar(MAX),
 @visit_cod_etab    varchar(MAX),
 @visit_m_h			varchar(MAX),
 @visit_d_act		varchar(MAX),
 @COD_VISIT varchar(max),
 @visit_c_visite    varchar(MAX),
 @ligne_visite      varchar(MAX);
 print 'matpers'
 print @bult_mat_pers
 print 'numfam'
 print @bult_NUM_FAM
 print 'dates'
 print @bult_dat_s
 
  DECLARE visits CURSOR
   FOR
  	 select 
  	    PRF_TYP,
		PRF_COD,
		dbo.LPAD(PRF_TYP,2,'0')+dbo.LPAD(PRF_COD,8,'0') cod_etab,
		dbo.LPAD((mnt_honor*1000),10,'0') m_h,
		convert(varchar,dat_act, 112) d_act,
		COD_VISIT,
		dbo.RPAD(COD_VISIT,5,' ') c_visite
     from lig_bult_visit
     where 
     cod_soc	  = @bult_COD_SOC 
     and mat_pers = @bult_mat_pers
     and num_fam  = @bult_NUM_FAM 
     and dat_soin = @bult_dat_s
     and num_soin = @bult_num_soin
	
 OPEN visits
 FETCH NEXT FROM visits into
 @visit_PRF_TYP1,
 @visit_PRF_COD,
 @visit_cod_etab,
 @visit_m_h,
 @visit_d_act,
 @COD_VISIT,
 @visit_c_visite
 while @@FETCH_STATUS = 0
 BEGIN   
 print 'test visit'
 
	select @pecCnam = isnull(pec_cnam,'O')  
  	from REF_VISIT
  	where COD_VISIT = @COD_VISIT
  	
  		if @pecCnam <> 'N'
  	begin 
  	
  	select @vclef = dbo.LPAD(PRF_CLE,2,'0')  
  	from ref_etablis
  	where PRF_TYP = @visit_PRF_TYP1
  	and PRF_COD 	= @visit_PRF_COD;
  	
  	
SET @ligne_visit = '3'+
ISNULL(@bult_ord,'     ')+
ISNULL(@visit_cod_etab,'          ')+
ISNULL(@vclef,'  ')+
ISNULL(@visit_m_h,'          ')+
ISNULL(@visit_d_act,'        ')+
ISNULL(@visit_c_visite,'     ')+@entree;
SET @ligne_bord = @ligne_bord+@ligne_visit
 end
 FETCH NEXT FROM visits into
 @visit_PRF_TYP1,
 @visit_PRF_COD,
 @visit_cod_etab,
 @visit_m_h,
 @visit_d_act,
 @COD_VISIT,
 @visit_c_visite
 END --end visite
 CLOSE visits 
 DEALLOCATE visits
/***********************************END VISITE****************************/
/***********************************MED***********************************/
DECLARE
@med_PRF_TYP    varchar(MAX),--1
	    @med_PRF_COD	varchar(MAX),--2
	    @med_c_etab     varchar(MAX),--3
		@med_m_h_m		varchar(MAX),--4
		@med_d_med		varchar(MAX),--5
		@med_c_med		varchar(MAX),--6
		@med_indi 		varchar(MAX),--7
		@med_nbr_jour   varchar(MAX),--8
		@med_acc		varchar(MAX),--9
		@med_n_pec varchar(MAX),--10
		@COD_med varchar(max)--11

DECLARE med CURSOR
FOR
 select PRF_TYP,--1
		PRF_COD,--2
		dbo.LPAD(PRF_TYP,2,'0')+dbo.LPAD(PRF_COD,8,'0') c_etab,--3
		dbo.LPAD((mnt_honor*1000),10,'0') m_h_m,--4
		convert(varchar,dat_act, 112)   d_med,--5
		COD_med,--11
		dbo.LPAD(COD_med,8,'0') c_med,--6
		dbo.LPAD(indice,4,'0') indi,--7
		ISNULL(dbo.LPAD(nbr_j,3,'0'),'    ') nbr_jour,--8
		ISNULL(accord_med,'N') acc,--9
		dbo.LPAD(num_pec_med,12,'0') n_pec--10
     from lig_bult_med
     where 
     cod_soc	  = @bult_COD_SOC 
     and mat_pers = @bult_mat_pers
     and num_fam  = @bult_NUM_FAM 
     and dat_soin = @bult_dat_s
     and num_soin = @bult_num_soin
     order by num_lig
 OPEN med
 FETCH NEXT FROM med into
		@med_PRF_TYP,--1
	    @med_PRF_COD,--2
	    @med_c_etab,--3
		@med_m_h_m,--4
		@med_d_med,--5
		@COD_med,--11
		@med_c_med,--6
		@med_indi,--7
		@med_nbr_jour,--8
		@med_acc,--9
		@med_n_pec --10
		while @@FETCH_STATUS = 0
		BEGIN
			
			select @pecCnam = isnull(pec_cnam,'O')  
  	from REF_MED
  	where COD_MED = @COD_med
  	
  		if @pecCnam <> 'N'
  	begin 
  	
			
			
			select @vclef = dbo.LPAD(PRF_CLE,2,'0')
  			from ref_etablis
  		    where PRF_TYP = @med_PRF_TYP
  			and PRF_COD 	= @med_PRF_COD;
  			
  			SET @ligne_med = '4'+
			 ISNULL(@bult_ord,'     ')+
			 ISNULL(@med_c_etab,'          ')+
			 ISNULL(@vclef,'  ')+
			 ISNULL(@med_c_med,'        ')+
			 ISNULL(@med_d_med,'        ')+
			 ISNULL(@med_indi,'    ')+
			 ISNULL(@med_m_h_m,'          ')+
			 ISNULL(@med_nbr_jour,'   ')+
			 ISNULL(@med_acc,' ')+
			 ISNULL(@med_n_pec,'000000000000')+
			 @entree;
			 SET @ligne_bord = @ligne_bord+@ligne_med
			 
	end
		 FETCH NEXT FROM med into
        @med_PRF_TYP,
	    @med_PRF_COD,
	    @med_c_etab,
		@med_m_h_m,
		@med_d_med,
		@COD_med,
		@med_c_med,
		@med_indi,
		@med_nbr_jour,
		@med_acc,
		@med_n_pec

 END --end med
 CLOSE med 
 DEALLOCATE med
 
/***********************************FIN MED*******************************/	
/***********************************MED***********************************/	
/***********************************ACT***********************************/
DECLARE
@act_PRF_TYP  	varchar(MAX),
		@act_PRF_COD	varchar(MAX),
		@act_a_etab		varchar(MAX),
		@act_m_h_a		varchar(MAX),
		@act_d_act		varchar(MAX),
		@COD_act			varchar(MAX),		
		@act_c_act		varchar(MAX),
		@act_a_acc		varchar(MAX),
		@act_a_n_pec	varchar(MAX),
		@act_let		varchar(MAX),
		@act_cot		varchar(MAX),
		@act_typ_prof	varchar(MAX)
DECLARE acts CURSOR
FOR
 select PRF_TYP,
		PRF_COD,
		dbo.LPAD(PRF_TYP,2,'0')+dbo.LPAD(PRF_COD,8,'0')				  a_etab,
		dbo.LPAD((mnt_honor*1000),10,'0')							  m_h_a,
		convert(varchar,dat_act, 112)								  d_act,
		COD_act,
		dbo.LPAD(COD_act,10,' ')										c_act,
		ISNULL(accord_act,'N')										  a_acc,
		dbo.LPAD(NUM_PEC_ACT,12,'0')								  a_n_pec,
		ISNULL(dbo.RPAD(LET_COD,10,' '),'          ')				  let,
		ISNULL(dbo.LPAD(COT_ACT,4,'0'),'    ')						  cot,
		ISNULL(CASE TYP_PRF		
			   WHEN 'P' THEN ' '
			   ELSE TYP_PRF END,' ')								  typ_prof
 from lig_bult_act
  where 
     cod_soc	  = @bult_COD_SOC 
     and mat_pers = @bult_mat_pers
     and num_fam  = @bult_NUM_FAM 
     and dat_soin = @bult_dat_s
     and num_soin = @bult_num_soin
     order by num_lig
OPEN acts 
FETCH NEXT FROM acts into 
@act_PRF_TYP,
		@act_PRF_COD,
		@act_a_etab,
		@act_m_h_a,
		@act_d_act,
		@COD_act,
		@act_c_act,		
		@act_a_acc,
		@act_a_n_pec,
		@act_let,
		@act_cot,
		@act_typ_prof
while @@FETCH_STATUS = 0
 BEGIN
 
 	
	select @pecCnam = isnull(pec_cnam,'O')  
  	from REF_ACT
  	where COD_ACT = @COD_act
  	
  	if @pecCnam <> 'N'
  	begin 
  
		
	
 
  	select @vclef = dbo.LPAD(PRF_CLE,2,'0')  
  	from ref_etablis
  	where 
  	PRF_TYP			= @act_PRF_TYP
  	and PRF_COD 	= @act_PRF_COD
  	
  	
  		SET @ligne_act = '5'+
				  ISNULL(@bult_ord,'     ')+
				  ISNULL(@act_a_etab,'          ')+
				  ISNULL(@vclef,'  ')+
				  'M'+
				  ISNULL(@act_c_act,'          ')+
				  ISNULL(@act_let,'          ')+
				  ISNULL(@act_cot,'    ')+
				  ISNULL(@act_m_h_a,'          ')+
				  ISNULL(@act_d_act,'        ')+
				  ISNULL(@act_a_acc,' ')+
				  ISNULL(@act_a_n_pec,'000000000000')+@entree
		 SET @ligne_bord = @ligne_bord+@ligne_act
	end	
FETCH NEXT FROM acts into 
   @act_PRF_TYP,
		@act_PRF_COD,
		@act_a_etab,
		@act_m_h_a,
		@act_d_act,
		@COD_act,
		@act_c_act,		
		@act_a_acc,
		@act_a_n_pec,
		@act_let,
		@act_cot,
		@act_typ_prof
END --end act
 CLOSE acts
 DEALLOCATE acts
/***********************************FIN ACT*******************************/	
/***********************************APP***********************************/
DECLARE
@app_PRF_TYP varchar(MAX),
		   @app_PRF_COD varchar(MAX),
		   @app_p_etab  varchar(MAX),
		   @app_m_h_p  varchar(MAX),
		   @app_d_app  varchar(MAX),
		   @COD_APP varchar(max),
		   @app_c_app  varchar(MAX),
		   @app_a_app  varchar(MAX),
		   @app_p_n_pec varchar(MAX)
DECLARE apps CURSOR 
FOR
  select PRF_TYP,
		   PRF_COD,
		   dbo.LPAD(PRF_TYP,2,'0')+dbo.LPAD(PRF_COD,8,'0') p_etab,
		   dbo.LPAD((mnt_honor*1000),10,'0') m_h_p,
		   convert(varchar,dat_act, 112) d_app,
		   COD_APP,
		   dbo.RPAD(COD_APP,10,' ') c_app,
		   ISNULL(ACCORD_APP,'N') a_app,
		   dbo.LPAD(NUM_PEC_APP,12,'0') p_n_pec
     from lig_bult_app
     where 
     cod_soc	  = @bult_COD_SOC 
     and mat_pers = @bult_mat_pers
     and num_fam  = @bult_NUM_FAM 
     and dat_soin = @bult_dat_s
     and num_soin = @bult_num_soin
     OPEN apps 
FETCH NEXT FROM apps into 
		   @app_PRF_TYP,
		   @app_PRF_COD,
		   @app_p_etab,
		   @app_m_h_p,
		   @app_d_app,
		   @COD_APP,
		   @app_c_app,
		   @app_a_app,
		   @app_p_n_pec
while @@FETCH_STATUS = 0
 BEGIN
 
 	
			select @pecCnam = isnull(pec_cnam,'O')  
  	from REF_APPAREIL
  	where COD_APP = @COD_APP
  	
  		if @pecCnam <> 'N'
  	begin 
  	
		
 
 
 SET @ligne_app = '6'+
				  ISNULL(@bult_ord,'     ')+
				  ISNULL(@app_p_etab,'          ')+
				  ISNULL(@vclef,'  ')+
				  ISNULL(@app_c_app,'          ')+
				  ISNULL(@app_d_app,'        ')+
				  ISNULL(@app_m_h_p,'          ')+
				  ISNULL(@app_a_app,' ')+
				  ISNULL(@app_p_n_pec,'000000000000')+@entree;
			SET @ligne_bord = @ligne_bord+@ligne_app
			
			end	  
 FETCH NEXT FROM apps into 
		   @app_PRF_TYP,
		   @app_PRF_COD,
		   @app_p_etab,
		   @app_m_h_p,
		   @app_d_app,
		   @COD_APP,
		   @app_c_app,
		   @app_a_app,
		   @app_p_n_pec
  END;
   CLOSE apps
 DEALLOCATE apps
/***********************************FIN APP*******************************/ 	
  
--end bult  	
  	FETCH NEXT FROM bult into
	@bult_COD_SOC ,
	@bult_MAT_PERS ,
	@bult_NUM_FAM ,
	@bult_dat_soin ,
	@bult_num_soin ,
	@bult_dat_s ,
	@bult_tot_h ,
	@bult_cod_m ,
	@bult_reg_r ,
	@bult_num_p ,
	@bult_ord   ,
	@bult_num_s 
 END --end bult
 CLOSE bult 
 DEALLOCATE bult
 /******************************************************************/
   	FETCH NEXT FROM bord into
	@bord_cod_soc,
	@bord_cod_bord,
	@bord_cod_b,
	@bord_DAT_BORD,
	@bord_nbr_b,
	@bord_tot_h
 END; --end bord
 CLOSE bord
 DEALLOCATE bord
 set @ligne_bord1 = substring(@ligne_bord,0,len(@ligne_bord)/2)
 set @ligne_bord2 = substring(@ligne_bord,len(@ligne_bord)/2,len(@ligne_bord))
 set @returnString = cast( @ligne_bord1 as varbinary(max))
 set @returnString2 = cast( @ligne_bord2 as varbinary(max))
 	-- Exec spWriteStringToFile @ligne_bord,'C:/','testsql.txt' 
 	print 'ligne bord****************'
END
GO
