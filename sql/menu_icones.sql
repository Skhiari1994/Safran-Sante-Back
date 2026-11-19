/*
  Icônes du menu (écran « Gestion Menu »).

  Partie 1 (obligatoire) : ajoute la colonne EVT_ICON à ADMEVENTTYPE (icône des entrées de niveau 1).
      À exécuter AVANT de déployer le service Administrations : l'entité Admeventtype lit cette colonne.
  Partie 2 (facultative) : pré-remplit les icônes vides, d'après le libellé, avec les mêmes règles que le front.
      N'écrase jamais une icône déjà renseignée. Par défaut @appliquer = 0 : aperçu puis ROLLBACK.
      Mettre @appliquer = 1 pour enregistrer.

  Icônes des modules : colonne MDL_ICON1 (varchar), déjà lue par le menu. MDL_ICON (varbinary) n'est pas utilisée.
*/
USE [ramidb]
GO

IF COL_LENGTH('dbo.ADMEVENTTYPE', 'EVT_ICON') IS NULL
    ALTER TABLE dbo.ADMEVENTTYPE ADD EVT_ICON varchar(100) NULL;
GO

SET NOCOUNT ON;
DECLARE @appliquer bit = 0;

BEGIN TRANSACTION;

SELECT 'MODULE' AS niveau, m.SUM_ID AS id, m.SUM_NAME AS libelle, x.icone AS icone_proposee
FROM dbo.ADMSUBMODULE m
CROSS APPLY (SELECT CASE
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%referentiel%' THEN 'ri-database-2-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%rembours%'
          OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%frais medic%' THEN 'ri-heart-pulse-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%adhesion%'
          OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%adherent%' THEN 'ri-team-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%cotisation%' THEN 'ri-wallet-3-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%credit%'
          OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%pret%' THEN 'ri-bank-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%indemnit%' THEN 'ri-hand-coin-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%retraite%' THEN 'ri-timer-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%convention%' THEN 'ri-file-list-2-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%autorisation%' THEN 'ri-key-2-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%profil%' THEN 'ri-user-settings-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%admin%' THEN 'ri-shield-user-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%chat%' THEN 'ri-chat-3-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%dashboard%'
          OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%tableau de bord%' THEN 'ri-dashboard-2-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%calendrier%'
          OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%conge%' THEN 'ri-calendar-2-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%prime%' THEN 'ri-award-line'
        WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%paie%' THEN 'ri-bill-line'
    END AS icone) x
WHERE (m.MDL_ICON1 IS NULL OR LTRIM(RTRIM(m.MDL_ICON1)) = '')
ORDER BY m.SUM_RANK;

SELECT 'NIVEAU 1' AS niveau, e.EVT_ID AS id, e.EVT_NAME AS libelle, x.icone AS icone_proposee
FROM dbo.ADMEVENTTYPE e
CROSS APPLY (SELECT CASE
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE 'edition%' THEN 'ri-printer-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%parametrage%' THEN 'ri-settings-3-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%preparation%' THEN 'ri-file-edit-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%depart%'
          OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%reintegration%' THEN 'ri-logout-circle-r-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%chargement%' THEN 'ri-upload-cloud-2-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%prise en charge maladie%' THEN 'ri-stethoscope-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%bulletin%' THEN 'ri-file-list-3-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%envoi%' THEN 'ri-send-plane-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%reception%' THEN 'ri-inbox-archive-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%reglement%' THEN 'ri-secure-payment-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%adherent mutuelle%' THEN 'ri-shield-check-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%fiche%' THEN 'ri-profile-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%commission%' THEN 'ri-percent-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%deblocage%' THEN 'ri-lock-unlock-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%modification%' THEN 'ri-pencil-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%saisie%' THEN 'ri-edit-box-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%validation%' THEN 'ri-checkbox-circle-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%consultation%' THEN 'ri-search-eye-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%virement%' THEN 'ri-exchange-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%cloture%' THEN 'ri-lock-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%suspension%' THEN 'ri-pause-circle-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%demande%'
          OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%offre%' THEN 'ri-file-text-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%cotisation%' THEN 'ri-wallet-3-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%credit%'
          OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%pret%' THEN 'ri-bank-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE 'adherent%' THEN 'ri-user-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%utilisateur%'
          OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%compte%' THEN 'ri-user-settings-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%rapport%' THEN 'ri-file-chart-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%conge%' THEN 'ri-calendar-2-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%formation%' THEN 'ri-book-2-line'
        WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%menu%' THEN 'ri-menu-line'
    END AS icone) x
