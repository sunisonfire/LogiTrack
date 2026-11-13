
USE logitrack_danna;

-- ============================================================
-- 1. LIMPIAR DATOS ANTERIORES
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE auditoria;
TRUNCATE TABLE detalle_movimiento;
TRUNCATE TABLE inventario_bodega;
TRUNCATE TABLE movimiento;
TRUNCATE TABLE empleado;
TRUNCATE TABLE administrador;
TRUNCATE TABLE producto;
TRUNCATE TABLE bodega;
TRUNCATE TABLE persona;
TRUNCATE TABLE marca;
TRUNCATE TABLE ciudad;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 2. CIUDADES
-- ============================================================

INSERT INTO ciudad (id, nombre) VALUES
(1, 'Bogota'),
(2, 'Medellin'),
(3, 'Cali'),
(4, 'Barranquilla');

-- ============================================================
-- 3. MARCAS DE MAQUILLAJE
-- ============================================================

INSERT INTO marca (id, nombre) VALUES
(1, 'Maybelline'),
(2, 'Vogue'),
(3, 'Loreal'),
(4, 'NYX'),
(5, 'MAC'),
(6, 'Ruby Rose');

-- ============================================================
-- 4. PERSONAS
-- ============================================================

INSERT INTO persona (id, nombre, apellido, documento, tipo) VALUES
(1, 'Danna', 'Tellez', '1010101010', 'ADMIN'),
(2, 'Anna', 'Gamboa', '1020202020', 'ADMIN'),
(3, 'Isabella', 'Gualdron', '1030303030', 'EMPLEADO'),
(4, 'Sara', 'Ramirez', '1040404040', 'EMPLEADO'),
(5, 'Laura', 'Acevedo', '1050505050', 'EMPLEADO'),
(6, 'Sara', 'Morales', '1060606060', 'EMPLEADO');

-- ============================================================
-- 5. BODEGAS
-- ============================================================

INSERT INTO bodega
(id, nombre, ubicacion, ciudad_id, capacidad, encargado_id)
VALUES
(1, 'Bodega Central', 'Zona Industrial Puente Aranda, Calle 13 # 45-20', 1, 5000, 3),
(2, 'Bodega Antioquia', 'Itagui, Carrera 50 # 35-10', 2, 3500, 4),
(3, 'Bodega Pacifico', 'Yumbo, Via Cali-Yumbo Km 3', 3, 3000, 5),
(4, 'Bodega Caribe', 'Zona Franca, Via 40 # 73-100', 4, 2500, 6);

-- ============================================================
-- 6. ADMINISTRADORES
-- Contraseña de ambos: Admin1234
-- ============================================================

INSERT INTO administrador (id, username, password) VALUES
(1, 'admin_laura', '$2b$10$jQ4hwM2007CSqEuvri18tOP9p/ggoxZNdjz39zqniZ2a0NZmTnkPW'),
(2, 'admin_carlos', '$2b$10$jQ4hwM2007CSqEuvri18tOP9p/ggoxZNdjz39zqniZ2a0NZmTnkPW');

-- ============================================================
-- 7. EMPLEADOS
-- Contraseña de todos: Empleado1234
-- ============================================================

INSERT INTO empleado (id, username, password, bodega_id) VALUES
(3, 'emp_isavs', '$2b$10$PLEeybkUhQUrce2mNU0bROUa5q3mMdc60QXhnHVQ1zrhOavN2rOJe', 1),
(4, 'emp_bravis', '$2b$10$PLEeybkUhQUrce2mNU0bROUa5q3mMdc60QXhnHVQ1zrhOavN2rOJe', 2),
(5, 'emp_lauri', '$2b$10$PLEeybkUhQUrce2mNU0bROUa5q3mMdc60QXhnHVQ1zrhOavN2rOJe', 3),
(6, 'emp_saris', '$2b$10$PLEeybkUhQUrce2mNU0bROUa5q3mMdc60QXhnHVQ1zrhOavN2rOJe', 4);

-- ============================================================
-- 8. PRODUCTOS DE MAQUILLAJE
-- ============================================================

INSERT INTO producto
(id, nombre, descripcion, precio, categoria, marca_id)
VALUES

-- LABIALES
(1, 'Labial SuperStay Matte', 'Labial liquido mate de larga duracion', 45000.00, 'LABIALES', 1),
(2, 'Labial Colorissimo', 'Labial cremoso de acabado satinado', 22000.00, 'LABIALES', 2),

-- GLOSSES
(3, 'Gloss Lifter Gloss', 'Brillo labial hidratante con acido hialuronico', 39000.00, 'GLOSSES', 1),
(4, 'Gloss Butter Gloss', 'Gloss suave con acabado brillante', 35000.00, 'GLOSSES', 4),

-- PESTAÑINAS
(5, 'Pestanina Lash Sensational', 'Mascara de pestanas con efecto volumen', 42000.00, 'PESTAÑINAS', 1),
(6, 'Pestanina Resist', 'Mascara de pestanas resistente al agua', 18000.00, 'PESTAÑINAS', 2),

