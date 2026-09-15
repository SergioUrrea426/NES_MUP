-- ============================================================================
-- SEED: logistica.sucursales
-- ============================================================================

INSERT INTO logistica.sucursales (codigo, nombre, es_bodega, id_ubicacion, direccion, estado)
SELECT 'PRIN', 'Sede Principal', false, id_catalogo, 'Por definir', true
FROM seguridad.catalogos_generales WHERE tipo_catalogo = 'CIUDAD' AND codigo = '11001';

INSERT INTO logistica.sucursales (codigo, nombre, es_bodega, id_ubicacion, direccion, estado)
SELECT 'BOD01', 'Bodega Principal', true, id_catalogo, 'Por definir', true
FROM seguridad.catalogos_generales WHERE tipo_catalogo = 'CIUDAD' AND codigo = '11001';
