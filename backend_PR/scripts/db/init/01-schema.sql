-- ============================================================================
-- NESS-SOFT - ESQUEMA POSTGRESQL ORGANIZADO POR DOMINIOS (SCHEMAS)
-- Basado en el modelo CRM+ERP optimizado (25 tablas, normalizado 3FN)
-- ============================================================================
-- CRITERIO DE ASIGNACIÓN:
--   Cada tabla vive en el schema de su dominio PRINCIPAL (no se duplica
--   ninguna tabla entre schemas: eso violaría la normalización). Cuando un
--   dominio necesita datos de otro, se referencia con FK completamente
--   calificada (schema.tabla) o se expone una VISTA. Esto es exactamente
--   para lo que existen los schemas en PostgreSQL: separar por dominio de
--   negocio y permitir permisos (GRANT) distintos por área, sin fragmentar
--   la base de datos en múltiples bases físicas.
-- ============================================================================

	
-- ============================================================================
-- 0. TIPOS (ENUM) COMPARTIDOS
-- Viven en "public" (schema por defecto) para ser accesibles desde
-- cualquier otro schema sin calificación adicional.
-- ============================================================================




CREATE TYPE tipo_catalogo_enum AS ENUM (
  'PAIS','DEPARTAMENTO','CIUDAD','MONEDA','UNIDAD_MEDIDA',
  'METODO_PAGO','TIPO_DOCUMENTO','CATEGORIA_PRODUCTO','MARCA'
);
CREATE TYPE tipo_tercero_enum AS ENUM ('CLIENTE','PROVEEDOR','AMBOS');
CREATE TYPE tipo_persona_enum AS ENUM ('NATURAL','JURIDICA');
CREATE TYPE etapa_oportunidad_enum AS ENUM ('CARRITO','COTIZACION','PEDIDO','FACTURADO','GANADA','PERDIDA');
CREATE TYPE tipo_actividad_enum AS ENUM ('LLAMADA','CORREO','REUNION','TAREA','NOTA');
CREATE TYPE tipo_entidad_actividad_enum AS ENUM ('TERCERO','OPORTUNIDAD','CASO_SOPORTE');
CREATE TYPE tipo_evento_bitacora_enum AS ENUM ('SEGURIDAD','TRANSACCION');
CREATE TYPE tipo_caso_enum AS ENUM ('PQRS','GARANTIA');
CREATE TYPE origen_medicion_enum AS ENUM ('REAL','PROYECTADO');
CREATE TYPE sentido_kpi_enum AS ENUM ('CRECIENTE','DECRECIENTE');
CREATE TYPE tendencia_enum AS ENUM ('SUBIENDO','BAJANDO','ESTABLE');
CREATE TYPE tipo_entidad_seguimiento_enum AS ENUM ('OBJETIVO','KPI','INICIATIVA');
CREATE TYPE naturaleza_producto_enum AS ENUM ('BIEN','SERVICIO');
CREATE TYPE estado_factura_enum AS ENUM ('EMITIDA','PAGADA','ANULADA','VENCIDA');
CREATE TYPE tipo_nota_enum AS ENUM ('CREDITO','DEBITO');
CREATE TYPE tipo_movimiento_enum AS ENUM ('ENTRADA','SALIDA','TRASLADO','AJUSTE');


-- ============================================================================
-- 1. CREACIÓN DE SCHEMAS
-- ============================================================================

CREATE SCHEMA IF NOT EXISTS seguridad;
CREATE SCHEMA IF NOT EXISTS comercial;
CREATE SCHEMA IF NOT EXISTS ecommerce;
CREATE SCHEMA IF NOT EXISTS postventa;
CREATE SCHEMA IF NOT EXISTS logistica;
CREATE SCHEMA IF NOT EXISTS gerencia;

COMMENT ON SCHEMA seguridad IS 'Autenticación, roles, catálogos maestros, bitácora de eventos y notificaciones del sistema';
COMMENT ON SCHEMA comercial IS 'Terceros (clientes/proveedores), productos, pipeline de ventas, facturación y comisiones';
COMMENT ON SCHEMA ecommerce IS 'Extensiones específicas de venta en línea sobre el pipeline comercial compartido';
COMMENT ON SCHEMA postventa IS 'Casos de soporte: PQRS y garantías';
COMMENT ON SCHEMA logistica IS 'Sucursales/bodegas, compras a proveedores e inventario';
COMMENT ON SCHEMA gerencia IS 'BI estratégico: objetivos, KPIs, mediciones, iniciativas y seguimiento';


-- ============================================================================
-- 2. SCHEMA: seguridad
-- Tablas: roles, usuarios, bitacora_eventos, catalogos_generales,
--         notificaciones
-- Por qué viven aquí: son servicios TRANSVERSALES que consumen todos los
-- demás schemas (autenticación, parámetros del sistema, trazabilidad y
-- notificaciones), no pertenecen a un dominio de negocio específico.
-- ============================================================================

CREATE TABLE seguridad.roles (
    id_rol          SERIAL PRIMARY KEY,
    codigo          VARCHAR(20) UNIQUE NOT NULL,
    nombre          VARCHAR(50) NOT NULL,
    descripcion     VARCHAR(255),
    nivel_jerarquia INT NOT NULL,
    estado          BOOLEAN DEFAULT TRUE,
    fecha_creacion  TIMESTAMP DEFAULT now()
);