-- DELINEADORES
(7, 'Delineador Epic Ink', 'Delineador liquido de punta precisa', 38000.00, 'DELINEADORES', 4),
(8, 'Delineador Black Intense', 'Lapiz delineador negro de larga duracion', 16000.00, 'DELINEADORES', 2),

-- BASES
(9, 'Base Fit Me Matte', 'Base liquida de acabado mate natural', 52000.00, 'BASES', 1),
(10, 'Base Infallible Fresh', 'Base de cobertura media y larga duracion', 68000.00, 'BASES', 3),

-- CORRECTORES
(11, 'Corrector Fit Me', 'Corrector liquido de cobertura natural', 35000.00, 'CORRECTORES', 1),
(12, 'Corrector Full Coverage', 'Corrector de alta cobertura', 28000.00, 'CORRECTORES', 6),

-- POLVOS
(13, 'Polvo Compacto Vogue', 'Polvo compacto de acabado natural', 24000.00, 'POLVOS', 2),
(14, 'Polvo HD Finishing', 'Polvo traslucido para sellar maquillaje', 42000.00, 'POLVOS', 4),

-- RUBORES
(15, 'Rubor Soft Blush', 'Rubor compacto de textura suave', 32000.00, 'RUBORES', 6),
(16, 'Rubor Powder Blush', 'Rubor en polvo de acabado luminoso', 125000.00, 'RUBORES', 5),

-- ILUMINADORES
(17, 'Iluminador Born To Glow', 'Iluminador liquido de efecto luminoso', 36000.00, 'ILUMINADORES', 4),
(18, 'Iluminador Master Chrome', 'Iluminador compacto efecto metalico', 48000.00, 'ILUMINADORES', 1),

-- SOMBRAS
(19, 'Paleta Ultimate Shadow', 'Paleta de sombras de tonos variados', 65000.00, 'SOMBRAS', 4),
(20, 'Paleta Nude Obsessions', 'Paleta de sombras en tonos nude', 59000.00, 'SOMBRAS', 4);

-- ============================================================
-- 9. MOVIMIENTOS
-- ============================================================

INSERT INTO movimiento
(id, fecha, tipo, persona_id, bodega_origen_id, bodega_destino_id)
VALUES
(1, '2026-11-01 09:00:00', 'ENTRADA', 1, NULL, 1),
(2, '2026-11-01 10:30:00', 'ENTRADA', 4, NULL, 2),
(3, '2026-11-02 08:45:00', 'ENTRADA', 5, NULL, 3),
(4, '2026-11-02 11:15:00', 'ENTRADA', 6, NULL, 4),
(5, '2026-11-05 14:20:00', 'SALIDA', 3, 1, NULL),
(6, '2026-11-08 09:30:00', 'TRANSFERENCIA', 2, 1, 2),
(7, '2026-11-10 16:00:00', 'SALIDA', 4, 2, NULL),
(8, '2026-11-11 10:10:00', 'TRANSFERENCIA', 1, 3, 4),
(9, '2026-11-12 15:40:00', 'SALIDA', 6, 4, NULL);

-- ============================================================
-- 10. DETALLE DE MOVIMIENTOS
-- ============================================================

INSERT INTO detalle_movimiento
(movimiento_id, producto_id, cantidad)
VALUES

-- M1: ENTRADA A BODEGA CENTRAL
(1, 1, 100),
(1, 2, 80),
(1, 3, 120),
(1, 4, 90),
(1, 5, 70),

-- M2: ENTRADA A BODEGA ANTIOQUIA
(2, 6, 80),
(2, 7, 100),
(2, 8, 90),
(2, 9, 90),
(2, 10, 60),

-- M3: ENTRADA A BODEGA PACIFICO
(3, 11, 60),
(3, 12, 80),
(3, 13, 100),
(3, 14, 70),
(3, 15, 90),

-- M4: ENTRADA A BODEGA CARIBE
(4, 16, 70),
(4, 17, 60),
(4, 18, 60),
(4, 19, 100),
(4, 20, 50),

-- M5: SALIDA DE BODEGA CENTRAL
(5, 1, 10),
(5, 3, 15),
(5, 5, 8),

-- M6: TRANSFERENCIA CENTRAL A ANTIOQUIA
(6, 2, 20),
(6, 4, 15),

-- M7: SALIDA DE BODEGA ANTIOQUIA
(7, 6, 12),
(7, 8, 20),

-- M8: TRANSFERENCIA PACIFICO A CARIBE
(8, 11, 10),
(8, 13, 15),

-- M9: SALIDA DE BODEGA CARIBE
(9, 16, 8),
(9, 18, 12);

-- ============================================================
-- 11. INVENTARIO FINAL
-- ============================================================

INSERT INTO inventario_bodega
(bodega_id, producto_id, cantidad)
VALUES

-- BODEGA CENTRAL
(1, 1, 90),
(1, 2, 60),
(1, 3, 105),
(1, 4, 75),
(1, 5, 62),

