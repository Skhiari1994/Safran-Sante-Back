/*
  Ajoute l'écran « Paramètre notifications » dans le menu Espace Admin.

  - Entrée de menu : table ADMEVENTTYPE, module 42 (Espace Admin), adresse /Admin/ParametreNotifications
    (route « ParametreNotifications » du module Admin côté front), placée après « Paramètres Généraux ».
  - Droits : mêmes rôles que l'entrée « Paramètres Généraux » (906113), table ADMSTORAGEAUTORISATION.
  - Peut être relancé sans danger : rien n'est ajouté si l'entrée ou le droit existe déjà.

  Retour arrière :
    DELETE FROM ADMSTORAGEAUTORISATION WHERE evt_id = (SELECT evt_id FROM ADMEVENTTYPE WHERE evt_action = N'/Admin/ParametreNotifications');
    DELETE FROM ADMEVENTTYPE WHERE evt_action = N'/Admin/ParametreNotifications';
*/
USE [ramidb]
GO

SET NOCOUNT ON;
BEGIN TRANSACTION;

DECLARE @action nvarchar(510) = N'/Admin/ParametreNotifications';
DECLARE @evt bigint = (SELECT evt_id FROM ADMEVENTTYPE WHERE evt_action = @action);

IF @evt IS NULL
BEGIN
    SET @evt = 906114;
    IF EXISTS (SELECT 1 FROM ADMEVENTTYPE WHERE evt_id = @evt)
    BEGIN
        RAISERROR('evt_id 906114 est deja pris par une autre entree de menu : choisir un autre identifiant.', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    INSERT INTO ADMEVENTTYPE (evt_id, evt_action, evt_evt_id, evt_maint, evt_name, evt_rank, sum_id, EVT_ICON)
    VALUES (@evt, @action, NULL, N'O', N'Paramètre notifications', 6, 42, N'ri-notification-3-line');
END

INSERT INTO ADMSTORAGEAUTORISATION (id_autho, evt_id, sum_id, role_id)
SELECT (SELECT MAX(id_autho) FROM ADMSTORAGEAUTORISATION) + ROW_NUMBER() OVER (ORDER BY a.role_id),
       @evt, a.sum_id, a.role_id
FROM ADMSTORAGEAUTORISATION a
WHERE a.evt_id = 906113
  AND NOT EXISTS (SELECT 1 FROM ADMSTORAGEAUTORISATION x WHERE x.evt_id = @evt AND x.role_id = a.role_id);

SELECT e.evt_id, e.evt_evt_id AS parent, e.evt_name, e.evt_action, e.evt_maint, e.evt_rank, e.sum_id, e.EVT_ICON,
       (SELECT STRING_AGG(CAST(role_id AS varchar(10)), ',') FROM ADMSTORAGEAUTORISATION WHERE evt_id = e.evt_id) AS roles
FROM ADMEVENTTYPE e
WHERE e.evt_action = @action;

COMMIT TRANSACTION;
GO
