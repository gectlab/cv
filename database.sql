CREATE DATABASE IF NOT EXISTS cvdb;
USE cvdb;

CREATE TABLE user(
userid INT PRIMARY KEY,
firstname VARCHAR(50),
lastname VARCHAR(50),
email VARCHAR(100),
phoneno VARCHAR(15),
address VARCHAR(200)
);

CREATE TABLE qualification(
qid INT PRIMARY KEY,
userid INT,
highestqualification VARCHAR(100),
university VARCHAR(100),
yearofgraduation INT,
cgpa DECIMAL(3,2),
FOREIGN KEY(userid) REFERENCES user(userid)
);

CREATE TABLE experience(
eid INT PRIMARY KEY,
userid INT,
nameoforganization VARCHAR(100),
noofyears INT,
designation VARCHAR(100),
startdate DATE,
enddate DATE,
FOREIGN KEY(userid) REFERENCES user(userid)
);

CREATE TABLE skills(
sid INT PRIMARY KEY,
userid INT,
skills VARCHAR(100),
proficiency VARCHAR(50),
FOREIGN KEY(userid) REFERENCES user(userid)
);

CREATE TABLE projects(
pid INT PRIMARY KEY,
userid INT,
noofprojects INT,
titles VARCHAR(200),
FOREIGN KEY(userid) REFERENCES user(userid)
);