CREATE TABLE seguridad.usuarios (
    id_usuario                  SERIAL PRIMARY KEY,
    id_rol                      INT NOT NULL REFERENCES seguridad.roles(id_rol),
    nombres                     VARCHAR(150) NOT NULL,
    username                    VARCHAR(50) UNIQUE NOT NULL,
    email                       VARCHAR(150) UNIQUE NOT NULL,
    password_hash               VARCHAR(255) NOT NULL,
    telefono                    VARCHAR(20),
    celular                     VARCHAR(20),
    foto_perfil                 VARCHAR(255),
    zona_horaria                VARCHAR(50),
    estado                      VARCHAR(15) DEFAULT 'ACTIVO',
    activo_2fa                  BOOLEAN DEFAULT FALSE,
    email_verificado            BOOLEAN DEFAULT FALSE,
    ultimo_acceso               TIMESTAMP,
    fecha_creacion               TIMESTAMP DEFAULT now(),
    creado_por                   INT REFERENCES seguridad.usuarios(id_usuario),
    intentos_fallidos            INT DEFAULT 0,
    bloqueo_hasta                 TIMESTAMP,
    ultimo_cambio_password        TIMESTAMP,
    requiere_cambio_password      BOOLEAN DEFAULT FALSE,
    fecha_actualizacion           TIMESTAMP,
    actualizado_por               INT REFERENCES seguridad.usuarios(id_usuario)
);

CREATE TABLE seguridad.bitacora_eventos (
    id_evento        BIGSERIAL PRIMARY KEY,
    tipo_evento      tipo_evento_bitacora_enum NOT NULL,
    id_usuario       INT REFERENCES seguridad.usuarios(id_usuario),
    modulo           VARCHAR(30),
    entidad          VARCHAR(30),
    id_entidad       INT,
    accion           VARCHAR(20),
    estado_anterior  VARCHAR(30),
    estado_nuevo     VARCHAR(30),
    direccion_ip     VARCHAR(45),
    user_agent       VARCHAR(255),
    sistema_operativo VARCHAR(30),
    navegador        VARCHAR(30),
    dispositivo      VARCHAR(30),
    sesion           UUID,
    token_sesion     UUID,
    severidad        VARCHAR(20),
    hash_integridad  VARCHAR(128),
    observaciones    VARCHAR(255),
    fecha_evento     TIMESTAMP DEFAULT now()
);
CREATE INDEX idx_bitacora_usuario ON seguridad.bitacora_eventos(id_usuario);

CREATE TABLE seguridad.catalogos_generales (
    id_catalogo     SERIAL PRIMARY KEY,
    tipo_catalogo   tipo_catalogo_enum NOT NULL,
    id_padre        INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    codigo          VARCHAR(20),
    nombre          VARCHAR(150) NOT NULL,
    descripcion     VARCHAR(255),
    atributo_extra  VARCHAR(100),
    orden           INT,
    estado          BOOLEAN DEFAULT TRUE,
    UNIQUE(tipo_catalogo, codigo)
);
CREATE INDEX idx_catalogos_tipo ON seguridad.catalogos_generales(tipo_catalogo);

CREATE TABLE seguridad.notificaciones (
    id_notificacion     SERIAL PRIMARY KEY,
    codigo_notificacion VARCHAR(20) UNIQUE,
    tipo_entidad        VARCHAR(20) NOT NULL, -- KPI, OBJETIVO, CASO_SOPORTE, OPORTUNIDAD...
    id_entidad          INT NOT NULL,
    titulo              VARCHAR(150) NOT NULL,
    descripcion         VARCHAR(500),
    categoria           VARCHAR(30),
    tipo_alerta         VARCHAR(30),
    prioridad           VARCHAR(10) DEFAULT 'MEDIA',
    nivel_riesgo        VARCHAR(15),
    id_usuario_destino  INT NOT NULL REFERENCES seguridad.usuarios(id_usuario),
    fecha_generacion    TIMESTAMP DEFAULT now(),
    fecha_vencimiento   DATE,
    estado              VARCHAR(15) DEFAULT 'ACTIVA',
    leida               BOOLEAN DEFAULT FALSE,
    fecha_lectura       TIMESTAMP,
    fecha_atencion      TIMESTAMP,
    accion_recomendada  VARCHAR(255),
    observaciones       VARCHAR(255),
    fecha_creacion      TIMESTAMP DEFAULT now()
);
CREATE INDEX idx_notificaciones_usuario ON seguridad.notificaciones(id_usuario_destino);


-- ============================================================================
-- 3. SCHEMA: comercial
-- Tablas: terceros, contactos, productos, oportunidades,
--         detalle_oportunidad, actividades, pagos, facturas,
--         notas_contables, comisiones
-- Es el núcleo del CRM: a quién le vendo, qué le vendo, en qué etapa va
-- el negocio, qué interacciones tuve y cómo se factura y paga.
-- ============================================================================

