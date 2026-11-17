
CREATE DATABASE spmendoza;
USE spmendoza;

-- =====================================================
-- 1. CIUDAD
-- =====================================================

CREATE TABLE ciudad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    departamento VARCHAR(100) NOT NULL
);

INSERT INTO ciudad (id, nombre, departamento) VALUES
(1, 'Bucaramanga', 'Santander'),
(2, 'Floridablanca', 'Santander'),
(3, 'Bogota', 'Cundinamarca'),
(4, 'Medellin', 'Antioquia'),
(5, 'Cali', 'Valle del Cauca');


-- =====================================================
-- 2. USUARIO
-- =====================================================

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'USUARIO') DEFAULT 'USUARIO',
    estado ENUM('PENDIENTE', 'APROBADO', 'RECHAZADO')
        DEFAULT 'PENDIENTE'
);

-- La contraseña del administrador es: temporal123
-- Se almacena su hash BCrypt.

INSERT INTO usuario (id, nombre, correo, password, rol, estado) VALUES
(1, 'Administrador', 'admin@logitrack.com',
 '$2a$10$RoYLY89B9KCB1ZF04mUV4O.x1FKaXGSogEP32N7jVGSukOETXV2e2',
 'ADMIN', 'APROBADO'),

(2, 'Carlos Rodriguez', 'carlos@logitrack.com',
 '$2a$10$RoYLY89B9KCB1ZF04mUV4O.x1FKaXGSogEP32N7jVGSukOETXV2e2',
 'USUARIO', 'APROBADO'),

(3, 'Laura Martinez', 'laura@logitrack.com',
 '$2a$10$RoYLY89B9KCB1ZF04mUV4O.x1FKaXGSogEP32N7jVGSukOETXV2e2',
 'USUARIO', 'APROBADO'),

(4, 'Andres Gomez', 'andres@logitrack.com',
 '$2a$10$RoYLY89B9KCB1ZF04mUV4O.x1FKaXGSogEP32N7jVGSukOETXV2e2',
 'USUARIO', 'APROBADO'),

(5, 'Maria Lopez', 'maria@logitrack.com',
 '$2a$10$RoYLY89B9KCB1ZF04mUV4O.x1FKaXGSogEP32N7jVGSukOETXV2e2',
 'USUARIO', 'PENDIENTE');


-- =====================================================
-- 3. BODEGA
-- =====================================================

CREATE TABLE bodega (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    capacidad DECIMAL(12,2) NOT NULL,
    ciudad_fk BIGINT NOT NULL,

    CONSTRAINT ciudad_bodega_fk
        FOREIGN KEY (ciudad_fk) REFERENCES ciudad(id)
);

ALTER TABLE bodega
CHANGE COLUMN ubicacion direccion VARCHAR(255) NOT NULL;

INSERT INTO bodega (id, nombre, direccion, capacidad, ciudad_fk) VALUES
(1, 'Bodega Principal', 'Zona Industrial, Bucaramanga', 10000.00, 1),
(2, 'Bodega Norte', 'Carrera 25, Floridablanca', 5000.00, 2),
(3, 'Bodega Central', 'Zona Industrial, Bogota', 8000.00, 3),
(4, 'Bodega Medellin', 'Sector Guayabal, Medellin', 6000.00, 4),
(5, 'Bodega Cali', 'Zona Industrial, Cali', 7000.00, 5);


-- =====================================================
-- 4. MARCA
-- =====================================================

CREATE TABLE marca (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO marca (id, nombre) VALUES
(1, 'Samsung'),
(2, 'Apple'),
(3, 'Lenovo'),
(4, 'Logitech'),
(5, 'HP');


-- =====================================================
-- 5. PRODUCTO
-- =====================================================

CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    marca_fk BIGINT NOT NULL,

    CONSTRAINT marca_producto_fk
        FOREIGN KEY (marca_fk) REFERENCES marca(id)
);

