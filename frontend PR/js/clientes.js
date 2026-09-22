// Datos demo con los mismos nombres que usará el futuro modelo de clientes.
const clientesBD = [
  { id_cliente: 1, num_documento: '901345890-1', razon_social: 'Industrias NESS S.A.S.', email: 'contacto@ness.com', telefono: '+57 310 123 4567', tipo_persona: 'JURIDICA', ciudad: 'Bogota D.C.', id_estado_cliente: 2 },
  { id_cliente: 2, num_documento: '1018234567', razon_social: 'Eric Santiago Rios', email: 'santiago.rios@gmail.com', telefono: '+57 300 987 6543', tipo_persona: 'NATURAL', ciudad: 'Medellin', id_estado_cliente: 1 },
  { id_cliente: 3, num_documento: '800123999-5', razon_social: 'Logistica del Caribe Lda.', email: 'operaciones@logicaribe.com', telefono: '+57 320 555 4433', tipo_persona: 'JURIDICA', ciudad: 'Barranquilla', id_estado_cliente: 2 },
  { id_cliente: 4, num_documento: '900555111-2', razon_social: 'Comercializadora Alfa', email: 'ventas@alfa.com', telefono: '+57 301 444 1122', tipo_persona: 'JURIDICA', ciudad: 'Cali', id_estado_cliente: 3 }
];

// Combina datos demo con registros creados durante la sesión del navegador.
let clientes = [...clientesBD, ...obtenerClientesLocales()];

const estadosCliente = {
  1: { label: 'Prospecto', className: 'client-status-prospect' },
  2: { label: 'Activo', className: 'client-status-active' },
  3: { label: 'Inactivo', className: 'client-status-inactive' }
};

// Inicia eventos y primera renderización cuando existe el HTML.
document.addEventListener('DOMContentLoaded', () => {
  conectarFiltros();
  renderizarClientes();
  lucide.createIcons();
});

// Recupera altas locales sin depender de servicios externos.
function obtenerClientesLocales() {
  try {
    return JSON.parse(localStorage.getItem('nesssoft.clientes') || '[]').map(cliente => ({
      ...cliente,
      id_cliente: cliente.id_cliente || cliente.id
    }));
  } catch (error) {
    return [];
  }
}

// Los filtros reaccionan al escribir o cambiar una opción.
function conectarFiltros() {
  document.getElementById('search-cliente').addEventListener('input', renderizarClientes);
  document.getElementById('filter-personeria').addEventListener('change', renderizarClientes);
  document.getElementById('filter-estado').addEventListener('change', renderizarClientes);
}

// Filtra el modelo y actualiza métricas y tabla.
function renderizarClientes() {
  const query = document.getElementById('search-cliente').value.trim().toLowerCase();
  const personeria = document.getElementById('filter-personeria').value;
  const estado = document.getElementById('filter-estado').value;
  const filtrados = clientes.filter(cliente => {
    const texto = `${cliente.razon_social || ''} ${cliente.num_documento || ''} ${cliente.email || ''}`.toLowerCase();
    return texto.includes(query) && (!personeria || cliente.tipo_persona === personeria) && (!estado || String(cliente.id_estado_cliente) === estado);
  });

  actualizarMetricas();
  document.getElementById('clientes-results').textContent = `${filtrados.length} resultado${filtrados.length === 1 ? '' : 's'} encontrado${filtrados.length === 1 ? '' : 's'}`;
  const tbody = document.getElementById('tabla-clientes-body');
  tbody.innerHTML = filtrados.length ? filtrados.map(crearFilaCliente).join('') : '<tr><td colspan="7" class="empty-state">No hay clientes que coincidan con los filtros.</td></tr>';
  lucide.createIcons();
}

// Construye una fila segura con datos visibles y acciones.
function crearFilaCliente(cliente) {
  const estado = estadosCliente[cliente.id_estado_cliente] || estadosCliente[3];
  const tipoPersona = cliente.tipo_persona === 'NATURAL' ? 'Persona natural' : 'Persona jurídica';
  return `<tr>
    <td><strong>${escapeHtml(cliente.razon_social || 'Sin nombre')}</strong><small>${escapeHtml(cliente.email || 'Sin correo')}</small></td>
    <td>${escapeHtml(cliente.num_documento || 'Pendiente')}</td>
    <td><div>${escapeHtml(cliente.telefono || 'Sin teléfono')}</div><small>${escapeHtml(cliente.email || 'Sin correo')}</small></td>
    <td><span class="client-persona-pill">${tipoPersona}</span></td>
    <td>${escapeHtml(cliente.ciudad || 'Sin ciudad')}</td>
    <td><span class="client-status ${estado.className}">${estado.label}</span></td>
    <td><div class="client-actions"><button class="icon-action" type="button" title="Nueva cotización" data-client-action="quote" data-client-id="${cliente.id_cliente}"><i data-lucide="file-plus-2"></i></button><button class="icon-action" type="button" title="Ver ficha" data-client-action="view" data-client-id="${cliente.id_cliente}"><i data-lucide="eye"></i></button></div></td>
  </tr>`;
}

// Calcula los contadores que aparecen en las tarjetas superiores.
function actualizarMetricas() {
  document.getElementById('metric-clientes-total').textContent = clientes.length;
  document.getElementById('metric-clientes-activos').textContent = clientes.filter(cliente => Number(cliente.id_estado_cliente) === 2).length;
  document.getElementById('metric-clientes-prospectos').textContent = clientes.filter(cliente => Number(cliente.id_estado_cliente) === 1).length;
}

// Usa delegación para manejar acciones creadas dinámicamente.
document.addEventListener('click', event => {
  const button = event.target.closest('[data-client-action]');
  if (!button) return;
  const cliente = clientes.find(item => String(item.id_cliente) === button.dataset.clientId);
  if (!cliente) return;
  if (button.dataset.clientAction === 'quote') {
    window.location.href = `cotizaciones.html?cliente=${encodeURIComponent(cliente.id_cliente)}`;
    return;
  }
  mostrarAlerta(`Ficha de ${cliente.razon_social}: vista frontend disponible.`, 'success');
});

// Muestra feedback sin usar ventanas emergentes del navegador.
function mostrarAlerta(mensaje, tipo) {
  const alertBox = document.getElementById('clientes-alert');
  alertBox.textContent = mensaje;
  alertBox.className = `clientes-alert ${tipo}`;
}

// Evita insertar texto de usuario como HTML ejecutable.
function escapeHtml(value) {
  return String(value).replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#039;', '"': '&quot;' }[character]));
}
