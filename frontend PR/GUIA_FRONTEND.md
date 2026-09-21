# Guía breve del frontend NESS SOFT

Esta guía explica la arquitectura actual del frontend sin conectarlo al backend ni a la base de datos.

## Cómo se organiza

- `pages/`: cada archivo HTML representa una pantalla del CRM.
- `css/styles.css`: reglas compartidas: colores, tipografía, sidebar, botones, tablas y tarjetas.
- `css/<modulo>.css`: estilos particulares de una pantalla o módulo.
- `js/<modulo>.js`: comportamiento de la pantalla, filtros, cálculos y eventos.
- `localStorage`: persistencia temporal del navegador para probar flujos sin backend.

## Pantallas principales

- `Login.html`: valida formato de correo y contraseña y simula el acceso local.
- `dashboard.html`: resumen general y accesos rápidos al CRM.
- `clientes.html`: directorio, filtros, métricas y acciones de clientes.
- `nuevo_cliente.html`: formulario para crear clientes en la vista frontend.
- `cotizaciones.html`: listado, métricas y filtros de propuestas comerciales.
- `nueva_cotizacion.html`: formulario con ítems, IVA y cálculo de totales.
- `oportunidades.html`: resumen frontend de etapas comerciales.
- `Usuarios.html`: administración de cuentas, roles y estados.
- `nuevos_usuarios.html`: formulario de creación de usuarios.

## Flujo comercial

1. Se registra un cliente desde `nuevo_cliente.html`.
2. El cliente aparece en `clientes.html`.
3. Desde un cliente se abre `nueva_cotizacion.html` con su identificador.
4. La cotización se calcula y se guarda localmente.
5. La propuesta aparece en `cotizaciones.html`.
6. Las oportunidades representan el seguimiento comercial pendiente de ampliar.

## Patrón de un controlador

Cada controlador normalmente sigue este orden:

1. Define datos demo o lee datos locales.
2. Espera a que el DOM esté listo.
3. Conecta eventos de inputs, selects y botones.
4. Filtra o transforma la información.
5. Renderiza HTML seguro dentro de la tabla o tarjetas.
6. Actualiza métricas y mensajes de estado.

## Por qué se usa `localStorage`

El proyecto trabaja actualmente solo frontend. `localStorage` permite simular alta, lectura y cambios de registros sin realizar llamadas HTTP. Más adelante puede reemplazarse por servicios sin modificar la estructura visual de las pantallas.

## Reglas visuales

- La sidebar usa la arquitectura común de NESS SOFT y no debe duplicarse en CSS de módulos.
- Los módulos usan `styles.css` como base.
- Los botones principales usan la clase `btn-primary`.
- Los botones secundarios usan `btn-secondary`.
- Las listas desplegables, tarjetas y tablas usan bordes curvos y tonos azules.
- Los estados se muestran mediante badges para facilitar la lectura rápida.
- Las reglas responsive deben mantener el contenido legible en pantallas pequeñas.

## Preparación para backend

Los objetos frontend ya utilizan identificadores y nombres de negocio, por ejemplo:

- `id_cliente`
- `id_cotizacion`
- `id_estado_cliente`
- `id_estado_cotizacion`
- `idRol`
- `username`

Estos nombres sirven como contrato futuro, pero actualmente no existe conexión con API, Spring Boot o PostgreSQL.
