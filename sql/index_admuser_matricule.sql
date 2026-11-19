-- Index sur ADMUSER (cod_soc, use_matricule) : accélère la jointure avec PERSONNEL (cod_soc, mat_pers)
-- utilisée notamment par la mise à jour de use_login à partir de personnel.cin.
-- Non unique (use_matricule peut être vide ou répété). Nommage aligné sur IX_ADMUSER_COD_SOC_USE_LOGIN.
-- Script rejouable : ne fait rien si l'index existe déjà.

IF NOT EXISTS (SELECT 1 FROM sys.indexes
                WHERE object_id = OBJECT_ID('dbo.ADMUSER')
                  AND name = 'IX_ADMUSER_COD_SOC_USE_MATRICULE')
BEGIN
    CREATE NONCLUSTERED INDEX IX_ADMUSER_COD_SOC_USE_MATRICULE
        ON dbo.ADMUSER (cod_soc, use_matricule);
END
GO
