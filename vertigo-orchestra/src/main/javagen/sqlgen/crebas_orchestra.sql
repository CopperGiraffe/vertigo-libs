-- ============================================================
--   SGBD      		  :  PostgreSql                     
-- ============================================================




-- ============================================================
--   Sequences                                      
-- ============================================================
create sequence SEQ_O_JOB_CRON
	start with 1000 cache 20; 

create sequence SEQ_O_JOB_EXEC
	start with 1000 cache 20; 

create sequence SEQ_O_JOB_EXECUTION
	start with 1000 cache 20; 

create sequence SEQ_O_JOB_LOG
	start with 1000 cache 20; 

create sequence SEQ_O_JOB_MODEL
	start with 1000 cache 20; 

create sequence SEQ_O_JOB_RUN
	start with 1000 cache 20; 

create sequence SEQ_O_JOB_SCHEDULE
	start with 1000 cache 20; 

create sequence SEQ_O_USER
	start with 1000 cache 20; 


-- ============================================================
--   Table : O_JOB_CRON                                        
-- ============================================================
create table O_JOB_CRON
(
    JCR_ID      	 NUMERIC     	not null,
    CRON_EXPRESSION	 VARCHAR(100)	not null,
    PARAMS      	 TEXT        	not null,
    JMO_ID      	 NUMERIC     	not null,
    constraint PK_O_JOB_CRON primary key (JCR_ID)
);

comment on column O_JOB_CRON.JCR_ID is
'id';

comment on column O_JOB_CRON.CRON_EXPRESSION is
'cron expression';

comment on column O_JOB_CRON.PARAMS is
'init params as JSON';

comment on column O_JOB_CRON.JMO_ID is
'JobModel';

-- ============================================================
--   Table : O_JOB_EXEC                                        
-- ============================================================
create table O_JOB_EXEC
(
    JID         	 VARCHAR(30) 	not null,
    JOB_NAME    	 VARCHAR(100)	not null,
    NODE_ID     	 NUMERIC     	not null,
    START_EXEC_DATE	 TIMESTAMP   	not null,
    MAX_EXEC_DATE	 TIMESTAMP   	not null,
    USR_ID      	 NUMERIC     	,
    constraint PK_O_JOB_EXEC primary key (JID)
);

comment on column O_JOB_EXEC.JID is
'Id';

comment on column O_JOB_EXEC.JOB_NAME is
'Job Name';

comment on column O_JOB_EXEC.NODE_ID is
'Node Id';

comment on column O_JOB_EXEC.START_EXEC_DATE is
'start exec date';

comment on column O_JOB_EXEC.MAX_EXEC_DATE is
'max date Max execution (start + timeout)';

comment on column O_JOB_EXEC.USR_ID is
'User';

-- ============================================================
--   Table : O_JOB_EXECUTION                                        
-- ============================================================
create table O_JOB_EXECUTION
(
    JEX_ID      	 NUMERIC     	not null,
    JOBNAME     	 VARCHAR(20) 	not null,
    STATUS      	 VARCHAR(20) 	not null,
    REASON      	 VARCHAR(20) 	,
    DATE_DEBUT  	 TIMESTAMP   	not null,
    DATE_FIN    	 TIMESTAMP   	,
    CLASS_ENGINE	 VARCHAR(200)	not null,
    WORKSPACE_IN	 TEXT        	not null,
    WORKSPACE_OUT	 TEXT        	,
    NOD_ID      	 NUMERIC     	not null,
    constraint PK_O_JOB_EXECUTION primary key (JEX_ID)
);

comment on column O_JOB_EXECUTION.JEX_ID is
'Id d''une trace d''execution d''un job';

comment on column O_JOB_EXECUTION.JOBNAME is
'Status général d''execution';

comment on column O_JOB_EXECUTION.STATUS is
'Status général d''execution';

comment on column O_JOB_EXECUTION.REASON is
'Code d''erreur fonctionel de l''execution';

comment on column O_JOB_EXECUTION.DATE_DEBUT is
'Date de début d''execution';

comment on column O_JOB_EXECUTION.DATE_FIN is
'Date de fin d''execution';

comment on column O_JOB_EXECUTION.CLASS_ENGINE is
'Implémentation effective de l''execution';

comment on column O_JOB_EXECUTION.WORKSPACE_IN is
'Workspace d''entrée de l''execution';

comment on column O_JOB_EXECUTION.WORKSPACE_OUT is
'Workspace de sortie de l''execution';

comment on column O_JOB_EXECUTION.NOD_ID is
'Id du noeud';

-- ============================================================
--   Table : O_JOB_LOG                                        
-- ============================================================
create table O_JOB_LOG
(
    JLO_ID      	 NUMERIC     	not null,
    DATE_TRACE  	 TIMESTAMP   	not null,
    LEVEL       	 VARCHAR(20) 	not null,
    TYPE_EXEC_CD	 VARCHAR(20) 	not null,
    MESSAGE     	 TEXT        	,
    PARAMETRE   	 TEXT        	,
    ERREUR      	 TEXT        	,
    PRO_ID      	 NUMERIC     	not null,
    constraint PK_O_JOB_LOG primary key (JLO_ID)
);

comment on column O_JOB_LOG.JLO_ID is
'Id d''une trace d''execution d''un job';

comment on column O_JOB_LOG.DATE_TRACE is
'Date de la trace';

comment on column O_JOB_LOG.LEVEL is
'Niveau de la trace';

comment on column O_JOB_LOG.TYPE_EXEC_CD is
'Type de trace';

comment on column O_JOB_LOG.MESSAGE is
'Message';

