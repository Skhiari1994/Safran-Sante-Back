

USE ramidb;
GO

SET NOCOUNT ON;
GO

/* ------------------------------ Séquences ------------------------------ */
/* Le service demande un identifiant à la fois (allocationSize = 1) : INCREMENT BY 1 */

IF OBJECT_ID('dbo.SEQ_CHAT_ROOM', 'SO') IS NULL
    CREATE SEQUENCE dbo.SEQ_CHAT_ROOM AS BIGINT START WITH 1 INCREMENT BY 1 NO CACHE;
GO

IF OBJECT_ID('dbo.CHAT_MSG_SEQ', 'SO') IS NULL
    CREATE SEQUENCE dbo.CHAT_MSG_SEQ AS BIGINT START WITH 1 INCREMENT BY 1 NO CACHE;
GO

IF OBJECT_ID('dbo.CHAT_REACT_SEQ', 'SO') IS NULL
    CREATE SEQUENCE dbo.CHAT_REACT_SEQ AS BIGINT START WITH 1 INCREMENT BY 1 NO CACHE;
GO

/* ------------------------------ Conversations (1 ligne par paire d'utilisateurs) ------------------------------ */

IF OBJECT_ID('dbo.CHAT_ROOM', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CHAT_ROOM
    (
        ID              BIGINT        NOT NULL,
        ROOM_ID         VARCHAR(100)  NOT NULL,   -- "room_<user min>_<user max>"
        USER1_ID        VARCHAR(100)  NOT NULL,   -- identifiant chat = login (sinon matricule) de ADMUSER
        USER2_ID        VARCHAR(100)  NOT NULL,
        CREATED_AT      DATETIME2(0)  NULL,
        LAST_MESSAGE_AT DATETIME2(0)  NULL,
        CONSTRAINT PK_CHAT_ROOM PRIMARY KEY (ID),
        CONSTRAINT UQ_CHAT_ROOM_ROOM_ID UNIQUE (ROOM_ID)
    );
END
GO

/* ------------------------------ Messages ------------------------------ */

IF OBJECT_ID('dbo.CHAT_MESSAGE', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CHAT_MESSAGE
    (
        ID           BIGINT         NOT NULL,
        SENDER_ID    VARCHAR(100)   NOT NULL,
        SENDER_NAME  NVARCHAR(255)  NULL,
        RECEIVER_ID  VARCHAR(100)   NULL,
        ROOM_ID      VARCHAR(100)   NULL,
        CONTENT      NVARCHAR(MAX)  NULL,          -- texte (émojis, arabe...), nom de fichier ou audio
        MESSAGE_TYPE VARCHAR(30)    NULL,          -- CHAT, FILE, IMAGE, VOICE...
        [TIMESTAMP]  DATETIME2(0)   NULL,
        IS_READ      BIT            NOT NULL CONSTRAINT DF_CHAT_MESSAGE_IS_READ DEFAULT (0),
        READ_AT      DATETIME2(0)   NULL,          -- "Vu à HH:mm"
        REPLY_TO_ID  BIGINT         NULL,          -- message cité
        CONSTRAINT PK_CHAT_MESSAGE PRIMARY KEY (ID)
    );
END
GO

/* ------------------------------ Réactions (une par utilisateur et par message) ------------------------------ */

IF OBJECT_ID('dbo.CHAT_REACTION', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.CHAT_REACTION
    (
        ID         BIGINT        NOT NULL,
        MESSAGE_ID BIGINT        NOT NULL,
        USER_ID    VARCHAR(100)  NOT NULL,
        EMOJI      NVARCHAR(20)  NOT NULL,
        CONSTRAINT PK_CHAT_REACTION PRIMARY KEY (ID),
        CONSTRAINT UQ_CHAT_REACTION_MSG_USER UNIQUE (MESSAGE_ID, USER_ID)
    );
END
GO

/* ------------------------------ Présence (en ligne / dernière connexion) ------------------------------ */

IF OBJECT_ID('dbo.USER_STATUS', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.USER_STATUS
    (
        USER_ID   VARCHAR(100) NOT NULL,
        IS_ONLINE BIT          NULL,
        LAST_SEEN DATETIME2(0) NULL,
        CONSTRAINT PK_USER_STATUS PRIMARY KEY (USER_ID)
    );
END
GO

/* ------------------------------ Index ------------------------------ */

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CHAT_MESSAGE_ROOM_TS' AND object_id = OBJECT_ID('dbo.CHAT_MESSAGE'))
    CREATE INDEX IX_CHAT_MESSAGE_ROOM_TS ON dbo.CHAT_MESSAGE (ROOM_ID, [TIMESTAMP]);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CHAT_MESSAGE_RECEIVER_READ' AND object_id = OBJECT_ID('dbo.CHAT_MESSAGE'))
    CREATE INDEX IX_CHAT_MESSAGE_RECEIVER_READ ON dbo.CHAT_MESSAGE (RECEIVER_ID, IS_READ);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CHAT_ROOM_USER1' AND object_id = OBJECT_ID('dbo.CHAT_ROOM'))
    CREATE INDEX IX_CHAT_ROOM_USER1 ON dbo.CHAT_ROOM (USER1_ID);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CHAT_ROOM_USER2' AND object_id = OBJECT_ID('dbo.CHAT_ROOM'))
    CREATE INDEX IX_CHAT_ROOM_USER2 ON dbo.CHAT_ROOM (USER2_ID);
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_CHAT_REACTION_MESSAGE' AND object_id = OBJECT_ID('dbo.CHAT_REACTION'))
    CREATE INDEX IX_CHAT_REACTION_MESSAGE ON dbo.CHAT_REACTION (MESSAGE_ID);
GO

/* ------------------------------ Contrôle ------------------------------ */

SELECT 'Tables'     AS type_objet, name FROM sys.tables    WHERE name IN ('CHAT_ROOM', 'CHAT_MESSAGE', 'CHAT_REACTION', 'USER_STATUS')
UNION ALL
SELECT 'Séquences'  AS type_objet, name FROM sys.sequences WHERE name IN ('SEQ_CHAT_ROOM', 'CHAT_MSG_SEQ', 'CHAT_REACT_SEQ')
ORDER BY 1, 2;
-- Résultat attendu : 4 tables + 3 séquences = 7 lignes.
GO

PRINT 'Messagerie : objets SQL Server prêts. Redémarrer communicationService.';
GO
