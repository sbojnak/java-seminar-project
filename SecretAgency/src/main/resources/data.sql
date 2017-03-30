CREATE TABLE MISSION (
   id bigint primary key generated always as identity,
   name varchar(50) not null,
   goal varchar(50) not null,
   location varchar(50) not null,
   description varchar(255) not null);

CREATE TABLE AGENT (
  id bigint primary key generated always as identity,
  name varchar(50) not null,
  birthDate date not null,
  securityLevel int);

CREATE TABLE ASSIGNMENT (
  id bigint primary key generated always as identity,
  agentId bigint REFERENCES AGENT(id) not null,
  missionId bigint  REFERENCES MISSION(id) not null,
  jobCompleted boolean default false);