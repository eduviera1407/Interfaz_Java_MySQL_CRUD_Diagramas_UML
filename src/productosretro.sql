drop database productosretro;

CREATE DATABASE productosretro;
use productosretro;
CREATE TABLE botas_retro
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,

    marca  TEXT,
    precio int(255)     NOT NULL
);

CREATE TABLE camisetas_retro
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    marca  TEXT,
    precio int(255)     NOT NULL
);

CREATE TABLE chaquetas_retro
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    marca  TEXT,
    precio int(255)     NOT NULL
);


SELECT * FROM chaquetas_retro
UNION ALL
SELECT * FROM botas_retro
UNION ALL
SELECT * FROM camisetas_retro;