CREATE TABLE comercial.terceros (
    id_tercero               SERIAL PRIMARY KEY,
    tipo_tercero             tipo_tercero_enum NOT NULL,
    tipo_persona             tipo_persona_enum NOT NULL,
    id_tipo_documento        INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    numero_documento         VARCHAR(20) NOT NULL,
    digito_verificacion      VARCHAR(1),
    nombres                  VARCHAR(150),
    apellidos                VARCHAR(150),
    razon_social             VARCHAR(200),
    nombre_comercial         VARCHAR(200),
    correo_principal         VARCHAR(150) NOT NULL,
    correo_facturacion       VARCHAR(150),
    telefono_fijo            VARCHAR(20),
    celular                  VARCHAR(20),
    direccion                VARCHAR(200),
    id_ubicacion             INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    regimen_tributario       VARCHAR(50),
    responsabilidad_fiscal   VARCHAR(50),
    responsable_iva          BOOLEAN DEFAULT FALSE,
    sitio_web                VARCHAR(150),
    limite_credito           NUMERIC(15,2) DEFAULT 0,
    dias_credito_cliente     INT DEFAULT 0,
    saldo_actual             NUMERIC(15,2) DEFAULT 0,
    descuento_general        NUMERIC(5,2) DEFAULT 0,
    ecommerce_client         BOOLEAN DEFAULT FALSE,
    acepta_habeas_data       BOOLEAN DEFAULT FALSE,
    fecha_aceptacion_habeas_data TIMESTAMP,
    cupo_credito             NUMERIC(15,2) DEFAULT 0,
    dias_credito_proveedor   INT DEFAULT 0,
    fecha_vinculacion        TIMESTAMP,
    estado                   BOOLEAN DEFAULT TRUE,
    observaciones            VARCHAR(255),
    fecha_creacion            TIMESTAMP DEFAULT now(),
    creado_por                INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion        TIMESTAMP,
    actualizado_por             INT REFERENCES seguridad.usuarios(id_usuario),
    UNIQUE(id_tipo_documento, numero_documento)
);

CREATE TABLE comercial.contactos (
    id_contacto     SERIAL PRIMARY KEY,
    id_tercero      INT NOT NULL REFERENCES comercial.terceros(id_tercero) ON DELETE CASCADE,
    nombre          VARCHAR(150) NOT NULL,
    cargo           VARCHAR(100),
    email           VARCHAR(150),
    telefono        VARCHAR(20),
    celular         VARCHAR(20),
    es_principal    BOOLEAN DEFAULT FALSE,
    estado          BOOLEAN DEFAULT TRUE
);
CREATE INDEX idx_contactos_tercero ON comercial.contactos(id_tercero);

