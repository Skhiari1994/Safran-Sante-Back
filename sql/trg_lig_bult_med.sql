USE [ramidb]
GO

/*
Table LIG_BULT_MED : les 3 triggers insert_med_insert, insert_med_update et insert_med_delete
sont remplaces par UN SEUL trigger, dbo.trg_lig_bult_med (AFTER INSERT, UPDATE, DELETE).
Retour arriere : trg_lig_bult_med_rollback.sql
*/

BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.insert_med_insert', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_med_insert;
IF OBJECT_ID(N'dbo.insert_med_update', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_med_update;
IF OBJECT_ID(N'dbo.insert_med_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_med_delete;
IF OBJECT_ID(N'dbo.trg_lig_bult_med', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_lig_bult_med;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER dbo.trg_lig_bult_med
ON dbo.LIG_BULT_MED
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    IF TRIGGER_NESTLEVEL(@@PROCID) > 1
        RETURN;

    DECLARE @operation char(1) = CASE
        WHEN EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted) THEN 'U'
        WHEN EXISTS (SELECT 1 FROM inserted) THEN 'I'
        WHEN EXISTS (SELECT 1 FROM deleted)  THEN 'D'
    END;

    IF @operation IS NULL
        RETURN;

    DECLARE @une_seule_ligne bit = CASE
        WHEN (SELECT COUNT(*) FROM inserted) = 1 AND (SELECT COUNT(*) FROM deleted) = 1 THEN 1 ELSE 0 END;

    DECLARE @l TABLE (
        rn              int IDENTITY(1,1) PRIMARY KEY,
        n_cod_soc       varchar(4),
        n_mat_pers      varchar(10),
        n_num_fam       int,
        n_dat_soin      date,
        n_abrv_act      varchar(4),
        n_cod_med       varchar(10),
        n_num_lig       float,
        n_num_soin      numeric(18,0),
        n_dat_act       date,
        n_indice        numeric(6),
        n_mnt_honor     numeric(15,3),
        n_mnt_net       numeric(15,3),
        n_mdc_prix      numeric(15,3),
        n_med_prix      numeric(15,3),
        n_prix_remb     numeric(15,3),
        o_cod_soc       varchar(4),
        o_mat_pers      varchar(10),
        o_num_fam       int,
        o_dat_soin      date,
        o_abrv_act      varchar(4),
        o_num_soin      numeric(18,0),
        o_indice        numeric(6),
        o_mnt_honor     numeric(15,3),
        o_mnt_net       numeric(15,3),
        o_mut_mnt_net   numeric(15,3),
        o_mnt_net_conj  numeric(15,3)
    );

    IF @operation = 'I'
        INSERT INTO @l (n_cod_soc, n_mat_pers, n_num_fam, n_dat_soin, n_abrv_act, n_cod_med, n_num_lig, n_num_soin,
                        n_dat_act, n_indice, n_mnt_honor, n_mnt_net, n_mdc_prix, n_med_prix, n_prix_remb)
        SELECT cod_soc, mat_pers, num_fam, dat_soin, abrv_act, cod_med, num_lig, num_soin,
               dat_act, indice, mnt_honor, mnt_net, mdc_prix, med_prix, prix_remb
        FROM inserted
        ORDER BY cod_soc, mat_pers, num_fam, dat_soin, num_soin, abrv_act, cod_med, num_lig, dat_act;
    ELSE IF @operation = 'D'
        INSERT INTO @l (o_cod_soc, o_mat_pers, o_num_fam, o_dat_soin, o_abrv_act, o_num_soin,
                        o_indice, o_mnt_honor, o_mnt_net, o_mut_mnt_net, o_mnt_net_conj)
        SELECT cod_soc, mat_pers, num_fam, dat_soin, abrv_act, num_soin,
               indice, mnt_honor, mnt_net, mut_mnt_net, mnt_net_conj
        FROM deleted
        ORDER BY cod_soc, mat_pers, num_fam, dat_soin, num_soin, abrv_act, cod_med, num_lig, dat_act;
    ELSE
        INSERT INTO @l (n_cod_soc, n_mat_pers, n_num_fam, n_dat_soin, n_abrv_act, n_cod_med, n_num_lig, n_num_soin,
                        n_dat_act, n_indice, n_mnt_honor, n_mnt_net, n_mdc_prix, n_med_prix, n_prix_remb,
                        o_cod_soc, o_mat_pers, o_num_fam, o_dat_soin, o_abrv_act, o_num_soin,
                        o_indice, o_mnt_honor, o_mnt_net, o_mut_mnt_net)
        SELECT i.cod_soc, i.mat_pers, i.num_fam, i.dat_soin, i.abrv_act, i.cod_med, i.num_lig, i.num_soin,
               i.dat_act, i.indice, i.mnt_honor, i.mnt_net, i.mdc_prix, i.med_prix, i.prix_remb,
               d.cod_soc, d.mat_pers, d.num_fam, d.dat_soin, d.abrv_act, d.num_soin,
               d.indice, d.mnt_honor, d.mnt_net, d.mut_mnt_net
        FROM inserted i
        JOIN deleted d
          ON @une_seule_ligne = 1
          OR (    d.cod_soc  = i.cod_soc
              AND d.mat_pers = i.mat_pers
              AND d.num_fam  = i.num_fam
              AND d.dat_soin = i.dat_soin
              AND d.abrv_act = i.abrv_act
              AND d.cod_med  = i.cod_med
              AND d.num_lig  = i.num_lig
              AND d.dat_act  = i.dat_act
              AND d.num_soin = i.num_soin)
        ORDER BY i.cod_soc, i.mat_pers, i.num_fam, i.dat_soin, i.num_soin, i.abrv_act, i.cod_med, i.num_lig, i.dat_act;

    DECLARE
        @n_cod_soc varchar(4), @n_mat_pers varchar(10), @n_num_fam int, @n_dat_soin date, @n_abrv_act varchar(4),
        @n_cod_med varchar(10), @n_num_lig float, @n_num_soin numeric(18,0), @n_dat_act date,
        @n_indice numeric(6), @n_mnt_honor numeric(15,3), @n_mnt_net numeric(15,3),
        @n_mdc_prix numeric(15,3), @n_med_prix numeric(15,3), @n_prix_remb numeric(15,3),
        @o_cod_soc varchar(4), @o_mat_pers varchar(10), @o_num_fam int, @o_dat_soin date, @o_abrv_act varchar(4),
        @o_num_soin numeric(18,0), @o_indice numeric(6), @o_mnt_honor numeric(15,3), @o_mnt_net numeric(15,3),
        @o_mut_mnt_net numeric(15,3), @o_mnt_net_conj numeric(15,3),

        @ref_num_soin numeric(18,0),
        @v_proratat varchar(1), @v_indic varchar(1), @v_nat_act varchar(1), @v_plafonne varchar(1),
        @v_taux_act numeric(5,2), @v_mtt_acte numeric(10,3), @v_plafond numeric(10,3),
        @w_plafond numeric(10,3), @w_sold_plaf numeric(10,3),
        @w_plafond_detail numeric(10,3), @w_sold_plaf_detail numeric(10,3),
        @wval_prix varchar(1), @wprix numeric(10,3), @v_mnt_net numeric(10,3), @mut_mnt_net numeric(10,3),
        @var numeric(10,3), @x numeric(10,3), @v_num_lig numeric(10,3), @anc_net_mut numeric(10,3),
        @old_mnt_conj numeric(15,3),

        @mnt_net_conj numeric(15,3), @lig_mntNet_calcul numeric(15,3), @lig_sauv_calcul numeric(15,3),
        @bult_cum_calcul numeric(15,3), @cod_fil_conj varchar(max), @cod_assur_conj varchar(max),
        @fil_calcul varchar(max), @assur_calcul varchar(max);

    DECLARE @i int = 1, @nb int = (SELECT COUNT(*) FROM @l);

    WHILE @i <= @nb
    BEGIN
        SELECT @ref_num_soin = NULL,
               @v_proratat = NULL, @v_indic = NULL, @v_nat_act = NULL, @v_plafonne = NULL, @v_taux_act = NULL,
               @v_mtt_acte = NULL, @v_plafond = NULL, @w_plafond = NULL, @w_sold_plaf = NULL,
               @w_plafond_detail = NULL, @w_sold_plaf_detail = NULL, @wval_prix = NULL, @wprix = NULL,
               @v_mnt_net = NULL, @mut_mnt_net = NULL, @var = NULL, @x = NULL, @v_num_lig = NULL,
               @anc_net_mut = NULL, @old_mnt_conj = NULL, @mnt_net_conj = NULL, @lig_mntNet_calcul = NULL,
               @lig_sauv_calcul = NULL, @bult_cum_calcul = NULL, @cod_fil_conj = NULL, @cod_assur_conj = NULL,
               @fil_calcul = NULL, @assur_calcul = NULL;

        SELECT @n_cod_soc = n_cod_soc, @n_mat_pers = n_mat_pers, @n_num_fam = n_num_fam, @n_dat_soin = n_dat_soin,
               @n_abrv_act = n_abrv_act, @n_cod_med = n_cod_med, @n_num_lig = n_num_lig, @n_num_soin = n_num_soin,
               @n_dat_act = n_dat_act, @n_indice = n_indice, @n_mnt_honor = n_mnt_honor, @n_mnt_net = n_mnt_net,
               @n_mdc_prix = n_mdc_prix, @n_med_prix = n_med_prix, @n_prix_remb = n_prix_remb,
               @o_cod_soc = o_cod_soc, @o_mat_pers = o_mat_pers, @o_num_fam = o_num_fam, @o_dat_soin = o_dat_soin,
               @o_abrv_act = o_abrv_act, @o_num_soin = o_num_soin, @o_indice = o_indice,
               @o_mnt_honor = o_mnt_honor, @o_mnt_net = o_mnt_net, @o_mut_mnt_net = o_mut_mnt_net,
               @o_mnt_net_conj = o_mnt_net_conj
        FROM @l
        WHERE rn = @i;

        IF @operation = 'D'
        BEGIN
            BEGIN TRY
                SELECT @anc_net_mut = ISNULL(mnt_net, 0)
                FROM lig_bult
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND num_soin = @o_num_soin
                  AND dat_soin = @o_dat_soin
                  AND abrv_act = @o_abrv_act;
            END TRY
            BEGIN CATCH
                SET @anc_net_mut = @o_mut_mnt_net;
            END CATCH

            IF @anc_net_mut = @o_mut_mnt_net
            BEGIN
                DELETE lig_bult
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND num_soin = @o_num_soin
                  AND dat_soin = @o_dat_soin
                  AND abrv_act = @o_abrv_act;
            END
            ELSE
            BEGIN
                UPDATE lig_bult
                SET mnt_honor    = ISNULL(mnt_honor, 0) - @o_mnt_honor,
                    mnt_net      = ISNULL(mnt_net, 0) - @o_mut_mnt_net,
                    indice       = indice - @o_indice,
                    MNT_NET_CONJ = ISNULL(MNT_NET_CONJ, 0) - ISNULL(@o_mnt_net_conj, 0)
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND num_soin = @o_num_soin
                  AND dat_soin = @o_dat_soin
                  AND abrv_act = @o_abrv_act;
            END

            UPDATE plafond_cnam
            SET sold_cnam_estim = CASE WHEN ISNULL(sold_cnam_estim, 0) - ISNULL(@o_mnt_net, 0) < 0
                                       THEN 0
                                       ELSE ISNULL(sold_cnam_estim, 0) - ISNULL(@o_mnt_net, 0) END
            WHERE cod_soc    = @o_cod_soc
              AND mat_pers   = @o_mat_pers
              AND annee_cnam = YEAR(@o_dat_soin);
        END
        ELSE
        BEGIN
            SET @ref_num_soin = CASE WHEN @operation = 'I' THEN @n_num_soin ELSE @o_num_soin END;

            SELECT @w_plafond = ISNULL(PLAFOND, 0), @w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM, 0)
            FROM plafond_assur
            WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam
              AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                 WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

            SELECT @v_indic    = ISNULL(a_indice, 'N'),
                   @v_nat_act  = ISNULL(nat_act, 'M'),
                   @v_plafonne = ISNULL(plafonne, 'N'),
                   @v_taux_act = ISNULL(taux_act, 0),
                   @v_mtt_acte = ISNULL(mtt_acte, 0),
                   @v_plafond  = ISNULL(plafond, 0),
                   @v_proratat = ISNULL(proratat, 'N')
            FROM bareme_remb
            WHERE COD_FIL = (SELECT cod_fil FROM pers_affil
                             WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND courant = 'O')
              AND COD_ASSUR = (SELECT cod_assur FROM personnel
                               WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers)
              AND ABRV_ACT = @n_abrv_act;

            IF @v_proratat = 'O'
            BEGIN
                SELECT @w_plafond_detail = ISNULL(PLAFOND, 0), @w_sold_plaf_detail = ISNULL(SOLD_ASSUR_ESTIM, 0)
                FROM plafond_assur_detail
                WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam
                  AND ABRV_ACT = @n_abrv_act
                  AND COD_FIL = (SELECT cod_fil FROM pers_affil
                                 WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND courant = 'O')
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers);

                IF @w_plafond_detail < @w_plafond
                    SET @w_plafond = @w_plafond_detail;
                IF @w_sold_plaf_detail < @w_sold_plaf
                    SET @w_sold_plaf = @w_sold_plaf_detail;
            END

            SELECT @wval_prix = val_prix
            FROM ref_fill_act
            WHERE cod_fil = (SELECT cod_fil FROM pers_affil
                             WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND courant = 'O')
              AND abrv_act = @n_abrv_act;

            IF @wval_prix = 'P'
                SET @wprix = ISNULL(@n_mdc_prix, 0);
            ELSE IF @wval_prix = 'G'
                SET @wprix = ISNULL(@n_med_prix, 0);
            ELSE
                SET @wprix = ISNULL(@n_prix_remb, 0);

            IF @v_indic = 'O'
            BEGIN
                IF @v_nat_act = 'M'
                BEGIN
                    SET @v_mnt_net = CAST(@n_indice AS float) * CAST(@v_mtt_acte AS float);
                    IF @v_plafonne = 'O' AND @v_mnt_net > @v_plafond
                        SET @mut_mnt_net = @v_plafond;
                    ELSE IF @v_mnt_net > @n_mnt_honor
                        SET @mut_mnt_net = @n_mnt_honor;
                    ELSE
                        SET @mut_mnt_net = @v_mnt_net;
                END
                ELSE
                BEGIN
                    SET @v_mnt_net = CAST(@n_indice AS float) * CAST(@wprix AS float) * (CAST(@v_taux_act AS float) / 100);
                    IF @v_plafonne = 'O' AND @v_mnt_net > @v_plafond
                        SET @mut_mnt_net = @v_plafond;
                    ELSE
                        SET @mut_mnt_net = @v_mnt_net;
                END
            END
            ELSE
            BEGIN
                IF @v_nat_act = 'M'
                BEGIN
                    SET @v_mnt_net = @n_mnt_honor;
                    IF @v_plafonne = 'O' AND @v_mnt_net > @v_plafond
                        SET @mut_mnt_net = @v_plafond;
                    ELSE IF @v_mnt_net > @v_mtt_acte
                        SET @mut_mnt_net = @v_mtt_acte;
                    ELSE
                        SET @mut_mnt_net = @v_mnt_net;
                END
                ELSE
                BEGIN
                    SET @v_mnt_net = CAST(@wprix AS float) * (CAST(@v_taux_act AS float) / 100);
                    IF @v_plafonne = 'O' AND @v_mnt_net > @v_plafond
                        SET @mut_mnt_net = @v_plafond;
                    ELSE
                        SET @mut_mnt_net = @v_mnt_net;
                END
            END

            IF @mut_mnt_net + @n_mnt_net <> @n_mnt_honor
                SET @mut_mnt_net = @n_mnt_honor - @n_mnt_net;

            IF @mut_mnt_net + @w_sold_plaf > @w_plafond
                SET @mut_mnt_net = @w_plafond - @w_sold_plaf;

            BEGIN TRY
                SELECT @var = ISNULL(mnt_net, 0)
                FROM lig_bult
                WHERE cod_soc  = @n_cod_soc
                  AND mat_pers = @n_mat_pers
                  AND num_fam  = @n_num_fam
                  AND num_soin = @ref_num_soin
                  AND dat_soin = @n_dat_soin
                  AND abrv_act = @n_abrv_act;
            END TRY
            BEGIN CATCH
            END CATCH

            IF @v_plafonne = 'O' AND ISNULL(@var, 0) + @mut_mnt_net >= @v_plafond
                SET @mut_mnt_net = @v_plafond - ISNULL(@var, 0);

            IF @operation = 'I'
            BEGIN
                SELECT @x = COUNT(*)
                FROM lig_bult
                WHERE cod_soc  = @n_cod_soc
                  AND mat_pers = @n_mat_pers
                  AND num_fam  = @n_num_fam
                  AND num_soin = @n_num_soin
                  AND dat_soin = @n_dat_soin
                  AND abrv_act = @n_abrv_act;

                SELECT @fil_calcul = '03', @assur_calcul = '01';
            END
            ELSE
            BEGIN
                SELECT @cod_fil_conj = COD_FIL
                FROM PERS_AFFIL
                WHERE MAT_PERS = (SELECT MAT_PERS_CONJ FROM FAMILLE
                                  WHERE MAT_PERS = @n_mat_pers AND COD_SOC = @n_cod_soc AND NUM_FAM = '99' AND PARENTE = 'C')
                  AND COD_SOC = (SELECT cod_soc_conj FROM FAMILLE
                                 WHERE MAT_PERS = @n_mat_pers AND COD_SOC = @n_cod_soc AND NUM_FAM = '99' AND PARENTE = 'C');

                SELECT @cod_assur_conj = COD_ASSUR
                FROM PERSONNEL
                WHERE MAT_PERS = (SELECT MAT_PERS_CONJ FROM FAMILLE
                                  WHERE MAT_PERS = @n_mat_pers AND COD_SOC = @n_cod_soc AND NUM_FAM = '99' AND PARENTE = 'C')
                  AND COD_SOC = (SELECT cod_soc_conj FROM FAMILLE
                                 WHERE MAT_PERS = @n_mat_pers AND COD_SOC = @n_cod_soc AND NUM_FAM = '99' AND PARENTE = 'C');

                SELECT @fil_calcul = @cod_fil_conj, @assur_calcul = @cod_assur_conj;
            END

            SET @mnt_net_conj = @n_mnt_honor - @n_mnt_net - @mut_mnt_net;

            EXEC [dbo].[calcul_mntNetConj]
                 @soc            = @n_cod_soc,
                 @mat            = @n_mat_pers,
                 @cod_fil        = @fil_calcul,
                 @cod_assur      = @assur_calcul,
                 @numfam         = @n_num_fam,
                 @datsoin        = @n_dat_soin,
                 @sauv_net       = 0,
                 @cumul_net      = 0,
                 @ACT_vmnt_honor = @mnt_net_conj,
                 @ACT_indice     = @n_indice,
                 @ACT_ABRV_ACT   = @n_abrv_act,
                 @lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
                 @lig_sauv       = @lig_sauv_calcul OUTPUT,
                 @bult_cum       = @bult_cum_calcul OUTPUT;

            IF @operation = 'I'
            BEGIN
                IF @x = 0
                BEGIN
                    SELECT @v_num_lig = ISNULL(MAX(ISNULL(num_lig, 0)), 0) + 1
                    FROM lig_bult
                    WHERE cod_soc  = @n_cod_soc
                      AND mat_pers = @n_mat_pers
                      AND num_fam  = @n_num_fam
                      AND num_soin = @n_num_soin
                      AND dat_soin = @n_dat_soin;

                    BEGIN TRY
                        INSERT INTO lig_bult (cod_soc, mat_pers, num_fam, dat_soin, abrv_act, num_lig, mnt_honor,
                                              mnt_net, dat_act, indice, num_soin, MNT_NET_CONJ)
                        VALUES (@n_cod_soc, @n_mat_pers, @n_num_fam, @n_dat_soin, @n_abrv_act, @v_num_lig,
                                @n_mnt_honor, @mut_mnt_net, @n_dat_act, @n_indice, @n_num_soin, @lig_mntNet_calcul);
                    END TRY
                    BEGIN CATCH
                    END CATCH
                END
                ELSE
                BEGIN
                    UPDATE lig_bult
                    SET mnt_honor    = ISNULL(mnt_honor, 0) + @n_mnt_honor,
                        mnt_net      = ISNULL(mnt_net, 0) + @mut_mnt_net,
                        indice       = indice + @n_indice,
                        MNT_NET_CONJ = ISNULL(MNT_NET_CONJ, 0) + @lig_mntNet_calcul,
                        DAT_ACT      = @n_dat_act
                    WHERE cod_soc  = @n_cod_soc
                      AND mat_pers = @n_mat_pers
                      AND num_fam  = @n_num_fam
                      AND num_soin = @n_num_soin
                      AND dat_soin = @n_dat_soin
                      AND abrv_act = @n_abrv_act;
                END

                UPDATE lig_bult_med
                SET mut_mnt_net  = @mut_mnt_net,
                    mnt_net_conj = @lig_mntNet_calcul
                WHERE cod_soc  = @n_cod_soc
                  AND mat_pers = @n_mat_pers
                  AND num_fam  = @n_num_fam
                  AND dat_soin = @n_dat_soin
                  AND num_soin = @n_num_soin
                  AND cod_med  = @n_cod_med
                  AND dat_act  = @n_dat_act;

                UPDATE plafond_cnam
                SET sold_cnam_estim = ISNULL(sold_cnam_estim, 0) + ISNULL(@n_mnt_net, 0)
                WHERE cod_soc  = @n_cod_soc
                  AND mat_pers = @n_mat_pers
                  AND annee_cnam = (SELECT MAX(annee_cnam) FROM plafond_cnam
                                    WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers);
            END
            ELSE
            BEGIN
                SELECT @old_mnt_conj = MNT_NET_CONJ
                FROM LIG_BULT
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND dat_soin = @o_dat_soin
                  AND num_soin = @ref_num_soin
                  AND abrv_act = @o_abrv_act
                  AND DAT_ACT  = @n_dat_act;

                UPDATE lig_bult
                SET mnt_honor    = ISNULL(mnt_honor, 0) + @n_mnt_honor - @o_mnt_honor,
                    mnt_net      = ISNULL(mnt_net, 0) + @mut_mnt_net - @o_mut_mnt_net,
                    indice       = indice - @o_indice + @n_indice,
                    MNT_NET_CONJ = ISNULL(MNT_NET_CONJ, 0) + @lig_mntNet_calcul - @old_mnt_conj,
                    DAT_ACT      = @n_dat_act
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_soin = @ref_num_soin
                  AND num_fam  = @o_num_fam
                  AND dat_soin = @o_dat_soin
                  AND abrv_act = @o_abrv_act;

                UPDATE lig_bult_med
                SET mut_mnt_net  = @mut_mnt_net,
                    MNT_NET_CONJ = @lig_mntNet_calcul
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND dat_soin = @o_dat_soin
                  AND num_soin = @ref_num_soin
                  AND abrv_act = @o_abrv_act
                  AND dat_act  = @n_dat_act;

                UPDATE plafond_cnam
                SET sold_cnam_estim = CASE
                        WHEN (SELECT ISNULL(c.sold_cnam_estim, 0) FROM plafond_cnam c
                              WHERE c.cod_soc = @o_cod_soc AND c.mat_pers = @o_mat_pers
                                AND c.annee_cnam = (SELECT MAX(annee_cnam) FROM plafond_cnam
                                                    WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers))
                             - ISNULL(@o_mnt_net, 0) + ISNULL(@n_mnt_net, 0) < 0
                        THEN 0
                        ELSE (SELECT ISNULL(c.sold_cnam_estim, 0) FROM plafond_cnam c
                              WHERE c.cod_soc = @o_cod_soc AND c.mat_pers = @o_mat_pers
                                AND c.annee_cnam = (SELECT MAX(annee_cnam) FROM plafond_cnam
                                                    WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers))
                             - ISNULL(@o_mnt_net, 0) + ISNULL(@n_mnt_net, 0)
                    END
                WHERE cod_soc    = @o_cod_soc
                  AND mat_pers   = @o_mat_pers
                  AND annee_cnam = YEAR(@o_dat_soin);
            END
        END

        SET @i += 1;
    END
END
GO

IF EXISTS (SELECT 1 FROM sys.triggers WHERE name = N'trg_lig_bult_med' AND is_disabled = 0
                                         AND parent_id = OBJECT_ID(N'dbo.LIG_BULT_MED'))
   AND NOT EXISTS (SELECT 1 FROM sys.triggers WHERE name IN (N'insert_med_insert', N'insert_med_update', N'insert_med_delete'))
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;
GO
