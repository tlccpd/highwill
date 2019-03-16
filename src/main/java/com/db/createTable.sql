﻿
CREATE DATABASE STORE;

GRANT ALL PRIVILEGES ON STORE.* TO store@'%' IDENTIFIED BY 'passme007';

USE STORE;


CREATE TABLE CODE_GROUP (
   GROUP_ID	    CHAR(3) NOT NULL,
   GROUP_NAME   VARCHAR(30) NOT NULL,
   IS_USE       CHAR(1) NOT NULL DEFAULT 'Y',
   DESCR        CHAR(100) NULL,
   PRIMARY KEY (GROUP_ID)
);

CREATE TABLE CODE (
   GROUP_ID	    CHAR(3) NOT NULL,
   CODE_ID      VARCHAR(3) NOT NULL,
   CODE_NAME    VARCHAR(30) NOT NULL,
   SEQ_NUM      INT,
   IS_USE       CHAR(1) NOT NULL DEFAULT 'Y',
   DESCR        VARCHAR(100) NULL,
   UPD_DT       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (GROUP_ID, CODE_ID)
);

CREATE TABLE USER (
   ID	        VARCHAR(20) NOT NULL,
   PASSWD       VARCHAR(32) NOT NULL,
   NAME         VARCHAR(20) NOT NULL,
   EMAIL        VARCHAR(30) NOT NULL,
   ENABLED      CHAR(1)   NOT NULL DEFAULT 'Y',
   ROLE         CHAR(3) NOT NULL DEFAULT 'MEM',
   REG_DT       DATETIME,
   UPD_DT       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (ID)
);

CREATE TABLE FILE_UPLOAD (
   SEQ_NUM	        INT AUTO_INCREMENT,
   USR_ID           VARCHAR(20) NOT NULL,
   PHYSICAL_NAME    VARCHAR(19),
   LOGICAL_NAME     VARCHAR(50),
   DESCR            VARCHAR(100),
   PRIMARY KEY (SEQ_NUM)
);

CREATE TABLE SCHEDULE (
   SCH_ID	        INT AUTO_INCREMENT,
   MONTH            VARCHAR(2),
   DAY              VARCHAR(2),
   HOUR             VARCHAR(2),
   MINUTE           VARCHAR(2),
   BEAN_ID          VARCHAR(50),
   DESCR            VARCHAR(100),
   ENABLED          CHAR(1) NOT NULL DEFAULT 'Y',
   CREATOR          VARCHAR(20),
   EDITOR           VARCHAR(20),
   REG_DT           DATETIME,
   UPD_DT           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (SCH_ID),
   FOREIGN KEY (CREATOR) REFERENCES USER(ID),
   FOREIGN KEY (EDITOR) REFERENCES USER(ID)
);

CREATE TABLE SERVER
(
   SEQ_NUM          INT NOT NULL AUTO_INCREMENT,
   NAME             VARCHAR(30) NOT NULL,
   IP               VARCHAR(15) NOT NULL,
   SCHEDULING       CHAR(1) NOT NULL DEFAULT 'N',
   PRIMARY KEY (SEQ_NUM)
);


INSERT INTO CODE_GROUP(GROUP_ID, GROUP_NAME, DESCR) VALUES('CAT', '카테고리', '자료실 카테고리 분류');
INSERT INTO CODE_GROUP(GROUP_ID, GROUP_NAME, DESCR) VALUES('USR', '사용자', '사용자 타입 구분');

INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM) VALUES('CAT', 'MOV', '비디오', 1);
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM) VALUES('CAT', 'AUD', '오디오', 2);
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM) VALUES('CAT', 'PIC', '사진', 3);
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM) VALUES('CAT', 'DOC', '문서', 4);
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM) VALUES('CAT', 'UTL', '유틸리티', 5);

INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM, DESCR) VALUES('USR', 'MEM', 'MEMBER', 1, '회원');
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM, DESCR) VALUES('USR', 'OPR', 'OPERATOR', 2, '서비스 운영자');
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM, DESCR) VALUES('USR', 'ADM', 'ADMIN', 3, '관리자');
INSERT INTO CODE(GROUP_ID, CODE_ID, CODE_NAME, SEQ_NUM, DESCR) VALUES('USR', 'SUP', 'SUPER', 4, '시스템 운영자');

INSERT INTO SERVER(NAME, IP, SCHEDULING) VALUES('PETER-K', 'front.local', 'N');

COMMIT;
