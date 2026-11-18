USE [ramidb]
GO

BEGIN TRANSACTION;
GO

IF OBJECT_ID(N'dbo.insert_act_insert', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_act_insert;
IF OBJECT_ID(N'dbo.insert_act_update', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_act_update;
IF OBJECT_ID(N'dbo.insert_act_delete', N'TR') IS NOT NULL DROP TRIGGER dbo.insert_act_delete;
IF OBJECT_ID(N'dbo.trg_lig_bult_act', N'TR') IS NOT NULL DROP TRIGGER dbo.trg_lig_bult_act;
GO

CREATE TRIGGER dbo.trg_lig_bult_act
ON dbo.LIG_BULT_ACT
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    SET NOCOUNT ON;

    IF TRIGGER_NESTLEVEL(@@PROCID) > 1
        RETURN;

    DECLARE
        @operation          char(1),
        @codSoc             varchar(4),
        @matPers            varchar(10),
        @numFam             int,
        @datSoin            date,
        @abrvAct            varchar(4),
        @numSoin            numeric(18,0),
        @numLig             numeric(6),
        @datAct             date,
        @indice             numeric(15,3),
        @mntHonor           numeric(15,3),
        @mntNet             numeric(15,3),
        @actPrix            numeric(15,3),

        @ancCodSoc          varchar(4),
        @ancMatPers         varchar(10),
        @ancNumSoin         numeric(18,0),
        @ancIndice          numeric(15,3),
        @ancMntHonor        numeric(15,3),
        @ancMntNet          numeric(15,3),
        @ancMutMntNet       numeric(15,3),
        @ancMntNetConj      numeric(15,3),

        @vIndic             varchar(1),
        @vNatAct            varchar(1),
        @vPlafonne          varchar(1),
        @vProratat          varchar(1),
        @vTauxAct           numeric(5,2),
        @vMttActe           numeric(10,3),
        @vPlafond           numeric(10,3),
        @vMntNet            numeric(10,3),
        @wPlafond           numeric(10,3),
        @wSoldPlaf          numeric(10,3),
        @wPlafondDetail     numeric(10,3),
        @wSoldPlafDetail    numeric(10,3),
        @mutMntNet          numeric(10,3),
        @var                numeric(10,3),

        @codFilConj         varchar(max),
        @codAssurConj       varchar(max),
        @matPersConj        varchar(max),
        @codSocConj         varchar(max),
        @mntNetConj         numeric(15,3),
        @ligMntNetConj      numeric(15,3),
        @ligSauv            numeric(15,3),
        @bultCum            numeric(15,3),

        @numLigBult         numeric(10,3),
        @mntNetLigBult      numeric(10,3),
        @erreurLigBult      nvarchar(4000),

        @anneeCnam          int,
        @soldCnam           numeric(15,3);

    SET @operation = CASE
        WHEN EXISTS (SELECT 1 FROM inserted) AND EXISTS (SELECT 1 FROM deleted) THEN 'U'
        WHEN EXISTS (SELECT 1 FROM inserted) THEN 'I'
        WHEN EXISTS (SELECT 1 FROM deleted) THEN 'D'
    END;

    IF @operation IS NULL
        RETURN;

    IF @operation = 'D'
        SELECT @codSoc = cod_soc, @matPers = mat_pers, @numFam = num_fam,
               @datSoin = dat_soin, @abrvAct = abrv_act
        FROM deleted;
    ELSE
        SELECT @codSoc = cod_soc, @matPers = mat_pers, @numFam = num_fam,
               @datSoin = dat_soin, @abrvAct = abrv_act, @numSoin = num_soin,
               @numLig = num_lig, @datAct = dat_act, @indice = ISNULL(indice, 1),
               @mntHonor = mnt_honor, @mntNet = mnt_net, @actPrix = act_prix
        FROM inserted;

    IF @operation = 'I'
        SELECT @ancCodSoc = @codSoc, @ancMatPers = @matPers, @ancNumSoin = @numSoin,
               @ancIndice = 0, @ancMntHonor = 0, @ancMntNet = 0,
               @ancMutMntNet = 0, @ancMntNetConj = 0;
    ELSE
        SELECT @ancCodSoc = cod_soc, @ancMatPers = mat_pers, @ancNumSoin = num_soin,
               @ancIndice = CASE WHEN @operation = 'U' THEN ISNULL(indice, 1) ELSE indice END,
               @ancMntHonor = mnt_honor, @ancMntNet = mnt_net,
               @ancMutMntNet = mut_mnt_net, @ancMntNetConj = mnt_net_conj
        FROM deleted;

    IF @operation = 'D'
    BEGIN
        SELECT @mntNetLigBult = ISNULL(mnt_net, 0)
        FROM lig_bult
        WHERE cod_soc = @ancCodSoc
          AND mat_pers = @ancMatPers
          AND num_fam = @numFam
          AND num_soin = @ancNumSoin
          AND dat_soin = @datSoin
          AND abrv_act = @abrvAct;

        IF @mntNetLigBult = @ancMutMntNet
            DELETE FROM lig_bult
            WHERE cod_soc = @ancCodSoc
              AND mat_pers = @ancMatPers
              AND num_fam = @numFam
              AND num_soin = @ancNumSoin
              AND dat_soin = @datSoin
              AND abrv_act = @abrvAct;
        ELSE
            UPDATE lig_bult
            SET mnt_honor = ISNULL(mnt_honor, 0) - @ancMntHonor,
                mnt_net = ISNULL(mnt_net, 0) - @ancMutMntNet,
                indice = indice - @ancIndice,
                mnt_net_conj = ISNULL(mnt_net_conj, 0) - ISNULL(@ancMntNetConj, 0)
            WHERE cod_soc = @ancCodSoc
              AND mat_pers = @ancMatPers
              AND num_fam = @numFam
              AND num_soin = @ancNumSoin
              AND dat_soin = @datSoin
              AND abrv_act = @abrvAct;
    END
    ELSE
    BEGIN
        SELECT @vIndic = ISNULL(a_indice, 'N'),
               @vNatAct = ISNULL(nat_act, 'M'),
               @vPlafonne = ISNULL(plafonne, 'N'),
               @vTauxAct = ISNULL(taux_act, 0),
               @vMttActe = ISNULL(mtt_acte, 0),
               @vPlafond = ISNULL(plafond, 0),
               @vProratat = ISNULL(proratat, 'N')
        FROM bareme_remb
        WHERE cod_fil = (SELECT cod_fil FROM pers_affil
                         WHERE cod_soc = @codSoc AND mat_pers = @matPers AND courant = 'O')
          AND cod_assur = (SELECT cod_assur FROM personnel
                           WHERE cod_soc = @codSoc AND mat_pers = @matPers)
          AND abrv_act = @abrvAct;

        SELECT @wPlafond = ISNULL(plafond, 0),
               @wSoldPlaf = ISNULL(sold_assur_estim, 0) - @ancMutMntNet
        FROM plafond_assur
        WHERE cod_soc = @codSoc
          AND mat_pers = @matPers
          AND num_fam = @numFam
          AND annee_assur = (SELECT MAX(annee_assur) FROM plafond_assur
                             WHERE cod_soc = @codSoc AND mat_pers = @matPers AND num_fam = @numFam);

        IF @vProratat = 'O'
        BEGIN
            SELECT @wPlafondDetail = ISNULL(plafond, 0),
                   @wSoldPlafDetail = ISNULL(sold_assur_estim, 0)
            FROM plafond_assur_detail
            WHERE cod_soc = @codSoc
              AND mat_pers = @matPers
              AND num_fam = @numFam
              AND abrv_act = @abrvAct
              AND cod_fil = (SELECT cod_fil FROM pers_affil
                             WHERE cod_soc = @codSoc AND mat_pers = @matPers AND courant = 'O')
              AND annee_assur = (SELECT MAX(annee_assur) FROM plafond_assur
                                 WHERE cod_soc = @codSoc AND mat_pers = @matPers);

            IF @wPlafondDetail < @wPlafond
                SET @wPlafond = @wPlafondDetail;
            IF @wSoldPlafDetail < @wSoldPlaf
                SET @wSoldPlaf = @wSoldPlafDetail;
        END

        IF @vIndic = 'O' AND @vNatAct = 'M'
            SET @vMntNet = CAST(@indice AS float) * CAST(@vMttActe AS float);
        ELSE IF @vIndic = 'O'
            SET @vMntNet = CAST(@indice AS float) * CAST(@mntHonor AS float) * (CAST(@vTauxAct AS float) / 100);
        ELSE IF @vNatAct = 'M'
            SET @vMntNet = @mntHonor;
        ELSE
            SET @vMntNet = CAST(@actPrix AS float) * (CAST(@vTauxAct AS float) / 100);

        IF @vPlafonne = 'O' AND @vMntNet > @vPlafond
            SET @mutMntNet = @vPlafond;
        ELSE IF @vNatAct = 'M' AND @vIndic = 'O' AND @vMntNet > @mntHonor
            SET @mutMntNet = @mntHonor;
        ELSE IF @vNatAct = 'M' AND @vIndic <> 'O' AND @vMntNet > @vMttActe
            SET @mutMntNet = @vMttActe;
        ELSE
            SET @mutMntNet = @vMntNet;

        IF @mutMntNet + @wSoldPlaf > @wPlafond
            SET @mutMntNet = @wPlafond - @wSoldPlaf;

        IF @mutMntNet + @mntNet > @mntHonor
            SET @mutMntNet = @mntHonor - @mntNet;

        SELECT @var = ISNULL(mnt_net, 0) - ISNULL(@ancMutMntNet, 0)
        FROM lig_bult
        WHERE cod_soc = @codSoc
          AND mat_pers = @matPers
          AND num_fam = @numFam
          AND dat_soin = @datSoin
          AND num_soin = @ancNumSoin
          AND abrv_act = @abrvAct;

        IF @vPlafonne = 'O' AND ISNULL(@var, 0) + @mutMntNet >= @vPlafond
            SET @mutMntNet = @vPlafond - ISNULL(@var, 0);

        SELECT @matPersConj = mat_pers_conj, @codSocConj = cod_soc_conj
        FROM famille
        WHERE mat_pers = @matPers AND cod_soc = @codSoc AND num_fam = '99' AND parente = 'C';

        SELECT @codFilConj = cod_fil
        FROM pers_affil
        WHERE mat_pers = @matPersConj AND cod_soc = @codSocConj;

        SELECT @codAssurConj = cod_assur
        FROM personnel
        WHERE mat_pers = @matPersConj AND cod_soc = @codSocConj;

        SET @mntNetConj = @mntHonor - @mntNet - @mutMntNet;

        EXEC dbo.calcul_mntNetConj
            @soc = @codSoc,
            @mat = @matPers,
            @cod_fil = @codFilConj,
            @cod_assur = @codAssurConj,
            @numfam = @numFam,
            @datsoin = @datSoin,
            @sauv_net = 0,
            @cumul_net = 0,
            @ACT_vmnt_honor = @mntNetConj,
            @ACT_indice = @indice,
            @ACT_ABRV_ACT = @abrvAct,
            @lig_mntNetConj = @ligMntNetConj OUTPUT,
            @lig_sauv = @ligSauv OUTPUT,
            @bult_cum = @bultCum OUTPUT;

        IF @operation = 'U'
        BEGIN
            SELECT @ancMntNetConj = mnt_net_conj FROM inserted;

            SELECT @ancMntNetConj = mnt_net_conj
            FROM lig_bult_act
            WHERE cod_soc = @ancCodSoc
              AND mat_pers = @ancMatPers
              AND num_fam = @numFam
              AND dat_soin = @datSoin
              AND num_soin = @ancNumSoin
              AND abrv_act = @abrvAct
              AND dat_act = @datAct;
        END

        IF @operation = 'I'
           AND NOT EXISTS (SELECT 1 FROM lig_bult
                           WHERE cod_soc = @codSoc
                             AND mat_pers = @matPers
                             AND num_fam = @numFam
                             AND dat_soin = @datSoin
                             AND num_soin = @numSoin
                             AND abrv_act = @abrvAct)
        BEGIN
            SELECT @numLigBult = ISNULL(MAX(ISNULL(num_lig, 0)), 0) + 1
            FROM lig_bult
            WHERE cod_soc = @codSoc
              AND mat_pers = @matPers
              AND num_fam = @numFam
              AND num_soin = @numSoin
              AND dat_soin = @datSoin;

            BEGIN TRY
                INSERT INTO lig_bult (cod_soc, mat_pers, num_fam, dat_soin, abrv_act, num_lig,
                                      mnt_honor, mnt_net, dat_act, indice, num_soin, mnt_net_conj)
                VALUES (@codSoc, @matPers, @numFam, @datSoin, @abrvAct, @numLigBult,
                        @mntHonor, ISNULL(@mutMntNet, 0), @datAct, @indice, @numSoin, @ligMntNetConj);
            END TRY
            BEGIN CATCH
                SET @erreurLigBult = ERROR_MESSAGE();
            END CATCH
        END
        ELSE
        BEGIN
            UPDATE lig_bult
            SET mnt_honor = ISNULL(mnt_honor, 0) + @mntHonor - @ancMntHonor,
                mnt_net = ISNULL(mnt_net, 0) + ISNULL(@mutMntNet, 0) - ISNULL(@ancMutMntNet, 0),
                indice = indice - @ancIndice + @indice,
                mnt_net_conj = ISNULL(mnt_net_conj, 0) + @ligMntNetConj - ISNULL(@ancMntNetConj, 0),
                dat_act = @datAct
            WHERE cod_soc = @codSoc
              AND mat_pers = @matPers
              AND num_fam = @numFam
              AND dat_soin = @datSoin
              AND num_soin = @ancNumSoin
              AND abrv_act = @abrvAct;
        END

        IF @operation = 'I'
            UPDATE a
            SET mut_mnt_net = @mutMntNet,
                mnt_net_conj = @mntNetConj
            FROM lig_bult_act a
            JOIN inserted b
              ON a.cod_soc = b.cod_soc
             AND a.mat_pers = b.mat_pers
             AND a.num_fam = b.num_fam
             AND a.dat_soin = b.dat_soin
             AND a.num_soin = b.num_soin
             AND a.abrv_act = b.abrv_act
             AND a.dat_act = b.dat_act;
        ELSE
            UPDATE lig_bult_act
            SET mut_mnt_net = @mutMntNet,
                mnt_net_conj = @ligMntNetConj
            WHERE cod_soc = @codSoc
              AND mat_pers = @matPers
              AND num_fam = @numFam
              AND dat_soin = @datSoin
              AND num_soin = @ancNumSoin
              AND abrv_act = @abrvAct
              AND num_lig = @numLig
              AND dat_act = @datAct;
    END

    IF @operation = 'D'
        SET @anneeCnam = YEAR(@datSoin);
    ELSE
        SELECT @anneeCnam = MAX(annee_cnam)
        FROM plafond_cnam
        WHERE cod_soc = @ancCodSoc AND mat_pers = @ancMatPers;

    SELECT @soldCnam = ISNULL(sold_cnam_estim, 0)
    FROM plafond_cnam
    WHERE cod_soc = @ancCodSoc
      AND mat_pers = @ancMatPers
      AND annee_cnam = @anneeCnam;

    SET @soldCnam = @soldCnam - ISNULL(@ancMntNet, 0) + ISNULL(@mntNet, 0);

    IF @operation <> 'I' AND @soldCnam < 0
        SET @soldCnam = 0;

    UPDATE plafond_cnam
    SET sold_cnam_estim = @soldCnam
    WHERE cod_soc = @ancCodSoc
      AND mat_pers = @ancMatPers
      AND annee_cnam = @anneeCnam;
END
GO

IF OBJECT_ID(N'dbo.trg_lig_bult_act', N'TR') IS NULL
    ROLLBACK TRANSACTION;
ELSE
    COMMIT TRANSACTION;
GO
