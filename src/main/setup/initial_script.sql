
/******* 
    Taghunt School - uma plataforma para criação de jogos como ferramenta de apoio à educação
    Copyright (C) 2017 Pedro Fernandes Vieira
 
    Este arquivo é parte da Taghunt School
 
    Taghunt School é um software livre;  você pode redistribuí-lo e/ou modificá-lo sob os
    termos da Licença Pública Geral GNU/GPL como publicada pela Free Software Foundation,
    na versão 3 da Licença, ou (a seu critério) qualquer versão mais recente.
 
    Este programa é distribuído na expectativa de ser útil, mas SEM QUALQUER GARANTIA; sem
    mesmo a garantia implícita de COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER APLICAÇÃO 
    EM PARTICULAR. Veja Licença Pública Geral GNU/GPL em português para maiores detalhes.
 
    Você deve ter recebido uma cópia da Licença Pública Geral GNU/GPL, sob o título “LICENSE”,
    junto com Taghunt School. Se não, acesse <http://www.gnu.org/licenses/>. 

    ---
	Taghunt School - Platform for creating games as a tool to support education
    Copyright (C) 2017 Pedro Fernandes Vieira
	
    This file is part of Taghunt School.

    Taghunt School is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Taghunt School is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Taghunt School.  If not, see <http://www.gnu.org/licenses/>.

*******/  

/* Connect with a superuser like postgres */
CREATE DATABASE taghuntschool
  WITH OWNER = taghuntschool;


-- Table: role_kind
CREATE TABLE role_kind (
	id SERIAL NOT NULL,
	role_kind_authority character varying(100) NOT NULL,
	role_kind_name character varying(100) NOT NULL
);

ALTER TABLE ONLY role_kind
    ADD CONSTRAINT role_kind_pk PRIMARY KEY (id);
	
ALTER TABLE ONLY role_kind
    ADD CONSTRAINT role_kind_name_uk UNIQUE (role_kind_name);
	

INSERT INTO role_kind 
(id, role_kind_authority, role_kind_name) 
VALUES(nextval('role_kind_id_seq'), 'ROLE_ADMIN', 'Manager')
;  

INSERT INTO role_kind 
(id, role_kind_authority, role_kind_name) 
VALUES(nextval('role_kind_id_seq'), 'ROLE_INSTRUCTOR','Instructor')
;  

INSERT INTO role_kind 
(id, role_kind_authority, role_kind_name) 
VALUES(nextval('role_kind_id_seq'), 'ROLE_STUDENT','Student')
;  

-- Table: general_usr
CREATE TABLE general_usr (
    id SERIAL  NOT NULL,
    general_usr_name character varying(100) NOT NULL,
    general_usr_login character varying(30)  NULL,
	general_usr_password character varying(100) NULL,
    general_usr_email character varying(100)  NULL,
    general_usr_enabled boolean  NOT NULL,
    general_usr_key character varying(500) NOT NULL,
    principal_role_kind_fk_id integer  NOT NULL
);

ALTER TABLE ONLY general_usr
    ADD CONSTRAINT general_usr_pk PRIMARY KEY (id);

ALTER TABLE ONLY general_usr
    ADD CONSTRAINT general_usr_login_uk UNIQUE (general_usr_login);
	
ALTER TABLE ONLY general_usr ADD CONSTRAINT general_usr_role_kind_FK 
    FOREIGN KEY (principal_role_kind_fk_id)
    REFERENCES role_kind (id);

-- user: manager      password: 123456 
INSERT INTO general_usr 
(id, general_usr_name, general_usr_login, general_usr_password, general_usr_key, general_usr_enabled, principal_role_kind_fk_id) 
VALUES(nextval('general_usr_id_seq'), 'Manager', 'manager', '$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', 'e8901e70-6207-4479-b585-b2e03de200c8', true, 1)
; 

-- Table: general_usr_role_kind
CREATE TABLE general_usr_role_kind (
	id SERIAL NOT NULL,
    general_usr_id_fk integer  NOT NULL,
	role_kind_id_fk integer NOT NULL
)
;

ALTER TABLE ONLY general_usr_role_kind
    ADD CONSTRAINT general_usr_role_kind_pk PRIMARY KEY (id)
;
																				  
