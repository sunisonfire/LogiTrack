
CREATE DATABASE IF NOT EXISTS logitrack_danna;
USE logitrack_danna;

-- =============================================
-- TABLA: CIUDAD
-- =============================================

CREATE TABLE ciudad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
) ENGINE=InnoDB;


-- =============================================
-- TABLA: PERSONA
-- =============================================

CREATE TABLE persona (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    tipo ENUM('ADMIN','EMPLEADO') NOT NULL,
    UNIQUE KEY uk_persona_id_tipo (id, tipo)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: BODEGA
-- =============================================

CREATE TABLE bodega (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    ciudad_id BIGINT NOT NULL,
    capacidad INT NOT NULL,
    encargado_id BIGINT NULL,

    CONSTRAINT chk_bodega_capacidad
        CHECK (capacidad > 0),

    CONSTRAINT uk_bodega_nombre_ciudad
        UNIQUE (nombre, ciudad_id),

    CONSTRAINT fk_bodega_ciudad
        FOREIGN KEY (ciudad_id) REFERENCES ciudad(id),

    CONSTRAINT fk_bodega_encargado
        FOREIGN KEY (encargado_id) REFERENCES persona(id)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: MARCA
-- =============================================

CREATE TABLE marca (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
) ENGINE=InnoDB;


-- =============================================
-- TABLA: PRODUCTO
-- =============================================

CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100) NULL,
    precio DECIMAL(12,2) NOT NULL,

    categoria ENUM(
        'LABIALES',
        'GLOSSES',
        'PESTANINAS',
        'DELINEADORES',
        'BASES',
        'CORRECTORES',
        'POLVOS',
        'RUBORES',
        'ILUMINADORES',
        'SOMBRAS'
    ) NOT NULL,

    marca_id BIGINT NOT NULL,

    CONSTRAINT chk_producto_precio
        CHECK (precio > 0),

    CONSTRAINT fk_producto_marca
        FOREIGN KEY (marca_id) REFERENCES marca(id)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: ADMINISTRADOR
-- =============================================

CREATE TABLE administrador (
    id BIGINT NOT NULL PRIMARY KEY,
    tipo ENUM('ADMIN','EMPLEADO') NOT NULL DEFAULT 'ADMIN',
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,

    CONSTRAINT chk_admin_tipo
        CHECK (tipo = 'ADMIN'),

    CONSTRAINT fk_admin_persona
        FOREIGN KEY (id, tipo) REFERENCES persona(id, tipo)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: EMPLEADO
-- =============================================

CREATE TABLE empleado (
    id BIGINT NOT NULL PRIMARY KEY,
    tipo ENUM('ADMIN','EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    bodega_id BIGINT NOT NULL,

    CONSTRAINT chk_empleado_tipo
        CHECK (tipo = 'EMPLEADO'),

    CONSTRAINT fk_empleado_persona
        FOREIGN KEY (id, tipo) REFERENCES persona(id, tipo),

    CONSTRAINT fk_empleado_bodega
        FOREIGN KEY (bodega_id) REFERENCES bodega(id)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: INVENTARIO_BODEGA
-- =============================================

CREATE TABLE inventario_bodega (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bodega_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,

    CONSTRAINT uk_inventario_bodega_producto
        UNIQUE (bodega_id, producto_id),

    CONSTRAINT chk_inventario_cantidad
        CHECK (cantidad >= 0),

    CONSTRAINT fk_inventario_bodega
        FOREIGN KEY (bodega_id) REFERENCES bodega(id),

    CONSTRAINT fk_inventario_producto
        FOREIGN KEY (producto_id) REFERENCES producto(id)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: MOVIMIENTO
-- =============================================

CREATE TABLE movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA','SALIDA','TRANSFERENCIA') NOT NULL,
    persona_id BIGINT NOT NULL,
    bodega_origen_id BIGINT NULL,
    bodega_destino_id BIGINT NULL,

    CONSTRAINT chk_movimiento_bodegas CHECK (
        (tipo = 'ENTRADA'
            AND bodega_origen_id IS NULL
            AND bodega_destino_id IS NOT NULL)

        OR

        (tipo = 'SALIDA'
            AND bodega_origen_id IS NOT NULL
            AND bodega_destino_id IS NULL)

        OR

        (tipo = 'TRANSFERENCIA'
            AND bodega_origen_id IS NOT NULL
            AND bodega_destino_id IS NOT NULL
            AND bodega_origen_id <> bodega_destino_id)
    ),

    CONSTRAINT fk_movimiento_persona
        FOREIGN KEY (persona_id) REFERENCES persona(id),

    CONSTRAINT fk_movimiento_origen
        FOREIGN KEY (bodega_origen_id) REFERENCES bodega(id),

    CONSTRAINT fk_movimiento_destino
        FOREIGN KEY (bodega_destino_id) REFERENCES bodega(id),

    INDEX idx_movimiento_fecha (fecha)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: DETALLE_MOVIMIENTO
-- =============================================

CREATE TABLE detalle_movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movimiento_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT fk_detalle_movimiento
        FOREIGN KEY (movimiento_id) REFERENCES movimiento(id),

    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id) REFERENCES producto(id)
) ENGINE=InnoDB;


-- =============================================
-- TABLA: AUDITORIA
-- =============================================

CREATE TABLE auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_operacion ENUM('INSERT','UPDATE','DELETE') NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    persona_id BIGINT NOT NULL,
    entidad VARCHAR(60) NOT NULL,
    entidad_id BIGINT NULL,
    valores_anteriores JSON NULL,
    valores_nuevos JSON NULL,

    CONSTRAINT fk_auditoria_persona
        FOREIGN KEY (persona_id) REFERENCES persona(id),

    INDEX idx_auditoria_persona (persona_id),
    INDEX idx_auditoria_tipo (tipo_operacion),
    INDEX idx_auditoria_fecha (fecha_hora)
) ENGINE=InnoDB;

-- =============================================
-- TABLA: SOLICITUD_REGISTRO
-- =============================================

CREATE TABLE solicitud_registro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    fecha_solicitud DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('PENDIENTE', 'APROBADA', 'RECHAZADA')
        NOT NULL DEFAULT 'PENDIENTE',
    fecha_revision DATETIME NULL,
    revisado_por_id BIGINT NULL,
    motivo_rechazo VARCHAR(255) NULL,

    CONSTRAINT fk_solicitud_revisor
        FOREIGN KEY (revisado_por_id)
        REFERENCES persona(id),

    INDEX idx_solicitud_estado (estado)
) ENGINE=InnoDB;