INSERT INTO producto (id, nombre, categoria, precio, marca_fk) VALUES
(1, 'Samsung Galaxy S25', 'Celulares', 3500000.00, 1),
(2, 'iPhone 16', 'Celulares', 4500000.00, 2),
(3, 'Lenovo ThinkPad E14', 'Computadores', 3200000.00, 3),
(4, 'Mouse Logitech M185', 'Perifericos', 65000.00, 4),
(5, 'HP Pavilion 15', 'Computadores', 2800000.00, 5);


-- =====================================================
-- 6. INVENTARIO
-- =====================================================

CREATE TABLE inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bodega_fk BIGINT NOT NULL,
    producto_fk BIGINT NOT NULL,
    stock INT NOT NULL DEFAULT 0,

    CONSTRAINT bodega_inventario_fk
        FOREIGN KEY (bodega_fk) REFERENCES bodega(id),

    CONSTRAINT producto_inventario_fk
        FOREIGN KEY (producto_fk) REFERENCES producto(id),

    UNIQUE (bodega_fk, producto_fk)
);

INSERT INTO inventario (id, bodega_fk, producto_fk, stock) VALUES
(1, 1, 1, 25),
(2, 2, 2, 35),
(3, 3, 3, 15),
(4, 4, 4, 7),
(5, 5, 4, 3);


-- =====================================================
-- 7. MOVIMIENTO
-- =====================================================

CREATE TABLE movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    cantidad INT NOT NULL,

    usuario_fk BIGINT NOT NULL,
    producto_fk BIGINT NOT NULL,
    bodega_origen_fk BIGINT NULL,
    bodega_destino_fk BIGINT NULL,

    CONSTRAINT usuario_movimiento_fk
        FOREIGN KEY (usuario_fk) REFERENCES usuario(id),

    CONSTRAINT producto_movimiento_fk
        FOREIGN KEY (producto_fk) REFERENCES producto(id),

    CONSTRAINT bodega_origen_movimiento_fk
        FOREIGN KEY (bodega_origen_fk) REFERENCES bodega(id),

    CONSTRAINT bodega_destino_movimiento_fk
        FOREIGN KEY (bodega_destino_fk) REFERENCES bodega(id)
);

INSERT INTO movimiento
(id, fecha, tipo, cantidad, usuario_fk, producto_fk,
 bodega_origen_fk, bodega_destino_fk) VALUES

(1, CURRENT_TIMESTAMP, 'ENTRADA', 20, 1, 1, NULL, 1),

(2, CURRENT_TIMESTAMP, 'ENTRADA', 35, 2, 2, NULL, 2),

(3, CURRENT_TIMESTAMP, 'SALIDA', 5, 3, 3, 3, NULL),

(4, CURRENT_TIMESTAMP, 'TRANSFERENCIA', 3, 1, 4, 4, 5),

(5, CURRENT_TIMESTAMP, 'ENTRADA', 5, 4, 1, NULL, 1);


-- =====================================================
-- 8. AUDITORIA
-- =====================================================

CREATE TABLE auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_fk BIGINT NOT NULL,
    entidad VARCHAR(100) NOT NULL,
    valores_anteriores JSON NULL,
    valores_nuevos JSON NULL,

    CONSTRAINT usuario_auditoria_fk
        FOREIGN KEY (usuario_fk) REFERENCES usuario(id)
);

INSERT INTO auditoria
(id, operacion, usuario_fk, entidad,
 valores_anteriores, valores_nuevos) VALUES

(1, 'INSERT', 1, 'Ciudad',
 NULL,
 '{"id": 1, "nombre": "Bucaramanga", "departamento": "Santander"}'),

(2, 'INSERT', 1, 'Marca',
 NULL,
 '{"id": 1, "nombre": "Samsung"}'),

(3, 'INSERT', 1, 'Bodega',
 NULL,
 '{"id": 1, "nombre": "Bodega Principal", "direccion": "Zona Industrial, Bucaramanga"}'),

(4, 'INSERT', 1, 'Producto',
 NULL,
 '{"id": 1, "nombre": "Samsung Galaxy S25", "categoria": "Celulares"}'),

(5, 'INSERT', 1, 'Movimiento',
 NULL,
 '{"id": 1, "tipo": "ENTRADA", "cantidad": 20, "productoId": 1}');

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
