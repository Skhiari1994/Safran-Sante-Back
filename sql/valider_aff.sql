-- Validation d'une demande d'affiliation mutuelle (appelée par Gestion_adherent : /Personnel/callValiderAff).
-- Traduction de la procédure Oracle VALIDER_AFF (schéma ASSUR@DB11G) : absente de la base SQL Server.
--
--   etat_aff = 'V' et typ_aff = 'A' : PERSONNEL.num_assur = matricule, dat_ass = date, typ_aff = 'A'
--   etat_aff = 'V' et typ_aff = 'S' : PERSONNEL.num_assur = NULL,      dat_ass = date, typ_aff = 'S'
--   autre combinaison               : aucune modification
--
-- La date arrive en texte : 'dd/MM/yyyy' (format Oracle d'origine) ou 'yyyy-MM-dd'. Comme to_date sous Oracle,
-- une date illisible fait échouer l'appel au lieu d'écraser dat_ass par NULL.
-- Script rejouable (CREATE OR ALTER).

CREATE OR ALTER PROCEDURE dbo.valider_aff
    @cod_soc_  VARCHAR(4),
    @etat_aff_ VARCHAR(1),
    @typ_aff_  VARCHAR(1),
    @mat_pers_ VARCHAR(10),
    @dat_ass_  VARCHAR(30)
AS
BEGIN
    SET NOCOUNT ON;

    IF @etat_aff_ = 'V' AND @typ_aff_ IN ('A', 'S')
    BEGIN
        -- la base est en niveau de compatibilité 100 : pas de TRY_CONVERT. On passe par 'yyyyMMdd', sans ambiguïté
        -- de langue, que ISDATE valide avant la conversion.
        DECLARE @texte VARCHAR(10) = LEFT(LTRIM(RTRIM(@dat_ass_)), 10);
        DECLARE @iso VARCHAR(8) = CASE
            WHEN @texte LIKE '[0-3][0-9]/[0-1][0-9]/[0-9][0-9][0-9][0-9]'
                THEN SUBSTRING(@texte, 7, 4) + SUBSTRING(@texte, 4, 2) + SUBSTRING(@texte, 1, 2)
            WHEN @texte LIKE '[0-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]'
                THEN SUBSTRING(@texte, 1, 4) + SUBSTRING(@texte, 6, 2) + SUBSTRING(@texte, 9, 2)
            ELSE NULL
        END;

        IF @iso IS NULL OR ISDATE(@iso) = 0
        BEGIN
            RAISERROR('valider_aff : date d''assurance invalide (format attendu dd/MM/yyyy).', 16, 1);
            RETURN;
        END;

        DECLARE @dat DATE = CONVERT(DATE, @iso, 112);

        UPDATE dbo.PERSONNEL
           SET NUM_ASSUR = CASE WHEN @typ_aff_ = 'A' THEN @mat_pers_ ELSE NULL END,
               DAT_ASS   = @dat,
               TYP_AFF   = @typ_aff_
         WHERE COD_SOC  = @cod_soc_
           AND MAT_PERS = @mat_pers_;
    END
END
GO
