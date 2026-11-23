USE [ramidb]
GO

/*
Table LIG_BULT : les 3 triggers insert_lig_bult_insert, insert_lig_bult_update et insert_lig_bult_delete
sont remplaces par UN SEUL trigger, dbo.trg_lig_bult (AFTER INSERT, UPDATE, DELETE).
Retour arriere : trg_lig_bult_rollback.sql
*/

BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.insert_lig_bult_insert', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_lig_bult_insert;
IF OBJECT_ID(N'dbo.insert_lig_bult_update', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_lig_bult_update;
IF OBJECT_ID(N'dbo.insert_lig_bult_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_lig_bult_delete;
IF OBJECT_ID(N'dbo.trg_lig_bult', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_lig_bult;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER dbo.trg_lig_bult
ON dbo.LIG_BULT
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
        n_num_soin      bigint,
        n_mnt_net       numeric(15,3),
        n_mnt_net_conj  numeric(15,3),
        o_cod_soc       varchar(4),
        o_mat_pers      varchar(10),
        o_num_fam       int,
        o_abrv_act      varchar(4),
        o_mnt_net       numeric(15,3),
        o_mnt_net_conj  numeric(15,3),
        o_imput_plaf    varchar(1)
    );

    IF @operation = 'I'
        INSERT INTO @l (n_cod_soc, n_mat_pers, n_num_fam, n_dat_soin, n_abrv_act, n_num_soin, n_mnt_net, n_mnt_net_conj)
        SELECT cod_soc, mat_pers, num_fam, dat_soin, abrv_act, num_soin, mnt_net, mnt_net_conj
        FROM inserted
        ORDER BY id_lig_bult;
    ELSE IF @operation = 'D'
        INSERT INTO @l (o_cod_soc, o_mat_pers, o_num_fam, o_abrv_act, o_mnt_net, o_mnt_net_conj, o_imput_plaf)
        SELECT cod_soc, mat_pers, num_fam, abrv_act, ISNULL(mnt_net, 0), ISNULL(mnt_net_conj, 0), ISNULL(imput_plaf, 'O')
        FROM deleted
        ORDER BY id_lig_bult;
    ELSE
        INSERT INTO @l (n_cod_soc, n_mat_pers, n_num_fam, n_dat_soin, n_abrv_act, n_num_soin, n_mnt_net, n_mnt_net_conj,
                        o_cod_soc, o_mat_pers, o_mnt_net, o_mnt_net_conj)
        SELECT i.cod_soc, i.mat_pers, i.num_fam, i.dat_soin, i.abrv_act, i.num_soin, i.mnt_net, i.mnt_net_conj,
               d.cod_soc, d.mat_pers, ISNULL(d.mnt_net, 0), d.mnt_net_conj
        FROM inserted i
        JOIN deleted d
          ON @une_seule_ligne = 1
          OR d.id_lig_bult = i.id_lig_bult
        ORDER BY i.id_lig_bult;

    DECLARE
        @n_cod_soc varchar(4), @n_mat_pers varchar(10), @n_num_fam int, @n_dat_soin date, @n_abrv_act varchar(4),
        @n_num_soin bigint, @n_mnt_net numeric(15,3), @n_mnt_net_conj numeric(15,3),
        @o_cod_soc varchar(4), @o_mat_pers varchar(10), @o_num_fam int, @o_abrv_act varchar(4),
        @o_mnt_net numeric(15,3), @o_mnt_net_conj numeric(15,3), @o_imput_plaf varchar(1),

        @b_cod_soc varchar(4), @b_mat_pers varchar(10), @b_abrv_act varchar(4),
        @v_indic varchar(1), @v_nat_act varchar(1), @v_plafonne varchar(1), @v_taux_act numeric(5,2),
        @v_mtt_acte numeric(10,3), @v_plafond numeric(10,3), @v_proratat varchar(1),
        @v_ctr_duree varchar(1), @v_duree_act numeric(6), @v_imput_plaf varchar(1),
        @Mat_Pers_Conj varchar(max), @Cod_Soc_Conj varchar(max), @numfamC int,
        @vsold_ectim numeric(15,3), @vsold_ectim_conj numeric(15,3);

    DECLARE @i int = 1, @nb int = (SELECT COUNT(*) FROM @l);

    WHILE @i <= @nb
    BEGIN
        SELECT @v_indic = NULL, @v_nat_act = NULL, @v_plafonne = NULL, @v_taux_act = NULL, @v_mtt_acte = NULL,
               @v_plafond = NULL, @v_proratat = NULL, @v_ctr_duree = NULL, @v_duree_act = NULL, @v_imput_plaf = NULL,
               @Mat_Pers_Conj = NULL, @Cod_Soc_Conj = NULL, @numfamC = NULL,
               @vsold_ectim = NULL, @vsold_ectim_conj = NULL;

        SELECT @n_cod_soc = n_cod_soc, @n_mat_pers = n_mat_pers, @n_num_fam = n_num_fam, @n_dat_soin = n_dat_soin,
               @n_abrv_act = n_abrv_act, @n_num_soin = n_num_soin, @n_mnt_net = n_mnt_net, @n_mnt_net_conj = n_mnt_net_conj,
               @o_cod_soc = o_cod_soc, @o_mat_pers = o_mat_pers, @o_num_fam = o_num_fam, @o_abrv_act = o_abrv_act,
               @o_mnt_net = o_mnt_net, @o_mnt_net_conj = o_mnt_net_conj, @o_imput_plaf = o_imput_plaf
        FROM @l
        WHERE rn = @i;

        IF @operation = 'D'
        BEGIN
            SELECT @b_cod_soc = @o_cod_soc, @b_mat_pers = @o_mat_pers, @b_abrv_act = @o_abrv_act,
                   @v_imput_plaf = @o_imput_plaf;

            IF @o_num_fam = 0
                SET @numfamC = 99;
            ELSE IF @o_num_fam = 99
                SET @numfamC = 0;
            ELSE
                SET @numfamC = @o_num_fam;

            SELECT @Mat_Pers_Conj = f.MAT_PERS_CONJ, @Cod_Soc_Conj = f.cod_soc_conj
            FROM FAMILLE f
            WHERE f.MAT_PERS = @o_mat_pers
              AND f.COD_SOC  = @o_cod_soc
              AND f.NUM_FAM  = '99'
              AND f.PARENTE  = 'C';
        END
        ELSE
        BEGIN
            SELECT @b_cod_soc = @n_cod_soc, @b_mat_pers = @n_mat_pers, @b_abrv_act = @n_abrv_act;

            IF @n_num_fam = 0
                SET @numfamC = 99;
            ELSE IF @n_num_fam = 99
                SET @numfamC = 0;
            ELSE
                SET @numfamC = @n_num_fam;

            IF @operation = 'I'
                SELECT @Mat_Pers_Conj = f.MAT_PERS_CONJ, @Cod_Soc_Conj = f.cod_soc_conj
                FROM FAMILLE f
                WHERE f.MAT_PERS = @n_mat_pers
                  AND f.COD_SOC  = @n_cod_soc
                  AND f.NUM_FAM  = @numfamC
                  AND f.PARENTE  = 'C';
            ELSE
                SELECT @Mat_Pers_Conj = f.MAT_PERS_CONJ, @Cod_Soc_Conj = f.cod_soc_conj
                FROM FAMILLE f
                WHERE f.MAT_PERS = @o_mat_pers
                  AND f.COD_SOC  = @o_cod_soc
                  AND f.NUM_FAM  = @numfamC
                  AND f.PARENTE  = 'C';
        END

        SELECT @v_indic    = ISNULL(a_indice, 'N'),
               @v_nat_act  = ISNULL(nat_act, 'M'),
               @v_plafonne = ISNULL(plafonne, 'N'),
               @v_taux_act = ISNULL(taux_act, 0),
               @v_mtt_acte = ISNULL(mtt_acte, 0),
               @v_plafond  = ISNULL(plafond, 0),
               @v_proratat = ISNULL(proratat, 'N')
        FROM bareme_remb
        WHERE COD_FIL = (SELECT cod_fil FROM pers_affil
                         WHERE cod_soc = @b_cod_soc AND mat_pers = @b_mat_pers AND courant = 'O')
          AND COD_ASSUR = (SELECT cod_assur FROM personnel
                           WHERE cod_soc = @b_cod_soc AND mat_pers = @b_mat_pers)
          AND ABRV_ACT = @b_abrv_act;

        IF @operation <> 'D'
        BEGIN
            BEGIN TRY
                SELECT @v_ctr_duree  = ISNULL(ctr_duree, 'N'),
                       @v_duree_act  = ISNULL(duree_act, 0),
                       @v_imput_plaf = ISNULL(IMPUT_PLAF, 'O')
                FROM acte
                WHERE abrv_act = @n_abrv_act;
            END TRY
            BEGIN CATCH
            END CATCH
        END

        IF @v_imput_plaf = 'O'
        BEGIN
            IF @operation = 'I'
            BEGIN
                UPDATE plafond_assur
                SET SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM, 0) + @n_mnt_net_conj
                WHERE cod_soc  = @Cod_Soc_Conj
                  AND mat_pers = @Mat_Pers_Conj
                  AND num_fam  = @numfamC
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);

                UPDATE plafond_assur
                SET SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM, 0) + ISNULL(@n_mnt_net, 0)
                WHERE cod_soc  = @n_cod_soc
                  AND mat_pers = @n_mat_pers
                  AND num_fam  = @n_num_fam
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

                IF @v_proratat = 'O'
                BEGIN
                    UPDATE plafond_assur_detail
                    SET SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM, 0) + ISNULL(@n_mnt_net_conj, 0)
                    WHERE cod_soc  = @Cod_Soc_Conj
                      AND mat_pers = @Mat_Pers_Conj
                      AND num_fam  = @numfamC
                      AND ABRV_ACT = @n_abrv_act
                      AND COD_FIL  = (SELECT cod_fil FROM pers_affil
                                      WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND courant = 'O')
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                         WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);

                    UPDATE plafond_assur_detail
                    SET SOLD_ASSUR_ESTIM = ISNULL(SOLD_ASSUR_ESTIM, 0) + ISNULL(@n_mnt_net, 0)
                    WHERE cod_soc  = @n_cod_soc
                      AND mat_pers = @n_mat_pers
                      AND num_fam  = @n_num_fam
                      AND ABRV_ACT = @n_abrv_act
                      AND COD_FIL  = (SELECT cod_fil FROM pers_affil
                                      WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND courant = 'O')
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                         WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);
                END
            END
            ELSE IF @operation = 'U'
            BEGIN
                SELECT @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM, 0)
                FROM plafond_assur
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @n_num_fam
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

                SET @vsold_ectim = @vsold_ectim - ISNULL(@o_mnt_net, 0) + ISNULL(@n_mnt_net, 0);
                IF @vsold_ectim < 0
                    SET @vsold_ectim = 0;

                UPDATE plafond_assur
                SET SOLD_ASSUR_ESTIM = @vsold_ectim
                WHERE cod_soc  = @n_cod_soc
                  AND mat_pers = @n_mat_pers
                  AND num_fam  = @n_num_fam
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

                SELECT @vsold_ectim_conj = ISNULL(SOLD_ASSUR_ESTIM, 0)
                FROM plafond_assur
                WHERE cod_soc  = @Cod_Soc_Conj
                  AND mat_pers = @Mat_Pers_Conj
                  AND num_fam  = @numfamC
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @numfamC);

                SET @vsold_ectim_conj = @vsold_ectim_conj - ISNULL(@o_mnt_net_conj, 0) + ISNULL(@n_mnt_net_conj, 0);
                IF @vsold_ectim_conj < 0
                    SET @vsold_ectim_conj = 0;

                UPDATE plafond_assur
                SET SOLD_ASSUR_ESTIM = @vsold_ectim_conj
                WHERE cod_soc  = @Cod_Soc_Conj
                  AND mat_pers = @Mat_Pers_Conj
                  AND num_fam  = @numfamC
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);

                IF @v_proratat = 'O'
                BEGIN
                    SELECT @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM, 0)
                    FROM PLAFOND_ASSUR_DETAIL
                    WHERE cod_soc  = @o_cod_soc
                      AND mat_pers = @o_mat_pers
                      AND num_fam  = @n_num_fam
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM PLAFOND_ASSUR_DETAIL
                                         WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

                    SET @vsold_ectim = @vsold_ectim - ISNULL(@o_mnt_net, 0) + ISNULL(@n_mnt_net, 0);
                    IF @vsold_ectim < 0
                        SET @vsold_ectim = 0;

                    UPDATE PLAFOND_ASSUR_DETAIL
                    SET SOLD_ASSUR_ESTIM = @vsold_ectim
                    WHERE cod_soc  = @n_cod_soc
                      AND mat_pers = @n_mat_pers
                      AND num_fam  = @n_num_fam
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM PLAFOND_ASSUR_DETAIL
                                         WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

                    SELECT @vsold_ectim_conj = ISNULL(SOLD_ASSUR_ESTIM, 0)
                    FROM PLAFOND_ASSUR_DETAIL
                    WHERE cod_soc  = @Cod_Soc_Conj
                      AND mat_pers = @Mat_Pers_Conj
                      AND num_fam  = @numfamC
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM PLAFOND_ASSUR_DETAIL
                                         WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @numfamC);

                    SET @vsold_ectim_conj = @vsold_ectim_conj - ISNULL(@o_mnt_net_conj, 0) + ISNULL(@n_mnt_net_conj, 0);
                    IF @vsold_ectim_conj < 0
                        SET @vsold_ectim_conj = 0;

                    UPDATE PLAFOND_ASSUR_DETAIL
                    SET SOLD_ASSUR_ESTIM = @vsold_ectim_conj
                    WHERE cod_soc  = @Cod_Soc_Conj
                      AND mat_pers = @Mat_Pers_Conj
                      AND num_fam  = @numfamC
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM PLAFOND_ASSUR_DETAIL
                                         WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);
                END
            END
            ELSE
            BEGIN
                SELECT @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM, 0)
                FROM plafond_assur
                WHERE cod_soc  = @o_cod_soc
                  AND num_fam  = @o_num_fam
                  AND mat_pers = @o_mat_pers
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers AND num_fam = @o_num_fam);

                SELECT @vsold_ectim_conj = ISNULL(SOLD_ASSUR_ESTIM, 0)
                FROM plafond_assur
                WHERE cod_soc  = @Cod_Soc_Conj
                  AND mat_pers = @Mat_Pers_Conj
                  AND num_fam  = @numfamC
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);

                SET @vsold_ectim_conj = @vsold_ectim_conj - ISNULL(@o_mnt_net_conj, 0);
                IF @vsold_ectim_conj < 0
                    SET @vsold_ectim_conj = 0;

                UPDATE plafond_assur
                SET SOLD_ASSUR_ESTIM = @vsold_ectim_conj
                WHERE cod_soc  = @Cod_Soc_Conj
                  AND mat_pers = @Mat_Pers_Conj
                  AND num_fam  = @numfamC
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);

                SET @vsold_ectim = @vsold_ectim - ISNULL(@o_mnt_net, 0);
                IF @vsold_ectim < 0
                    SET @vsold_ectim = 0;

                UPDATE plafond_assur
                SET SOLD_ASSUR_ESTIM = @vsold_ectim
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                     WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers AND num_fam = @o_num_fam);

                IF @v_proratat = 'O'
                BEGIN
                    SELECT @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM, 0)
                    FROM plafond_assur_detail
                    WHERE cod_soc  = @o_cod_soc
                      AND mat_pers = @o_mat_pers
                      AND num_fam  = @o_num_fam
                      AND ABRV_ACT = @o_abrv_act
                      AND COD_FIL  = (SELECT cod_fil FROM pers_affil
                                      WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers AND courant = 'O')
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                         WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers AND num_fam = @o_num_fam);

                    SET @vsold_ectim = @vsold_ectim - ISNULL(@o_mnt_net, 0);
                    IF @vsold_ectim < 0
                        SET @vsold_ectim = 0;

                    UPDATE plafond_assur_detail
                    SET SOLD_ASSUR_ESTIM = @vsold_ectim
                    WHERE cod_soc  = @o_cod_soc
                      AND mat_pers = @o_mat_pers
                      AND num_fam  = @o_num_fam
                      AND ABRV_ACT = @o_abrv_act
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                         WHERE cod_soc = @o_cod_soc AND mat_pers = @o_mat_pers AND num_fam = @o_num_fam);

                    SELECT @vsold_ectim = ISNULL(SOLD_ASSUR_ESTIM, 0)
                    FROM plafond_assur_detail
                    WHERE cod_soc  = @Cod_Soc_Conj
                      AND mat_pers = @Mat_Pers_Conj
                      AND num_fam  = @numfamC
                      AND ABRV_ACT = @o_abrv_act
                      AND COD_FIL  = (SELECT cod_fil FROM pers_affil
                                      WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND courant = 'O')
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                         WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);

                    SET @vsold_ectim = @vsold_ectim - ISNULL(@o_mnt_net_conj, 0);
                    IF @vsold_ectim < 0
                        SET @vsold_ectim = 0;

                    UPDATE plafond_assur_detail
                    SET SOLD_ASSUR_ESTIM = @vsold_ectim
                    WHERE cod_soc  = @Cod_Soc_Conj
                      AND mat_pers = @Mat_Pers_Conj
                      AND num_fam  = @numfamC
                      AND ABRV_ACT = @o_abrv_act
                      AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                         WHERE cod_soc = @Cod_Soc_Conj AND mat_pers = @Mat_Pers_Conj AND num_fam = @numfamC);
                END
            END
        END

        IF @operation <> 'D'
            UPDATE lig_bult
            SET IMPUT_PLAF = @v_imput_plaf,
                CTR_DUREE  = @v_ctr_duree,
                DUREE_ACT  = @v_duree_act,
                NAT_ACT    = @v_nat_act,
                MTT_ACTE   = @v_mtt_acte,
                TAUX_ACT   = @v_taux_act,
                PLAFONNE   = @v_plafonne,
                PLAFOND    = @v_plafond,
                A_INDICE   = @v_indic
            WHERE cod_soc  = @n_cod_soc
              AND mat_pers = @n_mat_pers
              AND num_fam  = @n_num_fam
              AND dat_soin = @n_dat_soin
              AND num_soin = @n_num_soin
              AND abrv_act = @n_abrv_act;

        SET @i += 1;
    END
END
GO

IF EXISTS (SELECT 1 FROM sys.triggers WHERE name = N'trg_lig_bult' AND is_disabled = 0
                                         AND parent_id = OBJECT_ID(N'dbo.LIG_BULT'))
   AND NOT EXISTS (SELECT 1 FROM sys.triggers WHERE name IN (N'insert_lig_bult_insert', N'insert_lig_bult_update', N'insert_lig_bult_delete'))
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;
GO
