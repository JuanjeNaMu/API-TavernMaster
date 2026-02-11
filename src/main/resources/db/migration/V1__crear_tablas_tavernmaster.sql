-- ============================================
-- TAVERNMASTER - VERSION 1
-- CREACION DE TABLAS SEGUN TU BBDD ORIGINAL
-- ============================================

-- TABLA JUGADOR
CREATE TABLE IF NOT EXISTS JUGADOR (
    id_jugador INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    es_admin BIT(1) DEFAULT 0,
    fecha_nac DATE,
    nombre_jug VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    sexo VARCHAR(20),
    PRIMARY KEY (id_jugador),
    UNIQUE KEY UK_email (email),
    UNIQUE KEY UK_nombre_jug (nombre_jug)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLA CAMPANA
CREATE TABLE IF NOT EXISTS CAMPANA (
    id_cam INT NOT NULL AUTO_INCREMENT,
    encuentros INT DEFAULT 0,
    master VARCHAR(255) NOT NULL,
    proxima_sesion DATE,
    titulo VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_cam)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLA PERSONAJE
CREATE TABLE IF NOT EXISTS PERSONAJE (
    id_per INT NOT NULL AUTO_INCREMENT,
    id_cam INT,
    imagen_base64 LONGTEXT,
    jugador_padre VARCHAR(255) NOT NULL,
    nivel INT DEFAULT 1,
    nombre_per VARCHAR(255) NOT NULL,
    PRIMARY KEY (id_per),
    KEY FK_id_cam (id_cam),
    CONSTRAINT FK_id_cam FOREIGN KEY (id_cam) 
        REFERENCES CAMPANA (id_cam) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
