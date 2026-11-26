USE [ramidb]
GO

/*
Sequence SEQ_ROLE : utilisee par l'entite Role (Administrations) pour numeroter les profils.
Elle existait sous Oracle et n'a pas ete migree vers SQL Server :
  "Invalid object name 'seq_role'" a l'ajout d'un profil (POST /admin/PERS_VALIDEUR/AddRole).

Demarre juste apres le plus grand id deja present dans la table ROLE (6 si la table contient les profils 1 a 5).
Le script peut etre relance sans risque : rien n'est fait si la sequence existe deja.
*/

IF NOT EXISTS (SELECT 1 FROM sys.sequences WHERE name = N'SEQ_ROLE' AND schema_id = SCHEMA_ID(N'dbo'))
BEGIN
    DECLARE @debut bigint = (SELECT ISNULL(MAX(id), 0) + 1 FROM dbo.[ROLE]);
    DECLARE @sql nvarchar(200) = N'CREATE SEQUENCE dbo.SEQ_ROLE AS bigint START WITH ' + CAST(@debut AS nvarchar(20))
                               + N' INCREMENT BY 1 NO CACHE;';
    EXEC sys.sp_executesql @sql;
END
GO
