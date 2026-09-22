// Estado visual del resumen; se alimenta de datos locales mientras no existe API.
const dashboardData = {
  clientes: 0,
  cotizaciones: 0,
  oportunidades: 0,
  ventas: 0
};

// Espera al HTML para actualizar métricas y conectar la búsqueda.
document.addEventListener('DOMContentLoaded', () => {
  cargarResumenLocal();
  actualizarMetricas();
  conectarBusqueda();
});

// Lee módulos locales y conserva valores demo cuando aún no hay registros.
function cargarResumenLocal() {
  const clientes = leerListaLocal('nesssoft.clientes');
  const cotizaciones = leerListaLocal('nesssoft.cotizaciones');
  const usuarios = leerListaLocal('nesssoft.usuarios');

  dashboardData.clientes = clientes.length;
  dashboardData.cotizaciones = cotizaciones.length;
  dashboardData.oportunidades = 0;
  dashboardData.ventas = cotizaciones
    .filter(cotizacion => Number(cotizacion.id_estado_cotizacion) === 3)
    .reduce((total, cotizacion) => total + Number(cotizacion.total || 0), 0);

  if (dashboardData.clientes === 0) dashboardData.clientes = 1254;
  if (dashboardData.cotizaciones === 0) dashboardData.cotizaciones = 89;
  if (dashboardData.oportunidades === 0) dashboardData.oportunidades = 34;
  if (dashboardData.ventas === 0) dashboardData.ventas = 125800000;
}

// Pinta los valores del resumen usando los IDs del HTML.
function actualizarMetricas() {
  document.getElementById('dashboard-clientes-total').textContent = dashboardData.clientes.toLocaleString('es-CO');
  document.getElementById('dashboard-cotizaciones-total').textContent = dashboardData.cotizaciones.toLocaleString('es-CO');
  document.getElementById('dashboard-oportunidades-total').textContent = dashboardData.oportunidades.toLocaleString('es-CO');
  document.getElementById('dashboard-ventas-total').textContent = formatearMonedaCompacta(dashboardData.ventas);
}

// Filtra los paneles informativos sin recargar la página.
function conectarBusqueda() {
  const searchInput = document.getElementById('dashboard-search');
  const links = [...document.querySelectorAll('.dashboard-info-card')];
  if (!searchInput) return;

  searchInput.addEventListener('input', event => {
    const query = event.target.value.trim().toLowerCase();
    links.forEach(link => {
      link.hidden = Boolean(query) && !link.textContent.toLowerCase().includes(query);
    });
  });
}

// Lee una lista del navegador y evita que un dato inválido rompa la vista.
function leerListaLocal(key) {
  try {
    const value = JSON.parse(localStorage.getItem(key) || '[]');
    return Array.isArray(value) ? value : [];
  } catch (error) {
    return [];
  }
}

// Convierte valores grandes a un formato corto y legible.
function formatearMonedaCompacta(value) {
  if (value >= 1000000) return `$ ${(value / 1000000).toFixed(1)}M`;
  return `$ ${Number(value).toLocaleString('es-CO')}`;
}
