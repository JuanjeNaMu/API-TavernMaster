-- ============================================
-- TAVERNMASTER - VERSION 2
-- INSERTAR JUGADOR DE PRUEBA (SI NO EXISTE)
-- ============================================

INSERT INTO JUGADOR (nombre_jug, password, email, sexo, EsAdmin)
SELECT 'PruebaFlyway', 'password123', 'prueba@flyway.com', 'Otro', 0
WHERE NOT EXISTS (
    SELECT 1 FROM JUGADOR WHERE email = 'prueba@flyway.com'
);