/*
  Ajoute l'écran « Corps » sous Référentiels > Paramétrage adhérents.

  - Entrée de menu : table ADMEVENTTYPE, rattachée au groupe 6010 (Paramétrage adhérents), module 22 (Référentiels),
    adresse Parametrage/Adherent/corps (route « corps » du module ParamAdherent côté front).
  - Droits : mêmes rôles que l'entrée « Types de départ » (6017), table ADMSTORAGEAUTORISATION.
  - Peut être relancé sans danger : rien n'est ajouté si l'entrée ou le droit existe déjà.

  Retour arrière :
    DELETE FROM ADMSTORAGEAUTORISATION WHERE evt_id = (SELECT evt_id FROM ADMEVENTTYPE WHERE evt_action = N'Parametrage/Adherent/corps');
    DELETE FROM ADMEVENTTYPE WHERE evt_action = N'Parametrage/Adherent/corps';
*/
USE [ramidb]
GO

SET NOCOUNT ON;
BEGIN TRANSACTION;

DECLARE @action nvarchar(510) = N'Parametrage/Adherent/corps';
DECLARE @evt bigint = (SELECT evt_id FROM ADMEVENTTYPE WHERE evt_action = @action);

IF @evt IS NULL
BEGIN
    SET @evt = 6019;
    IF EXISTS (SELECT 1 FROM ADMEVENTTYPE WHERE evt_id = @evt)
    BEGIN
        RAISERROR('evt_id 6019 est deja pris par une autre entree de menu : choisir un autre identifiant.', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    INSERT INTO ADMEVENTTYPE (evt_id, evt_action, evt_evt_id, evt_maint, evt_name, evt_rank, sum_id)
    VALUES (@evt, @action, 6010, N'O', N'Corps', 9, 22);
END

INSERT INTO ADMSTORAGEAUTORISATION (id_autho, evt_id, sum_id, role_id)
SELECT (SELECT MAX(id_autho) FROM ADMSTORAGEAUTORISATION) + ROW_NUMBER() OVER (ORDER BY a.role_id),
       @evt, a.sum_id, a.role_id
FROM ADMSTORAGEAUTORISATION a
WHERE a.evt_id = 6017
  AND NOT EXISTS (SELECT 1 FROM ADMSTORAGEAUTORISATION x WHERE x.evt_id = @evt AND x.role_id = a.role_id);

SELECT e.evt_id, e.evt_evt_id AS parent, e.evt_name, e.evt_action, e.evt_maint, e.evt_rank, e.sum_id,
       (SELECT STRING_AGG(CAST(role_id AS varchar(10)), ',') FROM ADMSTORAGEAUTORISATION WHERE evt_id = e.evt_id) AS roles
FROM ADMEVENTTYPE e
WHERE e.evt_action = @action;

COMMIT TRANSACTION;
GO
