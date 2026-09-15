-- ============================================================================
-- SEED: seguridad.catalogos_generales
-- Valores base para que comercial.terceros, comercial.productos, etc.
-- tengan FKs válidas desde el primer arranque.
-- ============================================================================

-- Ubicación (PAIS -> DEPARTAMENTO -> CIUDAD, jerárquico vía id_padre)
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('PAIS', 'CO', 'Colombia', 1, true);

INSERT INTO seguridad.catalogos_generales (tipo_catalogo, id_padre, codigo, nombre, orden, estado)
SELECT 'DEPARTAMENTO', id_catalogo, 'CUN', 'Cundinamarca', 1, true
FROM seguridad.catalogos_generales WHERE tipo_catalogo = 'PAIS' AND codigo = 'CO';

INSERT INTO seguridad.catalogos_generales (tipo_catalogo, id_padre, codigo, nombre, orden, estado)
SELECT 'CIUDAD', id_catalogo, '11001', 'Bogotá D.C.', 1, true
FROM seguridad.catalogos_generales WHERE tipo_catalogo = 'DEPARTAMENTO' AND codigo = 'CUN';

-- Tipos de documento
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('TIPO_DOCUMENTO', 'CC', 'Cédula de Ciudadanía', 1, true),
('TIPO_DOCUMENTO', 'CE', 'Cédula de Extranjería', 2, true),
('TIPO_DOCUMENTO', 'NIT', 'NIT', 3, true),
('TIPO_DOCUMENTO', 'PAS', 'Pasaporte', 4, true);

-- Monedas
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('MONEDA', 'COP', 'Peso Colombiano', 1, true),
('MONEDA', 'USD', 'Dólar Americano', 2, true);

-- Unidades de medida
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('UNIDAD_MEDIDA', 'UND', 'Unidad', 1, true),
('UNIDAD_MEDIDA', 'KG', 'Kilogramo', 2, true),
('UNIDAD_MEDIDA', 'LT', 'Litro', 3, true),
('UNIDAD_MEDIDA', 'MT', 'Metro', 4, true);

-- Métodos de pago
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('METODO_PAGO', 'EFECTIVO', 'Efectivo', 1, true),
('METODO_PAGO', 'TRANSFER', 'Transferencia Bancaria', 2, true),
('METODO_PAGO', 'TC', 'Tarjeta de Crédito', 3, true),
('METODO_PAGO', 'TD', 'Tarjeta de Débito', 4, true);

-- Categorías de producto (ejemplo inicial)
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('CATEGORIA_PRODUCTO', 'GEN', 'General', 1, true);

-- Marca (ejemplo inicial)
INSERT INTO seguridad.catalogos_generales (tipo_catalogo, codigo, nombre, orden, estado) VALUES
('MARCA', 'GEN', 'Sin marca', 1, true);
