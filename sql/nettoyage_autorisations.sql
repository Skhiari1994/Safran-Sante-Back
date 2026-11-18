/*
  Nettoyage des autorisations devenues inutiles (table ADMSTORAGEAUTORISATION).

  Une autorisation est inutile quand elle porte sur une entrée de menu CACHÉE :
    - l'entrée elle-même est inactive (EVT_MAINT <> 'O'), ou
    - un de ses parents (à n'importe quel niveau) est inactif.
  L'écran « Gestion des autorisations » ne charge que les entrées actives : il ne peut ni afficher ni décocher ces lignes,
  et le menu ne les affiche jamais. Elles restent seulement dans la base.

  Ne sont JAMAIS touchées : les lignes de module (EVT_ID NULL) et les autorisations d'entrées visibles.

  Utilisation
    @role_id   : NULL = tous les profils ; sinon l'id d'un profil (1 = UTILISATEUR, 2 = CHEF, 3 = RH, 4 = ADMIN...).
    @appliquer : 0 (défaut) = aperçu puis ROLLBACK, rien n'est modifié ; 1 = supprime.
  À l'application, les lignes supprimées sont d'abord copiées dans dbo.ADMSTORAGEAUTORISATION_SAUV_20261118
  (voir la requête de restauration en bas du script).

  Attention : si une entrée inactive est réactivée plus tard, ses anciennes autorisations ne seront plus là
  (il faudra les recocher, ou les restaurer depuis la table de sauvegarde).
*/
USE [ramidb]
GO

SET NOCOUNT ON;
DECLARE @role_id bigint = NULL;
DECLARE @appliquer bit = 0;

BEGIN TRANSACTION;

;WITH chaine AS (
    SELECT e.EVT_ID AS evt_depart, e.EVT_ID AS courant, e.EVT_EVT_ID AS parent, e.EVT_MAINT AS maint
    FROM dbo.ADMEVENTTYPE e
    UNION ALL
    SELECT c.evt_depart, p.EVT_ID, p.EVT_EVT_ID, p.EVT_MAINT
    FROM chaine c
    JOIN dbo.ADMEVENTTYPE p ON p.EVT_ID = c.parent
),
caches AS (
    SELECT DISTINCT evt_depart
    FROM chaine
    WHERE ISNULL(maint, 'N') <> 'O'
)
SELECT a.ID_AUTHO, a.ROLE_ID, a.SUM_ID, a.EVT_ID
INTO #a_supprimer
FROM dbo.ADMSTORAGEAUTORISATION a
JOIN caches c ON c.evt_depart = a.EVT_ID
WHERE a.EVT_ID IS NOT NULL
  AND (@role_id IS NULL OR a.ROLE_ID = @role_id)
OPTION (MAXRECURSION 50);

SELECT 'RESUME' AS partie, r.NAME AS profil, COUNT(*) AS nb_lignes_a_supprimer
FROM #a_supprimer s
JOIN dbo.ROLE r ON r.ID = s.ROLE_ID
GROUP BY r.NAME
ORDER BY r.NAME;

SELECT 'DETAIL' AS partie, r.NAME AS profil, m.SUM_NAME AS module, s.EVT_ID, e.EVT_NAME AS entree,
       CASE WHEN ISNULL(e.EVT_MAINT, 'N') <> 'O' THEN 'entree inactive' ELSE 'groupe parent inactif' END AS raison
FROM #a_supprimer s
JOIN dbo.ROLE r              ON r.ID = s.ROLE_ID
LEFT JOIN dbo.ADMSUBMODULE m ON m.SUM_ID = s.SUM_ID
LEFT JOIN dbo.ADMEVENTTYPE e ON e.EVT_ID = s.EVT_ID
ORDER BY r.NAME, m.SUM_NAME, s.EVT_ID;

IF @appliquer = 1
BEGIN
    IF OBJECT_ID('dbo.ADMSTORAGEAUTORISATION_SAUV_20261118') IS NULL
        SELECT CAST(a.ID_AUTHO AS bigint) AS ID_AUTHO, a.EVT_ID, a.SUM_ID, a.ROLE_ID
        INTO dbo.ADMSTORAGEAUTORISATION_SAUV_20261118
        FROM dbo.ADMSTORAGEAUTORISATION a
        WHERE 1 = 0;

    INSERT INTO dbo.ADMSTORAGEAUTORISATION_SAUV_20261118 (ID_AUTHO, EVT_ID, SUM_ID, ROLE_ID)
    SELECT a.ID_AUTHO, a.EVT_ID, a.SUM_ID, a.ROLE_ID
    FROM dbo.ADMSTORAGEAUTORISATION a
    JOIN #a_supprimer s ON s.ID_AUTHO = a.ID_AUTHO
    WHERE NOT EXISTS (SELECT 1 FROM dbo.ADMSTORAGEAUTORISATION_SAUV_20261118 b WHERE b.ID_AUTHO = a.ID_AUTHO);

    DELETE a
    FROM dbo.ADMSTORAGEAUTORISATION a
    JOIN #a_supprimer s ON s.ID_AUTHO = a.ID_AUTHO;

    PRINT CONCAT(@@ROWCOUNT, ' autorisation(s) supprimee(s) ; copie dans ADMSTORAGEAUTORISATION_SAUV_20261118.');
    COMMIT TRANSACTION;
END
ELSE
BEGIN
    ROLLBACK TRANSACTION;
    PRINT 'Apercu uniquement : rien n a ete supprime (mettre @appliquer = 1 pour appliquer).';
END

DROP TABLE #a_supprimer;
GO

/*
  Restauration (si besoin) : remet les lignes sauvegardées qui n'existent plus.
  Si ID_AUTHO est une colonne d'identité, entourer l'INSERT de SET IDENTITY_INSERT dbo.ADMSTORAGEAUTORISATION ON / OFF.

  INSERT INTO dbo.ADMSTORAGEAUTORISATION (ID_AUTHO, EVT_ID, SUM_ID, ROLE_ID)
  SELECT s.ID_AUTHO, s.EVT_ID, s.SUM_ID, s.ROLE_ID
  FROM dbo.ADMSTORAGEAUTORISATION_SAUV_20261118 s
  WHERE NOT EXISTS (SELECT 1 FROM dbo.ADMSTORAGEAUTORISATION a WHERE a.ID_AUTHO = s.ID_AUTHO);
*/