comment on column O_JOB_LOG.PARAMETRE is
'Paramètre';

comment on column O_JOB_LOG.ERREUR is
'Stacktrace d''erreur';

comment on column O_JOB_LOG.PRO_ID is
'JobExecution';

-- ============================================================
--   Table : O_JOB_MODEL                                        
-- ============================================================
create table O_JOB_MODEL
(
    JMO_ID      	 NUMERIC     	not null,
    JOB_NAME    	 VARCHAR(100)	not null,
    DESC        	 VARCHAR(100)	not null,
    CLASS_ENGINE	 VARCHAR(200)	not null,
    MAX_RETRY   	 NUMERIC     	not null,
    RUN_MAX_DELAY	 NUMERIC     	not null,
    EXEC_TIMEOUT	 NUMERIC     	not null,
    CREATION_DATE	 TIMESTAMP   	not null,
    ACTIVE      	 BOOL        	not null,
    constraint PK_O_JOB_MODEL primary key (JMO_ID)
);

comment on column O_JOB_MODEL.JMO_ID is
'id';

comment on column O_JOB_MODEL.JOB_NAME is
'Name';

comment on column O_JOB_MODEL.DESC is
'Description';

comment on column O_JOB_MODEL.CLASS_ENGINE is
'Class of the Job';

comment on column O_JOB_MODEL.MAX_RETRY is
'Max retry limit';

comment on column O_JOB_MODEL.RUN_MAX_DELAY is
'Max delay in seconds of all executions from scheduled date';

comment on column O_JOB_MODEL.EXEC_TIMEOUT is
'Timeout in seconds of a single execution';

comment on column O_JOB_MODEL.CREATION_DATE is
'Creation date';

comment on column O_JOB_MODEL.ACTIVE is
'Active/Inactive';

-- ============================================================
--   Table : O_JOB_RUN                                        
-- ============================================================
create table O_JOB_RUN
(
    JID         	 VARCHAR(30) 	not null,
    STATUS      	 VARCHAR(1)  	not null,
    NODE_ID     	 NUMERIC     	not null,
    MAX_DATE    	 TIMESTAMP   	not null,
    MAX_RETRY   	 NUMERIC     	not null,
    CURRENT_TRY 	 NUMERIC     	not null,
    constraint PK_O_JOB_RUN primary key (JID)
);

comment on column O_JOB_RUN.JID is
'Id';

comment on column O_JOB_RUN.STATUS is
'Exec status';

comment on column O_JOB_RUN.NODE_ID is
'Node Id';

comment on column O_JOB_RUN.MAX_DATE is
'Max date of the run';

comment on column O_JOB_RUN.MAX_RETRY is
'Max retry';

comment on column O_JOB_RUN.CURRENT_TRY is
'Current try';

-- ============================================================
--   Table : O_JOB_SCHEDULE                                        
-- ============================================================
create table O_JOB_SCHEDULE
(
    JSC_ID      	 NUMERIC     	not null,
    SCHEDULE_DATE	 TIMESTAMP   	not null,
    PARAMS      	 TEXT        	not null,
    JMO_ID      	 NUMERIC     	not null,
    constraint PK_O_JOB_SCHEDULE primary key (JSC_ID)
);

comment on column O_JOB_SCHEDULE.JSC_ID is
'id';

comment on column O_JOB_SCHEDULE.SCHEDULE_DATE is
'schedule date';

comment on column O_JOB_SCHEDULE.PARAMS is
'init params as JSON';

comment on column O_JOB_SCHEDULE.JMO_ID is
'JobModel';

-- ============================================================
--   Table : O_USER                                        
-- ============================================================
create table O_USER
(
    USR_ID      	 NUMERIC     	not null,
    FIRST_NAME  	 VARCHAR(100)	,
    LAST_NAME   	 VARCHAR(100)	,
    EMAIL       	 VARCHAR(100)	,
    PASSWORD    	 VARCHAR(100)	,
    MAIL_ALERT  	 BOOL        	,
    ACTIVE      	 BOOL        	,
    constraint PK_O_USER primary key (USR_ID)
);

comment on column O_USER.USR_ID is
'Id';

comment on column O_USER.FIRST_NAME is
'Nom';

comment on column O_USER.LAST_NAME is
'Prénom';

comment on column O_USER.EMAIL is
'Email';

comment on column O_USER.PASSWORD is
'Mot de passe';

comment on column O_USER.MAIL_ALERT is
'Alerté en cas d''erreur';

comment on column O_USER.ACTIVE is
'Compte Actif';



alter table O_JOB_CRON
	add constraint FK_JCR_JMO_O_JOB_MODEL foreign key (JMO_ID)
	references O_JOB_MODEL (JMO_ID);

create index JCR_JMO_O_JOB_MODEL_FK on O_JOB_CRON (JMO_ID asc);

alter table O_JOB_LOG
	add constraint FK_JLO_JEX_O_JOB_EXECUTION foreign key (PRO_ID)
	references O_JOB_EXECUTION (JEX_ID);

create index JLO_JEX_O_JOB_EXECUTION_FK on O_JOB_LOG (PRO_ID asc);

alter table O_JOB_EXEC
	add constraint FK_JOB_USR_O_USER foreign key (USR_ID)
	references O_USER (USR_ID);

create index JOB_USR_O_USER_FK on O_JOB_EXEC (USR_ID asc);

alter table O_JOB_SCHEDULE
	add constraint FK_JSC_JMO_O_JOB_MODEL foreign key (JMO_ID)
	references O_JOB_MODEL (JMO_ID);

create index JSC_JMO_O_JOB_MODEL_FK on O_JOB_SCHEDULE (JMO_ID asc);


