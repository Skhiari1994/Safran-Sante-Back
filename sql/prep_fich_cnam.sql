USE [ramidb]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[prep_fich_cnam](
    @codSoc         varchar(MAX),
    @codBord        varchar(MAX),
    @returnString   varbinary(Max) output,
    @returnString2  varbinary(Max) output)
AS
BEGIN
/*
    Prépare le fichier texte CNAM d'un bordereau d'envoi.

    Entrées  : @codSoc, @codBord = société et code du bordereau.
    Sorties  : @returnString et @returnString2 = le texte du fichier, coupé en deux moitiés (varbinary).

    Chaque ligne du fichier commence par son type :
        1 = bordereau     2 = bulletin de soins
        3 = visite        4 = médicament        5 = acte        6 = appareil

    Imbrication des curseurs :
        bord                      bordereau demandé                        -> ligne 1
          bult                    bulletins du bordereau                   -> ligne 2
            visits / med / acts / apps    lignes de soins du bulletin      -> lignes 3 / 4 / 5 / 6

    Remplissage des champs (sans fonction externe) :
        compléter à gauche   REPLICATE(c, n - LEN(x)) + x   ex. zéros devant un nombre
        compléter à droite   x + REPLICATE(c, n - LEN(x))   ex. espaces après un texte
    Le champ vaut NULL si x est NULL ou dépasse n caractères ; ISNULL(...) donne alors la valeur de remplacement
    (espaces ou zéros). Même comportement que les fonctions dbo.LPAD / dbo.RPAD qu'elles remplacent.
*/

    /* ================================================================== */
    /*  VARIABLES                                                         */
    /* ================================================================== */
    DECLARE
        @pecCnam         varchar(1),        -- prise en charge CNAM de la ligne de soins ('N' = exclue du fichier)
        -- texte du fichier et lignes en cours de construction
        @ligne_bord      varchar(MAX) = '',
        @ligne_bord1     varchar(MAX) = '',
        @ligne_bord2     varchar(MAX) = '',
        @ligne_bult      varchar(MAX) = '',
        @ligne_visit     varchar(MAX) = '',
        @ligne_act       varchar(MAX) = '',
        @ligne_med       varchar(MAX) = '',
        @ligne_app       varchar(MAX) = '',
        -- régime de retraite : société (v_) et adhérent (p_)
        @v_cod_retr      varchar(1),
        @p_cod_retr      varchar(1),
        @v_num_retr      varchar(10),
        @p_num_retr      varchar(10),
        -- bénéficiaire du bulletin
        @lien_par        varchar(1),
        @nom_prenom      varchar(150),
        @rang_m          varchar(2),
        @p_DAT_NAIS      varchar(10),
        @vclef           varchar(2),        -- clé de l'établissement (REF_ETABLIS)
        -- bordereau en cours
        @bord_cod_soc    varchar(MAX),
        @bord_cod_bord   varchar(MAX),
        @bord_cod_b      varchar(MAX),
        @bord_DAT_BORD   varchar(MAX),
        @bord_nbr_b      varchar(MAX),
        @bord_tot_h      varchar(MAX),
        -- valeurs de remplacement quand une donnée est vide
        @espace_8        varchar(8)   = '        ',
        @espace_20       varchar(20)  = '                    ',
        @espace_5        varchar(5)   = '     ',
        @espace_12       varchar(12)  = '            ',
        -- saut de ligne à la fin de chaque ligne du fichier
        @entree          varchar(MAX) = '
