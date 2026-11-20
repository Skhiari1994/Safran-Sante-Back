USE [ramidb]
GO

/*
Table LIG_BULT_VISIT : les 3 triggers insert_visit_insert, insert_visit_update et insert_visit_delete
sont remplaces par UN SEUL trigger, dbo.trg_lig_bult_visit (AFTER INSERT, UPDATE, DELETE).

- Regles de calcul inchangees (mut_mnt_net, part conjoint, lig_bult, plafond_cnam).
- Lignes traitees une par une.
- Plus de DISABLE / ENABLE TRIGGER (cause des deadlocks) ; plafond_cnam mis a jour en une instruction atomique.
- Seul ecart volontaire : la mise a jour de mut_mnt_net / mnt_net_conj dans lig_bult_visit cible la visite elle-meme
  (cle primaire complete : cod_visit et num_lig inclus), alors que les anciens triggers modifiaient aussi les autres
  visites du meme bulletin ayant la meme date et le meme code abrege.

Retour arriere : trg_lig_bult_visit_rollback.sql
*/

BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.insert_visit_insert', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_visit_insert;
IF OBJECT_ID(N'dbo.insert_visit_update', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_visit_update;
IF OBJECT_ID(N'dbo.insert_visit_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_visit_delete;
IF OBJECT_ID(N'dbo.trg_lig_bult_visit', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_lig_bult_visit;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER dbo.trg_lig_bult_visit
ON dbo.LIG_BULT_VISIT
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    /* ------------------------------------------------------------------------------------------------
       Remplace insert_visit_insert, insert_visit_update et insert_visit_delete.

       - Les regles de calcul sont celles des 3 triggers d'origine (mut_mnt_net, conjoint, lig_bult, plafond_cnam).
       - Les lignes sont traitees UNE PAR UNE (les anciens triggers ne lisaient qu'une ligne de "inserted"/"deleted").
       - Plus aucun DISABLE / ENABLE TRIGGER (verrou de schema Sch-M => deadlocks) : la mise a jour de
         mut_mnt_net / mnt_net_conj faite ici ne relance pas ce trigger (voir la garde ci-dessous).
       - plafond_cnam est mis a jour en une seule instruction atomique (plus de lecture puis ecriture).
       ------------------------------------------------------------------------------------------------ */

    -- Garde : sans effet tant que RECURSIVE_TRIGGERS est OFF (cas actuel), protege si l'option passait a ON.
    IF TRIGGER_NESTLEVEL(@@PROCID) > 1
        RETURN;

    DECLARE @operation char(1) = CASE
        WHEN EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted) THEN 'U'
        WHEN EXISTS (SELECT 1 FROM inserted) THEN 'I'
        WHEN EXISTS (SELECT 1 FROM deleted)  THEN 'D'
    END;

    IF @operation IS NULL
        RETURN;

    /* ---------- Lignes touchees : n_* = nouvelles valeurs (inserted), o_* = anciennes valeurs (deleted) ---------- */
    DECLARE @l TABLE (
        rn              int IDENTITY(1,1) PRIMARY KEY,
        n_cod_soc       varchar(4),
        n_mat_pers      varchar(10),
        n_num_fam       int,
        n_dat_soin      date,
        n_abrv_act      varchar(4),
        n_cod_visit     varchar(10),
        n_num_lig       float,
        n_num_soin      numeric(18,0),
        n_dat_act       date,
        n_mnt_honor     numeric(15,3),
        n_mnt_net       numeric(15,3),
        n_taux_remb     numeric(15,3),
        n_prix_visit    numeric(15,3),
        o_cod_soc       varchar(4),
        o_mat_pers      varchar(10),
        o_num_fam       int,
        o_dat_soin      date,
        o_abrv_act      varchar(4),
        o_num_soin      numeric(18,0),
        o_mnt_honor     numeric(15,3),
        o_mnt_net       numeric(15,3),
        o_mut_mnt_net   numeric(15,3),
        o_indice        numeric(6),
        o_mnt_net_conj  numeric(15,3)
    );

    IF @operation = 'I'
        INSERT INTO @l (n_cod_soc, n_mat_pers, n_num_fam, n_dat_soin, n_abrv_act, n_cod_visit, n_num_lig, n_num_soin,
                        n_dat_act, n_mnt_honor, n_mnt_net, n_taux_remb, n_prix_visit)
        SELECT cod_soc, mat_pers, num_fam, dat_soin, abrv_act, cod_visit, num_lig, num_soin,
               dat_act, mnt_honor, mnt_net, taux_remb, prix_visit
        FROM inserted
        ORDER BY cod_soc, mat_pers, num_fam, dat_soin, num_soin, abrv_act, cod_visit, num_lig, dat_act;
    ELSE IF @operation = 'D'
        INSERT INTO @l (o_cod_soc, o_mat_pers, o_num_fam, o_dat_soin, o_abrv_act, o_num_soin,
                        o_mnt_honor, o_mnt_net, o_mut_mnt_net, o_indice, o_mnt_net_conj)
        SELECT cod_soc, mat_pers, num_fam, dat_soin, abrv_act, num_soin,
               mnt_honor, mnt_net, mut_mnt_net, indice, mnt_net_conj
        FROM deleted
        ORDER BY cod_soc, mat_pers, num_fam, dat_soin, num_soin, abrv_act, cod_visit, num_lig, dat_act;
    ELSE
        -- mise a jour : l'ancienne et la nouvelle version d'une ligne sont rapprochees par la cle primaire
        INSERT INTO @l (n_cod_soc, n_mat_pers, n_num_fam, n_dat_soin, n_abrv_act, n_cod_visit, n_num_lig, n_num_soin,
                        n_dat_act, n_mnt_honor, n_mnt_net, n_taux_remb, n_prix_visit,
                        o_cod_soc, o_mat_pers, o_num_fam, o_dat_soin, o_abrv_act, o_num_soin,
                        o_mnt_honor, o_mnt_net, o_mut_mnt_net, o_indice, o_mnt_net_conj)
        SELECT i.cod_soc, i.mat_pers, i.num_fam, i.dat_soin, i.abrv_act, i.cod_visit, i.num_lig, i.num_soin,
               i.dat_act, i.mnt_honor, i.mnt_net, i.taux_remb, i.prix_visit,
               d.cod_soc, d.mat_pers, d.num_fam, d.dat_soin, d.abrv_act, d.num_soin,
               d.mnt_honor, d.mnt_net, d.mut_mnt_net, d.indice, d.mnt_net_conj
        FROM inserted i
        JOIN deleted d
          ON d.cod_soc   = i.cod_soc
         AND d.mat_pers  = i.mat_pers
         AND d.num_fam   = i.num_fam
         AND d.dat_soin  = i.dat_soin
         AND d.abrv_act  = i.abrv_act
         AND d.cod_visit = i.cod_visit
         AND d.num_lig   = i.num_lig
         AND d.dat_act   = i.dat_act
         AND d.num_soin  = i.num_soin
        ORDER BY i.cod_soc, i.mat_pers, i.num_fam, i.dat_soin, i.num_soin, i.abrv_act, i.cod_visit, i.num_lig, i.dat_act;

    /* ---------- Variables de travail (memes types que dans les triggers d'origine) ---------- */
    DECLARE
        @n_cod_soc varchar(4), @n_mat_pers varchar(10), @n_num_fam int, @n_dat_soin date, @n_abrv_act varchar(4),
        @n_cod_visit varchar(10), @n_num_lig float, @n_num_soin numeric(18,0), @n_dat_act date,
        @n_mnt_honor numeric(15,3), @n_mnt_net numeric(15,3), @n_taux_remb numeric(15,3), @n_prix_visit numeric(15,3),
        @o_cod_soc varchar(4), @o_mat_pers varchar(10), @o_num_fam int, @o_dat_soin date, @o_abrv_act varchar(4),
        @o_num_soin numeric(18,0), @o_mnt_honor numeric(15,3), @o_mnt_net numeric(15,3), @o_mut_mnt_net numeric(15,3),
        @o_indice numeric(6), @o_mnt_net_conj numeric(15,3),

        @v_indic varchar(1), @v_nat_act varchar(1), @v_plafonne varchar(1), @v_taux_act numeric(5,2),
        @v_mtt_acte numeric(10,3), @v_plafond numeric(10,3),
        @w_plafond numeric(10,3), @w_sold_plaf numeric(10,3),
        @mut_mnt_net numeric(10,3), @cnam_mnt_net numeric(10,3),
        @x numeric(10,3), @v_num_lig numeric(10,3), @anc_net_mut numeric(10,3), @old_mnt_conj numeric(15,3),

        @mnt_net_conj numeric(15,3), @lig_mntNet_calcul numeric(15,3), @lig_sauv_calcul numeric(15,3),
        @bult_cum_calcul numeric(15,3), @cod_fil_conj varchar(max), @cod_assur_conj varchar(max);

    DECLARE @i int = 1, @nb int = (SELECT COUNT(*) FROM @l);

    WHILE @i <= @nb
    BEGIN
        -- Valeurs remises a NULL a chaque ligne : comme dans les triggers d'origine, un SELECT qui ne trouve rien
        -- laisse la variable a NULL (ex. aucune ligne de bareme_remb => @v_nat_act NULL => branche "non M").
        SELECT @v_indic = NULL, @v_nat_act = NULL, @v_plafonne = NULL, @v_taux_act = NULL, @v_mtt_acte = NULL,
               @v_plafond = NULL, @w_plafond = NULL, @w_sold_plaf = NULL, @mut_mnt_net = NULL, @cnam_mnt_net = NULL,
               @x = NULL, @v_num_lig = NULL, @anc_net_mut = NULL, @old_mnt_conj = NULL, @mnt_net_conj = NULL,
               @lig_mntNet_calcul = NULL, @lig_sauv_calcul = NULL, @bult_cum_calcul = NULL,
               @cod_fil_conj = NULL, @cod_assur_conj = NULL;

        SELECT @n_cod_soc = n_cod_soc, @n_mat_pers = n_mat_pers, @n_num_fam = n_num_fam, @n_dat_soin = n_dat_soin,
               @n_abrv_act = n_abrv_act, @n_cod_visit = n_cod_visit, @n_num_lig = n_num_lig, @n_num_soin = n_num_soin,
               @n_dat_act = n_dat_act, @n_mnt_honor = n_mnt_honor, @n_mnt_net = n_mnt_net,
               @n_taux_remb = n_taux_remb, @n_prix_visit = n_prix_visit,
               @o_cod_soc = o_cod_soc, @o_mat_pers = o_mat_pers, @o_num_fam = o_num_fam, @o_dat_soin = o_dat_soin,
               @o_abrv_act = o_abrv_act, @o_num_soin = o_num_soin, @o_mnt_honor = o_mnt_honor, @o_mnt_net = o_mnt_net,
               @o_mut_mnt_net = o_mut_mnt_net, @o_indice = o_indice, @o_mnt_net_conj = o_mnt_net_conj
        FROM @l
        WHERE rn = @i;

        /* =====================================================================================================
           SUPPRESSION  (ex insert_visit_delete)
           ===================================================================================================== */
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
                -- cette visite etait la seule a alimenter la ligne cumulee de lig_bult : on la supprime
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

            -- plafond CNAM : on retire le net de la visite (jamais en dessous de 0)
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
            /* =================================================================================================
               INSERTION ET MODIFICATION : calcul de la part mutuelle (mut_mnt_net) - identique dans les
               triggers d'origine insert_visit_insert et insert_visit_update
               ================================================================================================= */

            -- plafond assurance (derniere annee connue)
            SELECT @w_plafond = ISNULL(PLAFOND, 0), @w_sold_plaf = ISNULL(SOLD_ASSUR_ESTIM, 0)
            FROM plafond_assur
            WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam
              AND ANNEE_ASSUR = (SELECT MAX(ANNEE_ASSUR) FROM plafond_assur
                                 WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND num_fam = @n_num_fam);

            -- bareme de remboursement de la filiere / assurance de l'adherent pour cet acte
            SELECT @v_indic    = ISNULL(a_indice, 'N'),
                   @v_nat_act  = ISNULL(nat_act, 'M'),
                   @v_plafonne = ISNULL(plafonne, 'N'),
                   @v_taux_act = ISNULL(taux_act, 0),
                   @v_mtt_acte = ISNULL(mtt_acte, 0),
                   @v_plafond  = ISNULL(plafond, 0)
            FROM bareme_remb
            WHERE COD_FIL = (SELECT cod_fil FROM pers_affil
                             WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers AND courant = 'O')
              AND COD_ASSUR = (SELECT cod_assur FROM personnel
                               WHERE cod_soc = @n_cod_soc AND mat_pers = @n_mat_pers)
              AND ABRV_ACT = @n_abrv_act;

            IF @v_nat_act = 'M'
            BEGIN
                IF @n_mnt_honor - @n_mnt_net > @v_mtt_acte
                BEGIN
                    -- (le test "plafonne et @v_mnt_net > plafond" des triggers d'origine ne pouvait jamais etre vrai :
                    --  @v_mnt_net n'y etait jamais renseigne)
                    SET @mut_mnt_net = @v_mtt_acte;
                END
                ELSE
                BEGIN
                    SET @mut_mnt_net = @n_mnt_honor - @n_mnt_net;
                    IF @v_plafonne = 'O' AND @mut_mnt_net > @v_plafond
                        SET @mut_mnt_net = @v_plafond;
                END
            END
            ELSE
            BEGIN
                SET @cnam_mnt_net = CAST(@n_prix_visit AS float) * (CAST(@n_taux_remb AS float) / 100);

                IF @n_mnt_honor = @cnam_mnt_net
                BEGIN
                    SET @mut_mnt_net = 0;
                END
                ELSE
                BEGIN
                    IF @n_mnt_honor >= @n_prix_visit
                    BEGIN
                        SET @mut_mnt_net = CAST(@n_prix_visit AS float) * CAST(@v_taux_act AS float) / 100;
                        IF @v_plafonne = 'O' AND @mut_mnt_net > @v_plafond
                            SET @mut_mnt_net = @v_plafond;
                    END
                    ELSE
                    BEGIN
                        SET @mut_mnt_net = CAST(@n_mnt_honor AS float) * CAST(@v_taux_act AS float) / 100;
                        IF @v_plafonne = 'O' AND @mut_mnt_net > @v_plafond
                            SET @mut_mnt_net = @v_plafond;
                    END
                END
            END

            IF @mut_mnt_net + @n_mnt_net > @n_mnt_honor
                SET @mut_mnt_net = @n_mnt_honor - @n_mnt_net;

            IF @mut_mnt_net + @w_sold_plaf > @w_plafond
                SET @mut_mnt_net = @w_plafond - @w_sold_plaf;

            IF @mut_mnt_net < 0
                SET @mut_mnt_net = 0;

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
            END

            -- part du conjoint (procedure dbo.calcul_mntNetConj, inchangee)
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

            SET @mnt_net_conj = @n_mnt_honor - @n_mnt_net - @mut_mnt_net;

            EXEC [dbo].[calcul_mntNetConj]
                 @soc            = @n_cod_soc,
                 @mat            = @n_mat_pers,
                 @cod_fil        = @cod_fil_conj,
                 @cod_assur      = @cod_assur_conj,
                 @numfam         = @n_num_fam,
                 @datsoin        = @n_dat_soin,
                 @sauv_net       = 0,
                 @cumul_net      = 0,
                 @ACT_vmnt_honor = @mnt_net_conj,
                 @ACT_indice     = NULL,
                 @ACT_ABRV_ACT   = @n_abrv_act,
                 @lig_mntNetConj = @lig_mntNet_calcul OUTPUT,
                 @lig_sauv       = @lig_sauv_calcul OUTPUT,
                 @bult_cum       = @bult_cum_calcul OUTPUT;

            IF @operation = 'I'
            BEGIN
                /* =========================================================================================
                   INSERTION  (ex insert_visit_insert)
                   ========================================================================================= */
                IF @x = 0
                BEGIN
                    SELECT @v_num_lig = ISNULL(MAX(ISNULL(num_lig, 0)), 0) + 1
                    FROM lig_bult
                    WHERE cod_soc  = @n_cod_soc
                      AND mat_pers = @n_mat_pers
                      AND num_fam  = @n_num_fam
                      AND num_soin = @n_num_soin
                      AND dat_soin = @n_dat_soin;

                    -- comme dans le trigger d'origine, une erreur d'insertion dans lig_bult n'interrompt pas l'enregistrement
                    BEGIN TRY
                        INSERT INTO lig_bult (cod_soc, mat_pers, num_fam, dat_soin, abrv_act, num_lig, mnt_honor,
                                              mnt_net, dat_act, indice, num_soin, MNT_NET_CONJ)
                        VALUES (@n_cod_soc, @n_mat_pers, @n_num_fam, @n_dat_soin, @n_abrv_act, @v_num_lig,
                                @n_mnt_honor, @mut_mnt_net, @n_dat_act, 1, @n_num_soin, @lig_mntNet_calcul);
                    END TRY
                    BEGIN CATCH
                    END CATCH
                END
                ELSE
                BEGIN
                    UPDATE lig_bult
                    SET mnt_honor    = ISNULL(mnt_honor, 0) + @n_mnt_honor,
                        mnt_net      = ISNULL(mnt_net, 0) + @mut_mnt_net,
                        indice       = indice + 1,
                        MNT_NET_CONJ = ISNULL(MNT_NET_CONJ, 0) + @lig_mntNet_calcul,
                        DAT_ACT      = @n_dat_act
                    WHERE cod_soc  = @n_cod_soc
                      AND mat_pers = @n_mat_pers
                      AND num_fam  = @n_num_fam
                      AND num_soin = @n_num_soin
                      AND dat_soin = @n_dat_soin
                      AND abrv_act = @n_abrv_act;
                END

                -- on garde dans la visite le montant mutuelle et la part conjoint calcules
                UPDATE lig_bult_visit
                SET mut_mnt_net  = @mut_mnt_net,
                    mnt_net_conj = @lig_mntNet_calcul
                WHERE cod_soc   = @n_cod_soc
                  AND mat_pers  = @n_mat_pers
                  AND num_fam   = @n_num_fam
                  AND dat_soin  = @n_dat_soin
                  AND num_soin  = @n_num_soin
                  AND abrv_act  = @n_abrv_act
                  AND dat_act   = @n_dat_act
                  AND cod_visit = @n_cod_visit
                  AND num_lig   = @n_num_lig;

                -- plafond CNAM : on ajoute le net de la visite
                UPDATE plafond_cnam
                SET sold_cnam_estim = ISNULL(sold_cnam_estim, 0) + ISNULL(@n_mnt_net, 0)
                WHERE cod_soc    = @n_cod_soc
                  AND mat_pers   = @n_mat_pers
                  AND annee_cnam = YEAR(@n_dat_soin);
            END
            ELSE
            BEGIN
                /* =========================================================================================
                   MODIFICATION  (ex insert_visit_update)
                   ========================================================================================= */
                SELECT @old_mnt_conj = MNT_NET_CONJ
                FROM lig_bult_visit
                WHERE cod_soc   = @o_cod_soc
                  AND mat_pers  = @o_mat_pers
                  AND num_fam   = @o_num_fam
                  AND dat_soin  = @o_dat_soin
                  AND num_soin  = @o_num_soin
                  AND abrv_act  = @o_abrv_act
                  AND dat_act   = @n_dat_act
                  AND cod_visit = @n_cod_visit
                  AND num_lig   = @n_num_lig;

                UPDATE lig_bult
                SET mnt_honor    = ISNULL(mnt_honor, 0) + @n_mnt_honor - ISNULL(@o_mnt_honor, 0),
                    mnt_net      = ISNULL(mnt_net, 0) + ISNULL(@mut_mnt_net, 0) - ISNULL(@o_mut_mnt_net, 0),
                    MNT_NET_CONJ = ISNULL(MNT_NET_CONJ, 0) + @lig_mntNet_calcul - @old_mnt_conj,
                    DAT_ACT      = @n_dat_act
                WHERE cod_soc  = @o_cod_soc
                  AND mat_pers = @o_mat_pers
                  AND num_fam  = @o_num_fam
                  AND dat_soin = @o_dat_soin
                  AND num_soin = @o_num_soin
                  AND abrv_act = @o_abrv_act;

                UPDATE lig_bult_visit
                SET mut_mnt_net  = ISNULL(@mut_mnt_net, 0),
                    mnt_net_conj = @lig_mntNet_calcul
                WHERE cod_soc   = @o_cod_soc
                  AND mat_pers  = @o_mat_pers
                  AND num_fam   = @o_num_fam
                  AND dat_soin  = @o_dat_soin
                  AND num_soin  = @o_num_soin
                  AND abrv_act  = @n_abrv_act
                  AND dat_act   = @n_dat_act
                  AND cod_visit = @n_cod_visit
                  AND num_lig   = @n_num_lig;

                -- plafond CNAM : on remplace l'ancien net par le nouveau (jamais en dessous de 0)
                UPDATE plafond_cnam
                SET sold_cnam_estim = CASE WHEN ISNULL(sold_cnam_estim, 0) - ISNULL(@o_mnt_net, 0) + ISNULL(@n_mnt_net, 0) < 0
                                           THEN 0
                                           ELSE ISNULL(sold_cnam_estim, 0) - ISNULL(@o_mnt_net, 0) + ISNULL(@n_mnt_net, 0) END
                WHERE cod_soc    = @o_cod_soc
                  AND mat_pers   = @o_mat_pers
                  AND annee_cnam = YEAR(@o_dat_soin);
            END
        END

        SET @i += 1;
    END
END
GO

IF EXISTS (SELECT 1 FROM sys.triggers WHERE name = N'trg_lig_bult_visit' AND is_disabled = 0
                                         AND parent_id = OBJECT_ID(N'dbo.LIG_BULT_VISIT'))
   AND NOT EXISTS (SELECT 1 FROM sys.triggers WHERE name IN (N'insert_visit_insert', N'insert_visit_update', N'insert_visit_delete'))
    COMMIT TRANSACTION;
ELSE
    ROLLBACK TRANSACTION;
GO