CREATE TABLE comercial.productos (
    id_producto            SERIAL PRIMARY KEY,
    sku                    VARCHAR(30) UNIQUE,
    nombre                 VARCHAR(150) NOT NULL,
    descripcion            VARCHAR(255),
    id_categoria           INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    id_marca               INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    id_unidad_medida       INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    naturaleza             naturaleza_producto_enum,
    precio_base            NUMERIC(15,2) NOT NULL,
    peso                   NUMERIC(10,3),
    largo                  NUMERIC(10,2),
    ancho                  NUMERIC(10,2),
    alto                   NUMERIC(10,2),
    volumen                NUMERIC(12,3),
    maneja_inventario      BOOLEAN DEFAULT TRUE,
    permite_venta          BOOLEAN DEFAULT TRUE,
    permite_compra         BOOLEAN DEFAULT TRUE,
    requiere_lote          BOOLEAN DEFAULT TRUE,
    requiere_serie         BOOLEAN DEFAULT TRUE,
    requiere_vencimiento   BOOLEAN DEFAULT TRUE,
    es_ecommerce           BOOLEAN DEFAULT FALSE,
    imagen_principal       VARCHAR(255),
    estado                 BOOLEAN DEFAULT TRUE,
    observaciones          VARCHAR(255),
    fecha_creacion          TIMESTAMP DEFAULT now(),
    creado_por              INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion      TIMESTAMP,
    actualizado_por           INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_productos_ecommerce ON comercial.productos(es_ecommerce) WHERE es_ecommerce = TRUE;

CREATE TABLE comercial.oportunidades (
    id_oportunidad          SERIAL PRIMARY KEY,
    codigo                  VARCHAR(20) UNIQUE,
    id_tercero              INT NOT NULL REFERENCES comercial.terceros(id_tercero),
    id_vendedor             INT NOT NULL REFERENCES seguridad.usuarios(id_usuario),
    id_sucursal             INT NOT NULL, -- FK a logistica.sucursales (ver sección 6)
    etapa                   etapa_oportunidad_enum DEFAULT 'CARRITO',
    origen                  VARCHAR(20), -- ECOMMERCE, PRESENCIAL, TELEFONO, REFERIDO...
    fecha_creacion          TIMESTAMP DEFAULT now(),
    fecha_cotizacion        TIMESTAMP,
    fecha_pedido            TIMESTAMP,
    fecha_vencimiento_cotizacion TIMESTAMP,
    direccion_entrega       VARCHAR(200),
    id_ubicacion_entrega    INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    persona_recibe          VARCHAR(150),
    telefono_contacto       VARCHAR(20),
    subtotal                NUMERIC(15,2) DEFAULT 0,
    descuento_total         NUMERIC(15,2) DEFAULT 0,
    impuesto_total          NUMERIC(15,2) DEFAULT 0,
    costo_envio             NUMERIC(15,2) DEFAULT 0,
    total                   NUMERIC(15,2) DEFAULT 0,
    id_moneda               INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    tasa_cambio              NUMERIC(15,6) DEFAULT 1,
    condiciones_pago         VARCHAR(20),
    prioridad                VARCHAR(10),
    observaciones             VARCHAR(255),
    notas_internas            VARCHAR(255),
    fecha_aprobacion          TIMESTAMP,
    aprobado_por              INT REFERENCES seguridad.usuarios(id_usuario),
    creado_por                 INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion         TIMESTAMP,
    actualizado_por              INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_oportunidades_tercero ON comercial.oportunidades(id_tercero);
CREATE INDEX idx_oportunidades_etapa ON comercial.oportunidades(etapa);

CREATE TABLE comercial.detalle_oportunidad (
    id_detalle              SERIAL PRIMARY KEY,
    id_oportunidad          INT NOT NULL REFERENCES comercial.oportunidades(id_oportunidad) ON DELETE CASCADE,
    id_producto             INT NOT NULL REFERENCES comercial.productos(id_producto),
    descripcion              VARCHAR(255),
    cantidad                 NUMERIC(12,2) NOT NULL,
    id_unidad_medida         INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    precio_unitario           NUMERIC(15,2) NOT NULL,
    porcentaje_descuento      NUMERIC(5,2) DEFAULT 0,
    valor_descuento           NUMERIC(15,2) DEFAULT 0,
    porcentaje_impuesto       NUMERIC(5,2) DEFAULT 0,
    subtotal                  NUMERIC(15,2) NOT NULL,
    total                     NUMERIC(15,2) NOT NULL,
    stock_disponible          BOOLEAN DEFAULT TRUE,
    estado                    VARCHAR(10) DEFAULT 'ACTIVO',
    observaciones              VARCHAR(255)
);
CREATE INDEX idx_detop_oportunidad ON comercial.detalle_oportunidad(id_oportunidad);

CREATE TABLE comercial.actividades (
    id_actividad            SERIAL PRIMARY KEY,
    tipo_actividad           tipo_actividad_enum NOT NULL,
    tipo_entidad             tipo_entidad_actividad_enum NOT NULL,
    id_entidad               INT NOT NULL, -- referencia a comercial.terceros, comercial.oportunidades o postventa.casos_soporte según tipo_entidad
    asunto                    VARCHAR(150) NOT NULL,
    descripcion                VARCHAR(500),
    fecha_programada           TIMESTAMP,
    fecha_realizada             TIMESTAMP,
    duracion_minutos            INT,
    estado                      VARCHAR(15) DEFAULT 'PENDIENTE',
    resultado                    VARCHAR(255),
    id_usuario_asignado           INT NOT NULL REFERENCES seguridad.usuarios(id_usuario),
    id_usuario_creador             INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_creacion                  TIMESTAMP DEFAULT now(),
    fecha_actualizacion               TIMESTAMP
);
CREATE INDEX idx_actividades_entidad ON comercial.actividades(tipo_entidad, id_entidad);

CREATE TABLE comercial.pagos (
    id_pago              SERIAL PRIMARY KEY,
    numero_pago          INT UNIQUE,
    id_tercero           INT NOT NULL REFERENCES comercial.terceros(id_tercero),
    id_oportunidad       INT REFERENCES comercial.oportunidades(id_oportunidad),
    id_metodo_pago       INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    fecha_pago           TIMESTAMP DEFAULT now(),
    valor_pagado         NUMERIC(15,2) NOT NULL,
    id_moneda            INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    tasa_cambio          NUMERIC(15,6) DEFAULT 1,
    referencia_pago      VARCHAR(50),
    entidad_financiera   VARCHAR(50),
    tipo_pago            VARCHAR(20),
    estado               VARCHAR(15) DEFAULT 'PENDIENTE',
    observaciones        VARCHAR(255),
    comprobante          VARCHAR(255),
    fecha_confirmacion   TIMESTAMP,
    confirmado_por       INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_creacion       TIMESTAMP DEFAULT now(),
    fecha_actualizacion  TIMESTAMP,
    actualizado_por      INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_pagos_tercero ON comercial.pagos(id_tercero);

CREATE TABLE comercial.facturas (
    id_factura           SERIAL PRIMARY KEY,
    numero_factura       INT NOT NULL,
    prefijo              VARCHAR(10),
    id_oportunidad       INT NOT NULL REFERENCES comercial.oportunidades(id_oportunidad),
    id_pago              INT REFERENCES comercial.pagos(id_pago),
    fecha_emision        TIMESTAMP DEFAULT now(),
    fecha_vencimiento    TIMESTAMP,
    estado               estado_factura_enum DEFAULT 'EMITIDA',
    tipo_factura         VARCHAR(20),
    subtotal             NUMERIC(15,2) NOT NULL,
    descuento_total      NUMERIC(15,2) DEFAULT 0,
    impuesto_total       NUMERIC(15,2) DEFAULT 0,
    costo_envio          NUMERIC(15,2) DEFAULT 0,
    total                NUMERIC(15,2) NOT NULL,
    id_moneda            INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    tasa_cambio          NUMERIC(15,6) DEFAULT 1,
    cufe                 VARCHAR(100),
    xml_dian             VARCHAR(255),
    pdf_factura          VARCHAR(255),
    fecha_envio_dian     TIMESTAMP,
    fecha_respuesta_dian TIMESTAMP,
    fecha_creacion       TIMESTAMP DEFAULT now(),
    fecha_actualizacion  TIMESTAMP,
    actualizado_por      INT REFERENCES seguridad.usuarios(id_usuario),
    UNIQUE(prefijo, numero_factura)
);

CREATE TABLE comercial.notas_contables (
    id_nota_contable     SERIAL PRIMARY KEY,
    tipo_nota            tipo_nota_enum NOT NULL,
    numero_nota          INT NOT NULL,
    prefijo              VARCHAR(10),
    id_factura           INT NOT NULL REFERENCES comercial.facturas(id_factura),
    fecha_emision        TIMESTAMP DEFAULT now(),
    motivo               VARCHAR(50),
    descripcion          VARCHAR(255),
    subtotal             NUMERIC(15,2) NOT NULL,
    descuento_total      NUMERIC(15,2) DEFAULT 0,
    impuesto_total       NUMERIC(15,2) DEFAULT 0,
    total                NUMERIC(15,2) NOT NULL,
    afecta_inventario    BOOLEAN DEFAULT FALSE,
    estado               VARCHAR(20) DEFAULT 'EMITIDA',
    cufe_referencia      VARCHAR(100),
    cude                 VARCHAR(100),
    xml_dian             VARCHAR(255),
    pdf_documento        VARCHAR(255),
    fecha_envio_dian     TIMESTAMP,
    fecha_respuesta_dian TIMESTAMP,
    observaciones        VARCHAR(255),
    fecha_creacion       TIMESTAMP DEFAULT now(),
    creado_por           INT REFERENCES seguridad.usuarios(id_usuario),
    UNIQUE(tipo_nota, prefijo, numero_nota)
);

CREATE TABLE comercial.comisiones (
    id_comision          SERIAL PRIMARY KEY,
    id_oportunidad       INT NOT NULL REFERENCES comercial.oportunidades(id_oportunidad),
    id_vendedor          INT NOT NULL REFERENCES seguridad.usuarios(id_usuario),
    tipo_comision        VARCHAR(20),
    porcentaje           NUMERIC(5,2),
    valor_base           NUMERIC(15,2),
    valor_comision       NUMERIC(15,2),
    estado               VARCHAR(20) DEFAULT 'PENDIENTE',
    fecha_generacion     TIMESTAMP DEFAULT now(),
    fecha_pago           TIMESTAMP,
    observaciones        VARCHAR(255),
    fecha_creacion       TIMESTAMP DEFAULT now(),
    creado_por           INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion  TIMESTAMP,
    actualizado_por      INT REFERENCES seguridad.usuarios(id_usuario)
);


-- ============================================================================
-- 4. SCHEMA: ecommerce
-- Tablas: resenas_producto, carritos_abandonados (NUEVAS)
-- Por qué solo estas dos: el "carrito" y el "pedido en línea" ya NO son
-- entidades propias — son la MISMA fila de comercial.oportunidades en
-- etapa='CARRITO' con origen='ECOMMERCE' (así se normalizó: un negocio que
-- avanza de etapa, no documentos duplicados). Duplicar esas tablas aquí
-- rompería la normalización. Lo que sí es genuinamente exclusivo del canal
-- de venta en línea (y no existía en el modelo anterior) son las reseñas de
-- producto y el seguimiento de carritos abandonados para recuperación.
-- ============================================================================

CREATE TABLE ecommerce.resenas_producto (
    id_resena        SERIAL PRIMARY KEY,
    id_producto      INT NOT NULL REFERENCES comercial.productos(id_producto),
    id_tercero       INT NOT NULL REFERENCES comercial.terceros(id_tercero),
    calificacion     INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    titulo           VARCHAR(150),
    comentario       VARCHAR(500),
    estado           VARCHAR(15) DEFAULT 'PUBLICADA', -- PUBLICADA/PENDIENTE_MODERACION/RECHAZADA
    fecha_creacion   TIMESTAMP DEFAULT now(),
    moderado_por     INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_moderacion TIMESTAMP
);
CREATE INDEX idx_resenas_producto ON ecommerce.resenas_producto(id_producto);

CREATE TABLE ecommerce.carritos_abandonados (
    id_seguimiento         SERIAL PRIMARY KEY,
    id_oportunidad         INT NOT NULL REFERENCES comercial.oportunidades(id_oportunidad),
    fecha_deteccion        TIMESTAMP DEFAULT now(), -- momento en que se marcó como abandonado (sin actividad por X horas)
    intentos_recuperacion  INT DEFAULT 0,
    fecha_ultimo_intento   TIMESTAMP,
    canal_recuperacion     VARCHAR(20), -- EMAIL, WHATSAPP, SMS
    recuperado             BOOLEAN DEFAULT FALSE,
    fecha_recuperacion     TIMESTAMP,
    observaciones          VARCHAR(255)
);
CREATE INDEX idx_carritos_abandonados_oportunidad ON ecommerce.carritos_abandonados(id_oportunidad);

-- Vista de conveniencia: expone, sin duplicar datos, las oportunidades que
-- están operando como "carrito de compras en línea" para que el equipo de
-- ecommerce consulte directo desde su schema.
CREATE VIEW ecommerce.carritos_activos AS
SELECT o.*
FROM comercial.oportunidades o
WHERE o.etapa = 'CARRITO' AND o.origen = 'ECOMMERCE';


-- ============================================================================
-- 5. SCHEMA: postventa
-- Tablas: casos_soporte
-- Dominio pequeño y bien acotado a propósito (PQRS + garantías fusionadas).
-- Las actividades de seguimiento a un caso viven en comercial.actividades
-- (tipo_entidad='CASO_SOPORTE'), reutilizando la misma tabla de
-- interacciones en vez de duplicarla aquí.
-- ============================================================================

CREATE TABLE postventa.casos_soporte (
    id_caso              SERIAL PRIMARY KEY,
    tipo_caso            tipo_caso_enum NOT NULL,
    subtipo              VARCHAR(20),
    origen               VARCHAR(20),
    id_tercero           INT NOT NULL REFERENCES comercial.terceros(id_tercero),
    id_factura           INT REFERENCES comercial.facturas(id_factura),
    id_usuario_reporta   INT REFERENCES seguridad.usuarios(id_usuario),
    id_responsable       INT REFERENCES seguridad.usuarios(id_usuario),
    id_sucursal          INT, -- FK a logistica.sucursales (ver sección 6)
    asunto               VARCHAR(150) NOT NULL,
    descripcion          VARCHAR(500),
    motivo               VARCHAR(150),
    prioridad            VARCHAR(10) DEFAULT 'MEDIA',
    estado               VARCHAR(15) DEFAULT 'ABIERTO',
    resultado            VARCHAR(20),
    aplica_inventario    BOOLEAN DEFAULT FALSE,
    aplica_reembolso     BOOLEAN DEFAULT FALSE,
    valor_reembolso      NUMERIC(15,2) DEFAULT 0,
    fecha_reporte        TIMESTAMP DEFAULT now(),
    fecha_asignacion     TIMESTAMP,
    fecha_resolucion     TIMESTAMP,
    fecha_cierre         TIMESTAMP,
    tiempo_respuesta_horas   NUMERIC(8,2),
    tiempo_resolucion_horas  NUMERIC(8,2),
    canal_respuesta          VARCHAR(20),
    satisfaccion_cliente     VARCHAR(15),
    observaciones             VARCHAR(255),
    fecha_creacion             TIMESTAMP DEFAULT now(),
    creado_por                  INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion           TIMESTAMP,
    actualizado_por                INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_casos_tercero ON postventa.casos_soporte(id_tercero);


-- ============================================================================
-- 6. SCHEMA: logistica
-- Tablas: sucursales, ordenes_compra, detalle_oc, movimientos_inventario
-- ============================================================================

CREATE TABLE logistica.sucursales (
    id_sucursal         SERIAL PRIMARY KEY,
    codigo              VARCHAR(10) UNIQUE NOT NULL,
    nombre              VARCHAR(100) NOT NULL,
    es_bodega           BOOLEAN DEFAULT FALSE,
    id_sucursal_padre   INT REFERENCES logistica.sucursales(id_sucursal),
    id_ubicacion        INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    direccion           VARCHAR(150),
    estado              BOOLEAN DEFAULT TRUE
);

-- Ahora sí se completan las FKs que quedaron pendientes en comercial y
-- postventa (se declaran aquí porque logistica.sucursales debía existir
-- primero; PostgreSQL permite FKs entre schemas sin restricción de orden
-- de creación de los schemas en sí, solo de las tablas referenciadas).
ALTER TABLE comercial.oportunidades
    ADD CONSTRAINT fk_oportunidades_sucursal
    FOREIGN KEY (id_sucursal) REFERENCES logistica.sucursales(id_sucursal);

ALTER TABLE postventa.casos_soporte
    ADD CONSTRAINT fk_casos_sucursal
    FOREIGN KEY (id_sucursal) REFERENCES logistica.sucursales(id_sucursal);

CREATE TABLE logistica.ordenes_compra (
    id_orden_compra          SERIAL PRIMARY KEY,
    id_tercero               INT NOT NULL REFERENCES comercial.terceros(id_tercero), -- el proveedor
    numero_orden              INT NOT NULL,
    prefijo                    VARCHAR(10),
    id_usuario_solicitante      INT NOT NULL REFERENCES seguridad.usuarios(id_usuario),
    id_usuario_aprobador         INT REFERENCES seguridad.usuarios(id_usuario),
    id_sucursal                   INT REFERENCES logistica.sucursales(id_sucursal),
    id_bodega_destino              INT REFERENCES logistica.sucursales(id_sucursal),
    fecha_orden                     TIMESTAMP DEFAULT now(),
    fecha_entrega_estimada           TIMESTAMP,
    tipo_compra                       VARCHAR(20),
    estado                             VARCHAR(15) DEFAULT 'PENDIENTE',
    costo_envio                         NUMERIC(15,2) DEFAULT 0,
    otros_cargos                         NUMERIC(15,2) DEFAULT 0,
    id_moneda                             INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    tasa_cambio                            NUMERIC(15,6) DEFAULT 1,
    condiciones_pago                        VARCHAR(20),
    observaciones                            VARCHAR(255),
    fecha_aprobacion                          TIMESTAMP,
    fecha_cierre                               TIMESTAMP,
    creado_por                                  INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_creacion                               TIMESTAMP DEFAULT now(),
    fecha_actualizacion                           TIMESTAMP,
    actualizado_por                                INT REFERENCES seguridad.usuarios(id_usuario),
    UNIQUE(prefijo, numero_orden)
);

CREATE TABLE logistica.detalle_oc (
    id_detalle_oc           SERIAL PRIMARY KEY,
    id_orden_compra         INT NOT NULL REFERENCES logistica.ordenes_compra(id_orden_compra) ON DELETE CASCADE,
    id_producto              INT NOT NULL REFERENCES comercial.productos(id_producto),
    descripcion               VARCHAR(255),
    cantidad_solicitada        NUMERIC(12,2) NOT NULL,
    cantidad_recibida           NUMERIC(12,2) DEFAULT 0,
    cantidad_pendiente           NUMERIC(12,2) GENERATED ALWAYS AS (cantidad_solicitada - cantidad_recibida) STORED,
    id_unidad_medida               INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    costo_unitario                   NUMERIC(15,2) NOT NULL,
    porcentaje_descuento              NUMERIC(5,2) DEFAULT 0,
    valor_descuento                    NUMERIC(15,2) DEFAULT 0,
    porcentaje_impuesto                 NUMERIC(5,2) DEFAULT 0,
    valor_impuesto                       NUMERIC(15,2) DEFAULT 0,
    subtotal                              NUMERIC(15,2) NOT NULL,
    total                                  NUMERIC(15,2) NOT NULL,
    fecha_entrega_estimada                  TIMESTAMP,
    estado                                   VARCHAR(15) DEFAULT 'PENDIENTE',
    observaciones                             VARCHAR(255),
    fecha_creacion                             TIMESTAMP DEFAULT now(),
    creado_por                                  INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion                          TIMESTAMP,
    actualizado_por                               INT REFERENCES seguridad.usuarios(id_usuario)
);

CREATE TABLE logistica.movimientos_inventario (
    id_movimiento         BIGSERIAL PRIMARY KEY,
    numero_movimiento     INT NOT NULL,
    fecha_movimiento      TIMESTAMP DEFAULT now(),
    tipo_movimiento       tipo_movimiento_enum NOT NULL,
    origen_movimiento     VARCHAR(20),
    id_producto           INT NOT NULL REFERENCES comercial.productos(id_producto),
    id_bodega_origen      INT REFERENCES logistica.sucursales(id_sucursal),
    id_bodega_destino     INT REFERENCES logistica.sucursales(id_sucursal),
    lote                  VARCHAR(30),
    serie                 VARCHAR(30),
    cantidad              NUMERIC(12,2) NOT NULL,
    id_unidad_medida      INT NOT NULL REFERENCES seguridad.catalogos_generales(id_catalogo),
    costo_unitario        NUMERIC(15,2),
    costo_total           NUMERIC(15,2),
    saldo_anterior        NUMERIC(12,2),
    saldo_nuevo           NUMERIC(12,2),
    motivo                VARCHAR(50),
    estado                VARCHAR(15) DEFAULT 'CONFIRMADO',
    observaciones         VARCHAR(255),
    fecha_creacion        TIMESTAMP DEFAULT now(),
    creado_por            INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion   TIMESTAMP,
    actualizado_por       INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_movinv_producto ON logistica.movimientos_inventario(id_producto);


-- ============================================================================
-- 7. SCHEMA: gerencia
-- Tablas: objetivos_estrategicos, kpis, mediciones_kpi,
--         iniciativas_estrategicas, seguimientos
-- ============================================================================

CREATE TABLE gerencia.objetivos_estrategicos (
    id_objetivo          SERIAL PRIMARY KEY,
    codigo_objetivo      VARCHAR(20) UNIQUE NOT NULL,
    nombre               VARCHAR(150) NOT NULL,
    descripcion          VARCHAR(500),
    perspectiva          VARCHAR(50),
    categoria            VARCHAR(50),
    prioridad            VARCHAR(10) DEFAULT 'MEDIA',
    estado               VARCHAR(15) DEFAULT 'ACTIVO',
    fecha_inicio         DATE,
    fecha_fin            DATE,
    porcentaje_avance    NUMERIC(5,2) DEFAULT 0,
    meta                 VARCHAR(50),
    id_unidad_medida     INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    id_responsable       INT REFERENCES seguridad.usuarios(id_usuario),
    area                 VARCHAR(50),
    presupuesto          NUMERIC(15,2),
    resultado_actual     VARCHAR(255),
    observaciones        VARCHAR(255),
    fecha_creacion       TIMESTAMP DEFAULT now(),
    creado_por           INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion  TIMESTAMP,
    actualizado_por      INT REFERENCES seguridad.usuarios(id_usuario)
);

CREATE TABLE gerencia.kpis (
    id_kpi                 SERIAL PRIMARY KEY,
    codigo_kpi             VARCHAR(20) UNIQUE NOT NULL,
    id_objetivo            INT NOT NULL REFERENCES gerencia.objetivos_estrategicos(id_objetivo),
    nombre                 VARCHAR(150) NOT NULL,
    descripcion            VARCHAR(500),
    categoria              VARCHAR(50),
    tipo_calculo           VARCHAR(20),
    frecuencia_medicion    VARCHAR(20),
    id_unidad_medida       INT REFERENCES seguridad.catalogos_generales(id_catalogo),
    valor_meta             NUMERIC(15,2),
    valor_actual           NUMERIC(15,2),
    valor_minimo           NUMERIC(15,2),
    valor_maximo           NUMERIC(15,2),
    sentido                sentido_kpi_enum,
    semaforo               VARCHAR(10),
    id_responsable         INT REFERENCES seguridad.usuarios(id_usuario),
    estado                 VARCHAR(15) DEFAULT 'ACTIVO',
    observaciones          VARCHAR(255),
    fecha_inicio           DATE,
    fecha_fin              DATE,
    fecha_ultima_medicion  DATE,
    fecha_creacion         TIMESTAMP DEFAULT now(),
    creado_por             INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion    TIMESTAMP,
    actualizado_por        INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_kpis_objetivo ON gerencia.kpis(id_objetivo);

CREATE TABLE gerencia.mediciones_kpi (
    id_medicion              SERIAL PRIMARY KEY,
    id_kpi                   INT NOT NULL REFERENCES gerencia.kpis(id_kpi) ON DELETE CASCADE,
    origen                   origen_medicion_enum NOT NULL DEFAULT 'REAL',
    periodo                  DATE NOT NULL,
    fecha_medicion           DATE DEFAULT CURRENT_DATE,
    fecha_inicio_periodo     DATE,
    fecha_fin_periodo        DATE,
    valor                    NUMERIC(15,2),
    valor_meta               NUMERIC(15,2),
    porcentaje_cumplimiento  NUMERIC(5,2),
    desviacion               NUMERIC(15,2),
    estado_semaforo          VARCHAR(10),
    tendencia                tendencia_enum,
    escenario                VARCHAR(10),
    margen_error             NUMERIC(6,2),
    nivel_confianza          NUMERIC(5,2),
    metodo_calculo           VARCHAR(50),
    fuente_datos             VARCHAR(150),
    observaciones            VARCHAR(255),
    validado                 BOOLEAN DEFAULT FALSE,
    fecha_validacion         DATE,
    validado_por             INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_creacion           TIMESTAMP DEFAULT now(),
    creado_por               INT REFERENCES seguridad.usuarios(id_usuario)
);
CREATE INDEX idx_medkpi_kpi ON gerencia.mediciones_kpi(id_kpi);

CREATE TABLE gerencia.iniciativas_estrategicas (
    id_iniciativa         SERIAL PRIMARY KEY,
    codigo                VARCHAR(20) UNIQUE NOT NULL,
    titulo                VARCHAR(150) NOT NULL,
    descripcion           VARCHAR(500),
    tipo_iniciativa       VARCHAR(20),
    origen                VARCHAR(30),
    id_objetivo           INT REFERENCES gerencia.objetivos_estrategicos(id_objetivo),
    id_kpi                INT REFERENCES gerencia.kpis(id_kpi),
    id_notificacion       INT REFERENCES seguridad.notificaciones(id_notificacion),
    id_responsable        INT REFERENCES seguridad.usuarios(id_usuario),
    prioridad             VARCHAR(10) DEFAULT 'MEDIA',
    impacto               VARCHAR(15),
    estado                VARCHAR(15) DEFAULT 'PROPUESTA',
    fecha_decision         DATE,
    fecha_inicio           DATE,
    fecha_fin              DATE,
    presupuesto_estimado    NUMERIC(15,2),
    costo_real               NUMERIC(15,2),
    beneficio_estimado        NUMERIC(15,2),
    porcentaje_avance          NUMERIC(5,2) DEFAULT 0,
    observaciones                VARCHAR(255),
    fecha_creacion                TIMESTAMP DEFAULT now(),
    creado_por                     INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion              TIMESTAMP,
    actualizado_por                   INT REFERENCES seguridad.usuarios(id_usuario)
);

CREATE TABLE gerencia.seguimientos (
    id_seguimiento              SERIAL PRIMARY KEY,
    codigo_seguimiento          VARCHAR(20) UNIQUE NOT NULL,
    tipo_entidad                tipo_entidad_seguimiento_enum NOT NULL,
    id_entidad                  INT NOT NULL,
    fecha_seguimiento           DATE DEFAULT CURRENT_DATE,
    estado_anterior              VARCHAR(20),
    estado_nuevo                 VARCHAR(20),
    porcentaje_avance             NUMERIC(5,2),
    descripcion_avance             VARCHAR(500),
    acciones_realizadas             VARCHAR(500),
    dificultades                     VARCHAR(500),
    acciones_pendientes               VARCHAR(500),
    fecha_proximo_seguimiento          DATE,
    id_responsable                      INT REFERENCES seguridad.usuarios(id_usuario),
    requiere_escalamiento                 BOOLEAN DEFAULT FALSE,
    observaciones                          VARCHAR(255),
    fecha_creacion                          TIMESTAMP DEFAULT now(),
    creado_por                               INT REFERENCES seguridad.usuarios(id_usuario),
    fecha_actualizacion                        TIMESTAMP,
    actualizado_por                             INT REFERENCES seguridad.usuarios(id_usuario)
);


-- ============================================================================
-- 8. PERMISOS SUGERIDOS POR SCHEMA (ejemplo de rol de aplicación por área)
-- Ejemplo de cómo los schemas permiten aislar accesos por equipo/rol de BD,
-- algo que no es posible de forma limpia con un solo schema "public".
-- ============================================================================

-- CREATE ROLE app_ventas;
-- GRANT USAGE ON SCHEMA comercial, ecommerce TO app_ventas;
-- GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA comercial TO app_ventas;
-- GRANT SELECT ON ALL TABLES IN SCHEMA seguridad TO app_ventas; -- solo lectura de catálogos/usuarios

-- CREATE ROLE app_logistica;
-- GRANT USAGE ON SCHEMA logistica TO app_logistica;
-- GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA logistica TO app_logistica;
-- GRANT SELECT ON comercial.productos, comercial.terceros TO app_logistica;

-- CREATE ROLE app_gerencia;
-- GRANT USAGE ON SCHEMA gerencia TO app_gerencia;
-- GRANT SELECT ON ALL TABLES IN SCHEMA comercial, logistica, postventa TO app_gerencia; -- lectura amplia para reportes
-- GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA gerencia TO app_gerencia;

-- ============================================================================
-- FIN DEL ESQUEMA
-- ============================================================================