WHERE e.EVT_EVT_ID IS NULL
  AND (e.EVT_ICON IS NULL OR LTRIM(RTRIM(e.EVT_ICON)) = '')
ORDER BY e.SUM_ID, e.EVT_RANK;

IF @appliquer = 1
BEGIN
    UPDATE m
    SET m.MDL_ICON1 = x.icone
    FROM dbo.ADMSUBMODULE m
    CROSS APPLY (SELECT CASE
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%referentiel%' THEN 'ri-database-2-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%rembours%'
              OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%frais medic%' THEN 'ri-heart-pulse-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%adhesion%'
              OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%adherent%' THEN 'ri-team-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%cotisation%' THEN 'ri-wallet-3-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%credit%'
              OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%pret%' THEN 'ri-bank-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%indemnit%' THEN 'ri-hand-coin-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%retraite%' THEN 'ri-timer-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%convention%' THEN 'ri-file-list-2-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%autorisation%' THEN 'ri-key-2-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%profil%' THEN 'ri-user-settings-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%admin%' THEN 'ri-shield-user-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%chat%' THEN 'ri-chat-3-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%dashboard%'
              OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%tableau de bord%' THEN 'ri-dashboard-2-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%calendrier%'
              OR m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%conge%' THEN 'ri-calendar-2-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%prime%' THEN 'ri-award-line'
            WHEN m.SUM_NAME COLLATE Latin1_General_CI_AI LIKE '%paie%' THEN 'ri-bill-line'
        END AS icone) x
    WHERE (m.MDL_ICON1 IS NULL OR LTRIM(RTRIM(m.MDL_ICON1)) = '')
      AND x.icone IS NOT NULL;

    UPDATE e
    SET e.EVT_ICON = x.icone
    FROM dbo.ADMEVENTTYPE e
    CROSS APPLY (SELECT CASE
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE 'edition%' THEN 'ri-printer-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%parametrage%' THEN 'ri-settings-3-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%preparation%' THEN 'ri-file-edit-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%depart%'
              OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%reintegration%' THEN 'ri-logout-circle-r-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%chargement%' THEN 'ri-upload-cloud-2-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%prise en charge maladie%' THEN 'ri-stethoscope-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%bulletin%' THEN 'ri-file-list-3-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%envoi%' THEN 'ri-send-plane-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%reception%' THEN 'ri-inbox-archive-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%reglement%' THEN 'ri-secure-payment-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%adherent mutuelle%' THEN 'ri-shield-check-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%fiche%' THEN 'ri-profile-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%commission%' THEN 'ri-percent-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%deblocage%' THEN 'ri-lock-unlock-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%modification%' THEN 'ri-pencil-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%saisie%' THEN 'ri-edit-box-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%validation%' THEN 'ri-checkbox-circle-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%consultation%' THEN 'ri-search-eye-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%virement%' THEN 'ri-exchange-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%cloture%' THEN 'ri-lock-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%suspension%' THEN 'ri-pause-circle-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%demande%'
              OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%offre%' THEN 'ri-file-text-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%cotisation%' THEN 'ri-wallet-3-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%credit%'
              OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%pret%' THEN 'ri-bank-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE 'adherent%' THEN 'ri-user-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%utilisateur%'
              OR e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%compte%' THEN 'ri-user-settings-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%rapport%' THEN 'ri-file-chart-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%conge%' THEN 'ri-calendar-2-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%formation%' THEN 'ri-book-2-line'
            WHEN e.EVT_NAME COLLATE Latin1_General_CI_AI LIKE '%menu%' THEN 'ri-menu-line'
        END AS icone) x
    WHERE e.EVT_EVT_ID IS NULL
      AND (e.EVT_ICON IS NULL OR LTRIM(RTRIM(e.EVT_ICON)) = '')
      AND x.icone IS NOT NULL;

    COMMIT TRANSACTION;
    PRINT 'Icones enregistrees.';
END
ELSE
BEGIN
    ROLLBACK TRANSACTION;
    PRINT 'Apercu uniquement : rien n a ete enregistre (mettre @appliquer = 1 pour appliquer).';
END
GO