ALTER TABLE ONLY general_usr_role_kind ADD CONSTRAINT general_usr_role_kind_general_usr_FK 
    FOREIGN KEY (general_usr_id_fk)
    REFERENCES general_usr (id)
;
	
ALTER TABLE ONLY general_usr_role_kind ADD CONSTRAINT general_usr_role_kind_role_kind_FK 
    FOREIGN KEY (role_kind_id_fk)
    REFERENCES role_kind (id)
;

ALTER TABLE ONLY general_usr_role_kind
    ADD CONSTRAINT general_usr_role_kind_name_uk UNIQUE (general_usr_id_fk, role_kind_id_fk)
;

ALTER TABLE ONLY general_usr_role_kind ALTER COLUMN id SET DEFAULT nextval('general_usr_role_kind_id_seq'::regclass)
;

INSERT INTO general_usr_role_kind 
(general_usr_id_fk, role_kind_id_fk) 
VALUES( 1, 1)
; 

-- Table: mission
CREATE TABLE mission (
	id SERIAL  NOT NULL,
    mission_title character varying(200)  NOT NULL,
	mission_desc character varying(2000)  NOT NULL,
	mission_local character varying(80)  NOT NULL,
    mission_published boolean  NOT NULL,
    general_usr_id_fk integer  NOT NULL
);

ALTER TABLE ONLY mission
    ADD CONSTRAINT mission_pk PRIMARY KEY (id);

ALTER TABLE ONLY mission
    ADD CONSTRAINT mission_title_uk UNIQUE (mission_title);
		
ALTER TABLE ONLY mission
    ADD CONSTRAINT general_usr_mission_FK FOREIGN KEY (general_usr_id_fk) REFERENCES general_usr (id);

  
-- Table: mission_steps
CREATE TABLE mission_steps (
	id SERIAL  NOT NULL,
    mission_fk_id integer  NOT NULL,
    mission_steps_tip character varying(2000)  NOT NULL,
    mission_steps_thing_name character varying(500)  NOT NULL,
    mission_steps_thing_key character varying(500)  NOT NULL
);

ALTER TABLE ONLY mission_steps
    ADD CONSTRAINT mission_steps_pk PRIMARY KEY (id);

ALTER TABLE ONLY mission_steps
    ADD CONSTRAINT mission_steps_thing_key_uk UNIQUE (mission_steps_thing_key);
	
ALTER TABLE ONLY mission_steps
    ADD CONSTRAINT mission_steps_mission_fk FOREIGN KEY (mission_fk_id) REFERENCES mission(id);

-- Table: general_usr_mission
CREATE TABLE general_usr_mission (
    id SERIAL  NOT NULL,
    mission_id_fk integer  NOT NULL,
    general_usr_id_fk integer  NOT NULL,
	mission_finished boolean  NOT NULL
);

ALTER TABLE ONLY general_usr_mission
    ADD CONSTRAINT general_usr_mission_pk PRIMARY KEY (id);
	
ALTER TABLE ONLY general_usr_mission
    ADD CONSTRAINT  general_usr_mission_mission_fk FOREIGN KEY (mission_id_fk) REFERENCES mission(id);

ALTER TABLE ONLY general_usr_mission
    ADD CONSTRAINT general_usr_mission_general_usr_FK FOREIGN KEY (general_usr_id_fk) REFERENCES general_usr (id);
	
	
-- Table: general_usr_mission_steps
CREATE TABLE general_usr_mission_steps (
	id SERIAL  NOT NULL,
    mission_steps_fk_id integer  NOT NULL,
    general_usr_mission_id_fk integer  NOT NULL,
    general_usr_mission_steps_ord integer NOT NULL,
    general_usr_mission_steps_complete timestamp  NULL
);

ALTER TABLE ONLY general_usr_mission_steps
    ADD CONSTRAINT general_usr_mission_steps_pk PRIMARY KEY (id);
	
ALTER TABLE ONLY general_usr_mission_steps
    ADD CONSTRAINT general_usr_mission_steps_mission_steps_fk FOREIGN KEY (mission_steps_fk_id) REFERENCES mission_steps(id);

ALTER TABLE ONLY general_usr_mission_steps
    ADD CONSTRAINT general_usr_mission_steps_general_usr_mission_FK FOREIGN KEY (general_usr_mission_id_fk) REFERENCES general_usr_mission (id);