'

    /* ================================================================== */
    /*  CURSEUR 1/6 : bord                                                */
    /*  Le bordereau demandé (société @codSoc, code @codBord).            */
    /*  Formate le code (20 car.), le nombre de bulletins (5 car.) et le  */
    /*  total des honoraires (12 car.) de la ligne de type 1.             */
    /* ================================================================== */
    DECLARE bord CURSOR
    FOR
        SELECT  cod_soc,
                cod_bord,
                REPLICATE(' ', 20 - LEN(CAST(cod_bord AS varchar(MAX)))) + CAST(cod_bord AS varchar(MAX))   AS cod_b,
                DAT_BORD,
                REPLICATE('0', 5 - LEN(CAST(NBR_BULT AS varchar(MAX)))) + CAST(NBR_BULT AS varchar(MAX))   AS nbr_b,
                REPLICATE('0', 12 - LEN(CAST(replace(TOT_HONOR, '.', '') AS varchar(MAX))))
                    + CAST(replace(TOT_HONOR, '.', '') AS varchar(MAX)) AS tot_h
        FROM    bord_envoi
        WHERE   cod_soc  = @codSoc
        AND     cod_bord = @codBord;

    OPEN bord
    FETCH NEXT FROM bord INTO
        @bord_cod_soc,
        @bord_cod_bord,
        @bord_cod_b,
        @bord_DAT_BORD,
        @bord_nbr_b,
        @bord_tot_h

    WHILE @@FETCH_STATUS = 0
    BEGIN

        /* ---------- Ligne 1 : bordereau ---------- */
        -- régime et numéro de retraite de la société
        SELECT
            @v_cod_retr = (SELECT CASE WHEN COD_RETR = 'C' THEN 1 ELSE 2 END AS LN),
            @v_num_retr = REPLICATE('0', 8 - LEN(CAST(num_retr AS varchar(MAX)))) + CAST(num_retr AS varchar(MAX))
        FROM    societe
        WHERE   cod_soc = @bord_cod_soc;

        SET @ligne_bord = '1' + ISNULL(@v_cod_retr, ' ')
                        + ISNULL(@v_num_retr, @espace_8)
                        + convert(varchar, getdate(), 112)
                        + ISNULL(@bord_cod_b, @espace_20)
                        + ISNULL(@bord_nbr_b, @espace_5)
                        + ISNULL(@bord_tot_h, @espace_12) + @entree;

        /* ---------- Variables du bulletin ---------- */
        DECLARE
            @bult_COD_SOC   varchar(MAX),
            @bult_MAT_PERS  varchar(MAX),
            @bult_NUM_FAM   varchar(MAX),
            @bult_dat_soin  varchar(MAX),
            @bult_num_soin  varchar(MAX),
            @bult_dat_s     varchar(MAX),
            @bult_tot_h     varchar(MAX),
            @bult_cod_m     varchar(MAX),
            @bult_reg_r     varchar(MAX),
            @bult_num_p     varchar(MAX),
            @bult_ord       varchar(MAX),
            @bult_num_s     varchar(MAX)

        /* ============================================================== */
        /*  CURSEUR 2/6 : bult                                            */
        /*  Les bulletins de soins du bordereau, dans l'ordre ORD_BULT.   */
        /*  Formate le total des honoraires (10 car.), le code maladie    */
        /*  (4 car.), le n° de prise en charge (12 car.), l'ordre du      */
        /*  bulletin (5 car.) et le n° de soin (20 car.).                 */
        /* ============================================================== */
        DECLARE bult CURSOR
        FOR
            SELECT  COD_SOC,
                    MAT_PERS,
                    NUM_FAM,
                    dat_soin,
                    num_soin,
                    convert(varchar, DAT_SOIN, 112)                                    dat_s,
                    REPLICATE('0', 10 - LEN(CAST(replace(TOT_HONOR, '.', '') AS varchar(MAX))))
                        + CAST(replace(TOT_HONOR, '.', '') AS varchar(MAX))   tot_h,
                    ISNULL(REPLICATE('0', 4 - LEN(CAST(COD_MALAD AS varchar(MAX)))) + CAST(COD_MALAD AS varchar(MAX)), '0000')   cod_m,
                    REG_REMB                                                           reg_r,
                    ISNULL(REPLICATE('0', 12 - LEN(CAST(NUM_PEC AS varchar(MAX)))) + CAST(NUM_PEC AS varchar(MAX)), '000000000000')   num_p,
                    REPLICATE('0', 5 - LEN(CAST(ORD_BULT AS varchar(MAX))))
                        + CAST(ORD_BULT AS varchar(MAX))   ord,
                    ISNULL(CAST(num_soin AS varchar(MAX)) + REPLICATE(' ', 20 - LEN(CAST(num_soin AS varchar(MAX)))), '                    ')   num_s
            FROM    bult_soin
            WHERE   cod_soc  = @codSoc
            AND     cod_bord = @codBord
            ORDER BY ORD_BULT;

        OPEN bult
        FETCH NEXT FROM bult INTO
            @bult_COD_SOC,
            @bult_MAT_PERS,
            @bult_NUM_FAM,
            @bult_dat_soin,
            @bult_num_soin,
            @bult_dat_s,
            @bult_tot_h,
            @bult_cod_m,
            @bult_reg_r,
            @bult_num_p,
            @bult_ord,
            @bult_num_s

        WHILE @@FETCH_STATUS = 0
        BEGIN

            /* ---------- Ligne 2 : bulletin ---------- */
            -- régime et numéro de retraite de l'adhérent
            -- numéro sur 10 caractères, d'après sa longueur sans les zéros de tête :
            --   10 -> tel quel ; 9 -> un zéro devant ; autre -> complété à 8 chiffres à gauche, puis zéros à droite jusqu'à 10
            SELECT
                @p_cod_retr = cod_retr,
                @p_num_retr =
                    CASE LEN(dbo.LTRIM_STR(num_retr, '0'))
                        WHEN 10 THEN num_retr
                        WHEN 9   THEN REPLICATE('0', 10 - LEN(dbo.LTRIM_STR(num_retr, '0'))) + dbo.LTRIM_STR(num_retr, '0')
                        ELSE REPLICATE('0', 8 - LEN(dbo.LTRIM_STR(num_retr, '0'))) + dbo.LTRIM_STR(num_retr, '0')
                            + REPLICATE('0', 10 - LEN(REPLICATE('0', 8 - LEN(dbo.LTRIM_STR(num_retr, '0'))) + dbo.LTRIM_STR(num_retr, '0')))
                    END
            FROM    personnel
            WHERE   cod_soc  = @bult_COD_SOC
            AND     mat_pers = @bult_MAT_PERS;

            -- lien de parenté
            SELECT
                @lien_par = CASE @bult_NUM_FAM
                                WHEN 0  THEN '0'
                                WHEN 99 THEN '1'
                                WHEN 97 THEN '3'
                                WHEN 98 THEN '4'
                                ELSE 2
                            END;

            -- nom, prénom et date de naissance du bénéficiaire :
            -- l'adhérent lui-même (NUM_FAM = 0) ou un membre de sa famille
            IF @bult_NUM_FAM = 0
            BEGIN
                SELECT
                    @nom_prenom = CAST(SUBSTRING(nom_pers + ' ' + pren_pers, 1, 60) AS varchar(MAX))
                        + REPLICATE(' ', 60 - LEN(CAST(SUBSTRING(nom_pers + ' ' + pren_pers, 1, 60) AS varchar(MAX)))),
                    @p_DAT_NAIS = ISNULL(convert(varchar, DAT_NAIS, 112), '        ')
                FROM    personnel
                WHERE   cod_soc  = @bult_cod_soc
                AND     mat_pers = @bult_mat_pers;
            END
            ELSE
            BEGIN
                SELECT
                    @nom_prenom = CAST(SUBSTRING(nom_pers + ' ' + NOM_PREN, 1, 60) AS varchar(MAX))
                        + REPLICATE(' ', 60 - LEN(CAST(SUBSTRING(nom_pers + ' ' + NOM_PREN, 1, 60) AS varchar(MAX)))),
                    @p_DAT_NAIS = ISNULL(convert(varchar, DAT_NAISS, 112), '        ')
                FROM    famille f,
                        personnel p
                WHERE   p.cod_soc  = @bult_COD_SOC
                AND     p.mat_pers = @bult_MAT_PERS
                AND     f.cod_soc  = p.cod_soc
                AND     f.mat_pers = p.mat_pers
                AND     f.num_fam  = @bult_NUM_FAM;
            END;

            SET @nom_prenom = LEFT(@nom_prenom, 60);

            -- rang du bénéficiaire
            SELECT
                @rang_m = REPLICATE('0', 2 - LEN(CAST(CASE @bult_NUM_FAM WHEN 0 THEN '0' WHEN 99 THEN '0' WHEN 97 THEN '0' WHEN 98 THEN '4' ELSE @bult_NUM_FAM END AS varchar(MAX))))
                    + CAST(CASE @bult_NUM_FAM WHEN 0 THEN '0' WHEN 99 THEN '0' WHEN 97 THEN '0' WHEN 98 THEN '4' ELSE @bult_NUM_FAM END AS varchar(MAX));

            SET @ligne_bult = '2'
                            + ISNULL(@bult_ord, '     ')
                            + ISNULL(@p_cod_retr, ' ')
                            + ISNULL(@p_num_retr, '          ')
                            + ISNULL(@lien_par, ' ')
                            + ISNULL(@nom_prenom, '                                                            ')
                            + ISNULL(@rang_m, '  ')
                            + ISNULL(@p_DAT_NAIS, '        ')
                            + ISNULL(@bult_dat_s, '        ')
                            + ISNULL(@bult_tot_h, '          ')
                            + ISNULL(@bult_cod_m, '    ')
                            + @bult_num_s
                            + ISNULL(@bult_reg_r, ' ')
                            + @bult_num_p + @entree;

            SET @ligne_bord = @ligne_bord + @ligne_bult

            /* ========================================================== */
            /*  VISITES  (lignes de type 3)                               */
            /* ========================================================== */
            DECLARE
                @visit_PRF_TYP1  varchar(MAX),
                @visit_PRF_COD   varchar(MAX),
                @visit_PRF_TYP2  varchar(MAX),
                @visit_cod_etab  varchar(MAX),
                @visit_m_h       varchar(MAX),
                @visit_d_act     varchar(MAX),
                @COD_VISIT       varchar(max),
                @visit_c_visite  varchar(MAX),
                @ligne_visite    varchar(MAX);

            /* ---------------------------------------------------------- */
            /*  CURSEUR 3/6 : visits                                      */
            /*  Les visites du bulletin (table lig_bult_visit).           */
            /*  Formate le code établissement (type 2 car. + code 8 car.),*/
            /*  les honoraires (10 car.), la date de l'acte et le code    */
            /*  de la visite (5 car.).                                    */
            /* ---------------------------------------------------------- */
            DECLARE visits CURSOR
            FOR
                SELECT  PRF_TYP,
                        PRF_COD,
                        REPLICATE('0', 2 - LEN(CAST(PRF_TYP AS varchar(MAX)))) + CAST(PRF_TYP AS varchar(MAX))
                            + REPLICATE('0', 8 - LEN(CAST(PRF_COD AS varchar(MAX)))) + CAST(PRF_COD AS varchar(MAX))   cod_etab,
                        REPLICATE('0', 10 - LEN(CAST((mnt_honor * 1000) AS varchar(MAX))))
                            + CAST((mnt_honor * 1000) AS varchar(MAX))   m_h,
                        convert(varchar, dat_act, 112)                         d_act,
                        COD_VISIT,
                        CAST(COD_VISIT AS varchar(MAX))
                            + REPLICATE(' ', 5 - LEN(CAST(COD_VISIT AS varchar(MAX))))   c_visite
                FROM    lig_bult_visit
                WHERE   cod_soc  = @bult_COD_SOC
                AND     mat_pers = @bult_mat_pers
                AND     num_fam  = @bult_NUM_FAM
                AND     dat_soin = @bult_dat_s
                AND     num_soin = @bult_num_soin

            OPEN visits
            FETCH NEXT FROM visits INTO
                @visit_PRF_TYP1,
                @visit_PRF_COD,
                @visit_cod_etab,
                @visit_m_h,
                @visit_d_act,
                @COD_VISIT,
                @visit_c_visite

            WHILE @@FETCH_STATUS = 0
            BEGIN
                -- la visite n'est écrite que si elle est prise en charge par la CNAM
                SELECT  @pecCnam = isnull(pec_cnam, 'O')
                FROM    REF_VISIT
                WHERE   COD_VISIT = @COD_VISIT

                IF @pecCnam <> 'N'
                BEGIN
                    SELECT   @vclef = REPLICATE('0', 2 - LEN(CAST(PRF_CLE AS varchar(MAX)))) + CAST(PRF_CLE AS varchar(MAX))
                    FROM    ref_etablis
                    WHERE   PRF_TYP = @visit_PRF_TYP1
                    AND     PRF_COD = @visit_PRF_COD;

                    SET @ligne_visit = '3'
                                     + ISNULL(@bult_ord, '     ')
                                     + ISNULL(@visit_cod_etab, '          ')
                                     + ISNULL(@vclef, '  ')
                                     + ISNULL(@visit_m_h, '          ')
                                     + ISNULL(@visit_d_act, '        ')
                                     + ISNULL(@visit_c_visite, '     ') + @entree;

                    SET @ligne_bord = @ligne_bord + @ligne_visit
                END

                FETCH NEXT FROM visits INTO
                    @visit_PRF_TYP1,
                    @visit_PRF_COD,
                    @visit_cod_etab,
                    @visit_m_h,
                    @visit_d_act,
                    @COD_VISIT,
                    @visit_c_visite
            END
            CLOSE visits
            DEALLOCATE visits

            /* ========================================================== */
            /*  MÉDICAMENTS  (lignes de type 4)                           */
            /* ========================================================== */
            DECLARE
                @med_PRF_TYP    varchar(MAX),   --1
                @med_PRF_COD    varchar(MAX),   --2
                @med_c_etab     varchar(MAX),   --3
                @med_m_h_m      varchar(MAX),   --4
                @med_d_med      varchar(MAX),   --5
                @med_c_med      varchar(MAX),   --6
                @med_indi       varchar(MAX),   --7
                @med_nbr_jour   varchar(MAX),   --8
                @med_acc        varchar(MAX),   --9
                @med_n_pec      varchar(MAX),   --10
                @COD_med        varchar(max)    --11

            /* ---------------------------------------------------------- */
            /*  CURSEUR 4/6 : med                                         */
            /*  Les médicaments du bulletin (table lig_bult_med),         */
            /*  dans l'ordre num_lig.                                     */
            /*  Formate le code établissement, les honoraires (10 car.),  */
            /*  le code médicament (8 car.), l'indice (4 car.), le nombre */
            /*  de jours (3 car.), l'accord et le n° de prise en charge   */
            /*  (12 car.).                                                */
            /* ---------------------------------------------------------- */
            DECLARE med CURSOR
            FOR
                SELECT  PRF_TYP,                                                        --1
                        PRF_COD,                                                        --2
                        REPLICATE('0', 2 - LEN(CAST(PRF_TYP AS varchar(MAX)))) + CAST(PRF_TYP AS varchar(MAX))
                            + REPLICATE('0', 8 - LEN(CAST(PRF_COD AS varchar(MAX)))) + CAST(PRF_COD AS varchar(MAX))   c_etab,   --3
                        REPLICATE('0', 10 - LEN(CAST((mnt_honor * 1000) AS varchar(MAX))))
                            + CAST((mnt_honor * 1000) AS varchar(MAX))   m_h_m,   --4
                        convert(varchar, dat_act, 112)                         d_med,   --5
                        COD_med,                                                        --11
                        REPLICATE('0', 8 - LEN(CAST(COD_med AS varchar(MAX))))
                            + CAST(COD_med AS varchar(MAX))   c_med,   --6
                        REPLICATE('0', 4 - LEN(CAST(indice AS varchar(MAX))))
                            + CAST(indice AS varchar(MAX))   indi,   --7
                        ISNULL(REPLICATE('0', 3 - LEN(CAST(nbr_j AS varchar(MAX)))) + CAST(nbr_j AS varchar(MAX)), '    ')   nbr_jour,--8
                        ISNULL(accord_med, 'N')                                acc,     --9
                        REPLICATE('0', 12 - LEN(CAST(num_pec_med AS varchar(MAX))))
                            + CAST(num_pec_med AS varchar(MAX))   n_pec   --10
                FROM    lig_bult_med
                WHERE   cod_soc  = @bult_COD_SOC
                AND     mat_pers = @bult_mat_pers
                AND     num_fam  = @bult_NUM_FAM
                AND     dat_soin = @bult_dat_s
                AND     num_soin = @bult_num_soin
                ORDER BY num_lig

            OPEN med
            FETCH NEXT FROM med INTO
                @med_PRF_TYP,   --1
                @med_PRF_COD,   --2
                @med_c_etab,    --3
                @med_m_h_m,     --4
                @med_d_med,     --5
                @COD_med,       --11
                @med_c_med,     --6
                @med_indi,      --7
                @med_nbr_jour,  --8
                @med_acc,       --9
                @med_n_pec      --10

            WHILE @@FETCH_STATUS = 0
            BEGIN
                -- le médicament n'est écrit que s'il est pris en charge par la CNAM
                SELECT  @pecCnam = isnull(pec_cnam, 'O')
                FROM    REF_MED
                WHERE   COD_MED = @COD_med

                IF @pecCnam <> 'N'
                BEGIN
                    SELECT   @vclef = REPLICATE('0', 2 - LEN(CAST(PRF_CLE AS varchar(MAX)))) + CAST(PRF_CLE AS varchar(MAX))
                    FROM    ref_etablis
                    WHERE   PRF_TYP = @med_PRF_TYP
                    AND     PRF_COD = @med_PRF_COD;

                    SET @ligne_med = '4'
                                   + ISNULL(@bult_ord, '     ')
                                   + ISNULL(@med_c_etab, '          ')
                                   + ISNULL(@vclef, '  ')
                                   + ISNULL(@med_c_med, '        ')
                                   + ISNULL(@med_d_med, '        ')
                                   + ISNULL(@med_indi, '    ')
                                   + ISNULL(@med_m_h_m, '          ')
                                   + ISNULL(@med_nbr_jour, '   ')
                                   + ISNULL(@med_acc, ' ')
                                   + ISNULL(@med_n_pec, '000000000000')
                                   + @entree;

                    SET @ligne_bord = @ligne_bord + @ligne_med
                END

                FETCH NEXT FROM med INTO
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
            END
            CLOSE med
            DEALLOCATE med

            /* ========================================================== */
            /*  ACTES  (lignes de type 5)                                 */
            /* ========================================================== */
            DECLARE
                @act_PRF_TYP    varchar(MAX),
                @act_PRF_COD    varchar(MAX),
                @act_a_etab     varchar(MAX),
                @act_m_h_a      varchar(MAX),
                @act_d_act      varchar(MAX),
                @COD_act        varchar(MAX),
                @act_c_act      varchar(MAX),
                @act_a_acc      varchar(MAX),
                @act_a_n_pec    varchar(MAX),
                @act_let        varchar(MAX),
                @act_cot        varchar(MAX),
                @act_typ_prof   varchar(MAX)

            /* ---------------------------------------------------------- */
            /*  CURSEUR 5/6 : acts                                        */
            /*  Les actes du bulletin (table lig_bult_act),               */
            /*  dans l'ordre num_lig.                                     */
            /*  Formate le code établissement, les honoraires (10 car.),  */
            /*  le code acte (10 car.), la lettre clé (10 car.), le       */
            /*  coefficient (4 car.), l'accord et le n° de prise en       */
            /*  charge (12 car.).                                         */
            /* ---------------------------------------------------------- */
            DECLARE acts CURSOR
            FOR
                SELECT  PRF_TYP,
                        PRF_COD,
                        REPLICATE('0', 2 - LEN(CAST(PRF_TYP AS varchar(MAX)))) + CAST(PRF_TYP AS varchar(MAX))
                            + REPLICATE('0', 8 - LEN(CAST(PRF_COD AS varchar(MAX)))) + CAST(PRF_COD AS varchar(MAX))   a_etab,
                        REPLICATE('0', 10 - LEN(CAST((mnt_honor * 1000) AS varchar(MAX))))
                            + CAST((mnt_honor * 1000) AS varchar(MAX))   m_h_a,
                        convert(varchar, dat_act, 112)                         d_act,
                        COD_act,
                        REPLICATE(' ', 10 - LEN(CAST(COD_act AS varchar(MAX))))
                            + CAST(COD_act AS varchar(MAX))   c_act,
                        ISNULL(accord_act, 'N')                                a_acc,
                        REPLICATE('0', 12 - LEN(CAST(NUM_PEC_ACT AS varchar(MAX))))
                            + CAST(NUM_PEC_ACT AS varchar(MAX))   a_n_pec,
                        ISNULL(CAST(LET_COD AS varchar(MAX)) + REPLICATE(' ', 10 - LEN(CAST(LET_COD AS varchar(MAX)))), '          ')   let,
                        ISNULL(REPLICATE('0', 4 - LEN(CAST(COT_ACT AS varchar(MAX)))) + CAST(COT_ACT AS varchar(MAX)), '    ')   cot,
                        ISNULL(CASE TYP_PRF
                                   WHEN 'P' THEN ' '
                                   ELSE TYP_PRF
                               END, ' ')                                       typ_prof
                FROM    lig_bult_act
                WHERE   cod_soc  = @bult_COD_SOC
                AND     mat_pers = @bult_mat_pers
                AND     num_fam  = @bult_NUM_FAM
                AND     dat_soin = @bult_dat_s
                AND     num_soin = @bult_num_soin
                ORDER BY num_lig

            OPEN acts
            FETCH NEXT FROM acts INTO
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

            WHILE @@FETCH_STATUS = 0
            BEGIN
                -- l'acte n'est écrit que s'il est pris en charge par la CNAM
                SELECT  @pecCnam = isnull(pec_cnam, 'O')
                FROM    REF_ACT
                WHERE   COD_ACT = @COD_act

                IF @pecCnam <> 'N'
                BEGIN
                    SELECT   @vclef = REPLICATE('0', 2 - LEN(CAST(PRF_CLE AS varchar(MAX)))) + CAST(PRF_CLE AS varchar(MAX))
                    FROM    ref_etablis
                    WHERE   PRF_TYP = @act_PRF_TYP
                    AND     PRF_COD = @act_PRF_COD

                    SET @ligne_act = '5'
                                   + ISNULL(@bult_ord, '     ')
                                   + ISNULL(@act_a_etab, '          ')
                                   + ISNULL(@vclef, '  ')
                                   + 'M'
                                   + ISNULL(@act_c_act, '          ')
                                   + ISNULL(@act_let, '          ')
                                   + ISNULL(@act_cot, '    ')
                                   + ISNULL(@act_m_h_a, '          ')
                                   + ISNULL(@act_d_act, '        ')
                                   + ISNULL(@act_a_acc, ' ')
                                   + ISNULL(@act_a_n_pec, '000000000000') + @entree

                    SET @ligne_bord = @ligne_bord + @ligne_act
                END

                FETCH NEXT FROM acts INTO
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
            END
            CLOSE acts
            DEALLOCATE acts

            /* ========================================================== */
            /*  APPAREILS  (lignes de type 6)                             */
            /* ========================================================== */
            DECLARE
                @app_PRF_TYP    varchar(MAX),
                @app_PRF_COD    varchar(MAX),
                @app_p_etab     varchar(MAX),
                @app_m_h_p      varchar(MAX),
                @app_d_app      varchar(MAX),
                @COD_APP        varchar(max),
                @app_c_app      varchar(MAX),
                @app_a_app      varchar(MAX),
                @app_p_n_pec    varchar(MAX)

            /* ---------------------------------------------------------- */
            /*  CURSEUR 6/6 : apps                                        */
            /*  Les appareils du bulletin (table lig_bult_app).           */
            /*  Formate le code établissement, les honoraires (10 car.),  */
            /*  le code appareil (10 car.), l'accord et le n° de prise en */
            /*  charge (12 car.).                                         */
            /* ---------------------------------------------------------- */
            DECLARE apps CURSOR
            FOR
                SELECT  PRF_TYP,
                        PRF_COD,
                        REPLICATE('0', 2 - LEN(CAST(PRF_TYP AS varchar(MAX)))) + CAST(PRF_TYP AS varchar(MAX))
                            + REPLICATE('0', 8 - LEN(CAST(PRF_COD AS varchar(MAX)))) + CAST(PRF_COD AS varchar(MAX))   p_etab,
                        REPLICATE('0', 10 - LEN(CAST((mnt_honor * 1000) AS varchar(MAX))))
                            + CAST((mnt_honor * 1000) AS varchar(MAX))   m_h_p,
                        convert(varchar, dat_act, 112)                         d_app,
                        COD_APP,
                        CAST(COD_APP AS varchar(MAX))
                            + REPLICATE(' ', 10 - LEN(CAST(COD_APP AS varchar(MAX))))   c_app,
                        ISNULL(ACCORD_APP, 'N')                                a_app,
                        REPLICATE('0', 12 - LEN(CAST(NUM_PEC_APP AS varchar(MAX))))
                            + CAST(NUM_PEC_APP AS varchar(MAX))   p_n_pec
                FROM    lig_bult_app
                WHERE   cod_soc  = @bult_COD_SOC
                AND     mat_pers = @bult_mat_pers
                AND     num_fam  = @bult_NUM_FAM
                AND     dat_soin = @bult_dat_s
                AND     num_soin = @bult_num_soin

            OPEN apps
            FETCH NEXT FROM apps INTO
                @app_PRF_TYP,
                @app_PRF_COD,
                @app_p_etab,
                @app_m_h_p,
                @app_d_app,
                @COD_APP,
                @app_c_app,
                @app_a_app,
                @app_p_n_pec

            WHILE @@FETCH_STATUS = 0
            BEGIN
                -- l'appareil n'est écrit que s'il est pris en charge par la CNAM
                SELECT  @pecCnam = isnull(pec_cnam, 'O')
                FROM    REF_APPAREIL
                WHERE   COD_APP = @COD_APP

                IF @pecCnam <> 'N'
                BEGIN
                    -- pas de recherche propre aux appareils : @vclef garde la dernière clé établissement lue
                    SET @ligne_app = '6'
                                   + ISNULL(@bult_ord, '     ')
                                   + ISNULL(@app_p_etab, '          ')
                                   + ISNULL(@vclef, '  ')
                                   + ISNULL(@app_c_app, '          ')
                                   + ISNULL(@app_d_app, '        ')
                                   + ISNULL(@app_m_h_p, '          ')
                                   + ISNULL(@app_a_app, ' ')
                                   + ISNULL(@app_p_n_pec, '000000000000') + @entree;

                    SET @ligne_bord = @ligne_bord + @ligne_app
                END

                FETCH NEXT FROM apps INTO
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

            /* ---------- Bulletin suivant ---------- */
            FETCH NEXT FROM bult INTO
                @bult_COD_SOC,
                @bult_MAT_PERS,
                @bult_NUM_FAM,
                @bult_dat_soin,
                @bult_num_soin,
                @bult_dat_s,
                @bult_tot_h,
                @bult_cod_m,
                @bult_reg_r,
                @bult_num_p,
                @bult_ord,
                @bult_num_s
        END
        CLOSE bult
        DEALLOCATE bult

        /* ---------- Bordereau suivant ---------- */
        FETCH NEXT FROM bord INTO
            @bord_cod_soc,
            @bord_cod_bord,
            @bord_cod_b,
            @bord_DAT_BORD,
            @bord_nbr_b,
            @bord_tot_h
    END;
    CLOSE bord
    DEALLOCATE bord

    /* ================================================================== */
    /*  SORTIES : le texte est coupé en deux moitiés                      */
    /* ================================================================== */
    SET @ligne_bord1 = substring(@ligne_bord, 0, len(@ligne_bord) / 2)
    SET @ligne_bord2 = substring(@ligne_bord, len(@ligne_bord) / 2, len(@ligne_bord))
    SET @returnString  = cast(@ligne_bord1 AS varbinary(max))
    SET @returnString2 = cast(@ligne_bord2 AS varbinary(max))
END
GO
