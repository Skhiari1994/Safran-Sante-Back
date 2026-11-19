-- Table des demandes d'affiliation / adhésion mutuelle (entité AffilMutuelle du service Gestion_adherent).
-- Absente de la base SQL Server après la migration depuis Oracle : "Invalid object name 'affil_mutuelle'".
-- Reprise du DDL Oracle d'origine (schéma ASSUR@DB11G) : colonnes, valeur par défaut, clé primaire,
-- clés étrangères vers PERSONNEL et TYPE_DEPART, contraintes de valeurs.
-- Script rejouable : ne fait rien si la table existe déjà.
-- Hibernate est en ddl-auto: none, rien ne la crée automatiquement.

IF OBJECT_ID('dbo.AFFIL_MUTUELLE', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.AFFIL_MUTUELLE (
        COD_SOC        VARCHAR(4)    NOT NULL,
        MAT_PERS       VARCHAR(10)   NOT NULL,
        DAT_ASS        DATE          NOT NULL,
        NUM_ASSUR      VARCHAR(10)   NULL,
        TYP_AFF        VARCHAR(1)    NULL,
        DAT_DEM        DATE          NULL,
        OBS_AFF        VARCHAR(100)  NULL,
        COEF_COT       NUMERIC(6,3)  NULL CONSTRAINT DF_AFFIL_MUTUELLE_COEF_COT DEFAULT 100,
        ETAT_AFF       VARCHAR(1)    NULL,
        CORPS          VARCHAR(1)    NULL,
        COD_TYP_DEPART VARCHAR(4)    NULL,
        COD_AFFECT     VARCHAR(4)    NULL,
        CONSTRAINT PK_AFFIL_MUTUELLE PRIMARY KEY (COD_SOC, MAT_PERS, DAT_ASS),
        CONSTRAINT FK1_AFFIL_MUTUELLE FOREIGN KEY (COD_TYP_DEPART) REFERENCES dbo.TYPE_DEPART (COD_TYP_DEPART),
        CONSTRAINT FK2_AFFIL_MUTUELLE FOREIGN KEY (COD_SOC, MAT_PERS) REFERENCES dbo.PERSONNEL (COD_SOC, MAT_PERS),
        CONSTRAINT CK_AFFIL_MUTUELLE  CHECK (TYP_AFF IN ('A', 'S')),
        CONSTRAINT CK1_AFFIL_MUTUELLE CHECK (ETAT_AFF IN ('I', 'V'))
    );
END
GO
