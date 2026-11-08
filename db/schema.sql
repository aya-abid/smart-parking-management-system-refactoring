-- Database used by the parking server.
-- mysql -uroot < db/schema.sql

CREATE DATABASE IF NOT EXISTS db1;
USE db1;

CREATE TABLE IF NOT EXISTS admin (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS employee (
    empid      VARCHAR(20) PRIMARY KEY,
    name       VARCHAR(100),
    fathername VARCHAR(100),
    password   VARCHAR(50),
    photo      VARCHAR(255),
    address    VARCHAR(255),
    mobileno   VARCHAR(15)
);

CREATE TABLE IF NOT EXISTS vehiclerent (
    vehicletype VARCHAR(30) PRIMARY KEY,
    rentperhour INT
);

CREATE TABLE IF NOT EXISTS parking (
    pid         INT AUTO_INCREMENT PRIMARY KEY,
    vehicleno   VARCHAR(20),
    vehicletype VARCHAR(30),
    dateofentry VARCHAR(10),
    timeofentry VARCHAR(5),
    employeeid  VARCHAR(20),
    parkingbay  VARCHAR(20),
    location    VARCHAR(10),
    dateofexit  VARCHAR(10),
    timeofexit  VARCHAR(5),
    rent        INT
);

-- default admin account
INSERT IGNORE INTO admin (username, password) VALUES ('admin', 'admin');