-- BODEGA ANTIOQUIA
(2, 2, 20),
(2, 4, 15),
(2, 6, 68),
(2, 7, 100),
(2, 8, 70),
(2, 9, 90),
(2, 10, 60),

-- BODEGA PACIFICO
(3, 11, 50),
(3, 12, 80),
(3, 13, 85),
(3, 14, 70),
(3, 15, 90),

-- BODEGA CARIBE
(4, 11, 10),
(4, 13, 15),
(4, 16, 62),
(4, 17, 60),
(4, 18, 48),
(4, 19, 100),
(4, 20, 50);

-- ============================================================
-- 12. AUDITORIA
-- ============================================================

INSERT INTO auditoria
(tipo_operacion, fecha_hora, persona_id, entidad, entidad_id,
 valores_anteriores, valores_nuevos)
VALUES

-- PRODUCTOS
('INSERT', '2026-10-30 09:00:00', 1, 'Producto', 1, NULL,
 '{"nombre":"Labial SuperStay Matte","precio":45000.00,"categoria":"LABIALES","marcaId":1}'),

('INSERT', '2026-10-30 09:10:00', 1, 'Producto', 3, NULL,
 '{"nombre":"Gloss Lifter Gloss","precio":39000.00,"categoria":"GLOSSES","marcaId":1}'),

('INSERT', '2026-10-30 09:20:00', 1, 'Producto', 9, NULL,
 '{"nombre":"Base Fit Me Matte","precio":52000.00,"categoria":"BASES","marcaId":1}'),

('INSERT', '2026-10-30 09:30:00', 1, 'Producto', 19, NULL,
 '{"nombre":"Paleta Ultimate Shadow","precio":65000.00,"categoria":"SOMBRAS","marcaId":4}'),

-- BODEGAS
('INSERT', '2026-10-30 10:00:00', 1, 'Bodega', 1, NULL,
 '{"nombre":"Bodega Central","ciudadId":1,"capacidad":5000,"encargadoId":3}'),

('INSERT', '2026-10-30 10:15:00', 1, 'Bodega', 2, NULL,
 '{"nombre":"Bodega Antioquia","ciudadId":2,"capacidad":3500,"encargadoId":4}'),

-- MOVIMIENTOS
('INSERT', '2026-11-01 09:00:00', 1, 'Movimiento', 1, NULL,
 '{"tipo":"ENTRADA","bodegaOrigenId":null,"bodegaDestinoId":1}'),

('INSERT', '2026-11-08 09:30:00', 2, 'Movimiento', 6, NULL,
 '{"tipo":"TRANSFERENCIA","bodegaOrigenId":1,"bodegaDestinoId":2}'),

('INSERT', '2026-11-11 10:10:00', 1, 'Movimiento', 8, NULL,
 '{"tipo":"TRANSFERENCIA","bodegaOrigenId":3,"bodegaDestinoId":4}'),

-- ACTUALIZACIONES
('UPDATE', '2026-11-03 11:00:00', 2, 'Producto', 3,
 '{"precio":37000.00}',
 '{"precio":39000.00}'),

('UPDATE', '2026-11-04 16:30:00', 2, 'Producto', 9,
 '{"precio":49000.00}',
 '{"precio":52000.00}'),

('UPDATE', '2026-11-04 17:00:00', 2, 'Bodega', 4,
 '{"encargadoId":null}',
 '{"encargadoId":6}'),

-- ELIMINACION DE PRUEBA
('DELETE', '2026-11-09 13:45:00', 1, 'Ciudad', 99,
 '{"nombre":"Cartagena"}', NULL);

-- ============================================================
-- 13. VERIFICACION GENERAL
-- ============================================================

SELECT 'Ciudades' AS tabla, COUNT(*) AS registros FROM ciudad
UNION ALL
SELECT 'Marcas', COUNT(*) FROM marca
UNION ALL
SELECT 'Personas', COUNT(*) FROM persona
UNION ALL
SELECT 'Bodegas', COUNT(*) FROM bodega
UNION ALL
SELECT 'Administradores', COUNT(*) FROM administrador
UNION ALL
SELECT 'Empleados', COUNT(*) FROM empleado
UNION ALL
SELECT 'Productos', COUNT(*) FROM producto
UNION ALL
SELECT 'Movimientos', COUNT(*) FROM movimiento
UNION ALL
SELECT 'Detalles', COUNT(*) FROM detalle_movimiento
UNION ALL
SELECT 'Inventarios', COUNT(*) FROM inventario_bodega
UNION ALL
SELECT 'Auditorias', COUNT(*) FROM auditoria;

-- CONSULTA DE INVENTARIO POR BODEGA

SELECT
    b.nombre AS bodega,
    p.nombre AS producto,
    p.categoria,
    i.cantidad AS existencias
FROM inventario_bodega i
INNER JOIN bodega b ON i.bodega_id = b.id
INNER JOIN producto p ON i.producto_id = p.id
ORDER BY b.id, p.id;

