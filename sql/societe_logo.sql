/*
  Logo d'une société (écran « Sociétés »).

  Ajoute la colonne LOGO_SOC à SOCIETE : l'image (PNG, JPEG ou WebP, 5 Mo au plus) est stockée telle quelle.
  Le contrôle du format et de la taille est fait par le service Referentiel.

  À exécuter AVANT de déployer le service Referentiel : l'entité Societe lit cette colonne,
  et sans elle toutes les requêtes sur les sociétés échouent.
*/
USE [ramidb]
GO

IF COL_LENGTH('dbo.SOCIETE', 'LOGO_SOC') IS NULL
    ALTER TABLE dbo.SOCIETE ADD LOGO_SOC varbinary(max) NULL;
GO

SELECT COD_SOC, LIB_SOC, CASE WHEN LOGO_SOC IS NULL THEN 'sans logo' ELSE 'avec logo' END AS logo
FROM dbo.SOCIETE;
GO
