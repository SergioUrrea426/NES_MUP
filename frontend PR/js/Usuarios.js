const rolesCatalogo = [
  { id: 1, codigo: 'ADSIS', nombre: 'Administrador del sistema' },
  { id: 2, codigo: 'CEO', nombre: 'Gerente' },
  { id: 3, codigo: 'AC', nombre: 'Administrador Comercial' },
  { id: 4, codigo: 'ASC', nombre: 'Asesor Comercial' },
  { id: 5, codigo: 'CE', nombre: 'Cliente Ecommerce' },
  { id: 6, codigo: 'SIS', nombre: 'Sistemas' }
];

const usuariosDemo = [
  { idUsuario: 101, nombres: 'Administrador del Sistema', username: 'admin', email: 'admin@nesssoft.local', idRol: 1, estado: 'ACTIVO', fechaCreacion: '2026-01-15T08:00:00' },
  { idUsuario: 102, nombres: 'Carlos Alberto Mendoza', username: 'cmendoza', email: 'carlos.mendoza@ness.com', telefono: '+57 601 555 0101', celular: '+57 310 444 5566', idRol: 3, estado: 'ACTIVO', fechaCreacion: '2026-02-01T08:00:00' },
  { idUsuario: 103, nombres: 'Laura Sofia Gomez', username: 'lgomez', email: 'laura.gomez@ness.com', celular: '+57 320 777 8899', idRol: 4, estado: 'ACTIVO', fechaCreacion: '2026-03-10T08:00:00' },
  { idUsuario: 104, nombres: 'Andres Felipe Torres', username: 'atorres', email: 'andres.torres@ness.com', celular: '+57 301 222 3344', idRol: 4, estado: 'INACTIVO', fechaCreacion: '2026-04-05T08:00:00' }
];

let usuarios = [...usuariosDemo, ...obtenerUsuariosLocales()];

document.addEventListener('DOMContentLoaded', () => {
  cargarRoles();
  conectarFiltros();
  renderizarUsuarios();
  lucide.createIcons();
});

function obtenerUsuariosLocales() {
  try {
    return JSON.parse(localStorage.getItem('nesssoft.usuarios') || '[]');
  } catch (error) {
    return [];
  }
}

function cargarRoles() {
  const select = document.getElementById('select-rol');
  rolesCatalogo.forEach(rol => {
    const option = document.createElement('option');
    option.value = rol.id;
    option.textContent = `${rol.codigo} - ${rol.nombre}`;
    select.appendChild(option);
  });
}

function conectarFiltros() {
  document.getElementById('input-search').addEventListener('input', renderizarUsuarios);
  document.getElementById('select-rol').addEventListener('change', renderizarUsuarios);
  document.getElementById('select-estado').addEventListener('change', renderizarUsuarios);
}

function renderizarUsuarios() {
  const query = document.getElementById('input-search').value.trim().toLowerCase();
  const rol = document.getElementById('select-rol').value;
  const estado = document.getElementById('select-estado').value;
  const filtrados = usuarios.filter(usuario => {
    const texto = `${usuario.nombres || ''} ${usuario.username || ''} ${usuario.email || ''}`.toLowerCase();
    return texto.includes(query) && (!rol || String(usuario.idRol) === rol) && (!estado || normalizarEstado(usuario) === estado);
  });

  actualizarMetricas();
  document.getElementById('results-label').textContent = `${filtrados.length} resultado${filtrados.length === 1 ? '' : 's'} encontrado${filtrados.length === 1 ? '' : 's'}`;
  const tbody = document.getElementById('tabla-usuarios-body');
  tbody.innerHTML = filtrados.length
    ? filtrados.map(crearFilaUsuario).join('')
    : '<tr><td colspan="7" class="empty-state">No hay usuarios que coincidan con los filtros.</td></tr>';
  lucide.createIcons();
}

function crearFilaUsuario(usuario) {
  const estado = normalizarEstado(usuario);
  const rol = rolesCatalogo.find(item => item.id === Number(usuario.idRol));
  const telefono = usuario.celular || usuario.telefono || 'Sin teléfono';
  const fecha = usuario.fechaCreacion ? new Intl.DateTimeFormat('es-CO').format(new Date(usuario.fechaCreacion)) : 'Pendiente';
  return `<tr>
    <td><strong>${escapeHtml(usuario.nombres || 'Sin nombre')}</strong><small>@${escapeHtml(usuario.username || 'sin-usuario')}</small></td>
    <td>${escapeHtml(usuario.email || 'Sin correo')}</td>
    <td>${escapeHtml(telefono)}</td>
    <td><span class="role-pill">${rol ? rol.codigo : `Rol ${usuario.idRol}`}</span><small>${rol ? rol.nombre : 'Rol configurado'}</small></td>
    <td><span class="status-badge ${estado === 'ACTIVO' ? 'badge-done' : 'badge-danger'}">${estado === 'ACTIVO' ? 'Activo' : 'Inactivo'}</span></td>
    <td>${fecha}</td>
    <td><button class="icon-action ${estado === 'ACTIVO' ? 'danger' : 'success'}" type="button" data-user-action="toggle" data-user-id="${usuario.idUsuario}" title="${estado === 'ACTIVO' ? 'Desactivar cuenta' : 'Activar cuenta'}"><i data-lucide="${estado === 'ACTIVO' ? 'user-x' : 'user-check'}"></i></button></td>
  </tr>`;
}

function normalizarEstado(usuario) {
  return String(usuario.estado || (usuario.activo ? 'ACTIVO' : 'INACTIVO')).toUpperCase();
}

function actualizarMetricas() {
  document.getElementById('metric-total').textContent = usuarios.length;
  document.getElementById('metric-active').textContent = usuarios.filter(usuario => normalizarEstado(usuario) === 'ACTIVO').length;
  document.getElementById('metric-inactive').textContent = usuarios.filter(usuario => normalizarEstado(usuario) !== 'ACTIVO').length;
}

document.addEventListener('click', event => {
  const button = event.target.closest('[data-user-action="toggle"]');
  if (!button) return;
  const usuario = usuarios.find(item => String(item.idUsuario) === button.dataset.userId);
  if (!usuario) return;
  usuario.estado = normalizarEstado(usuario) === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
  guardarUsuariosLocales();
  mostrarAlerta(`Cuenta ${usuario.estado === 'ACTIVO' ? 'activada' : 'desactivada'} en la vista frontend.`, 'success');
  renderizarUsuarios();
});

function guardarUsuariosLocales() {
  const locales = usuarios.filter(usuario => usuario.idUsuario > 1000000000000);
  localStorage.setItem('nesssoft.usuarios', JSON.stringify(locales));
}

function mostrarAlerta(mensaje, tipo) {
  const alertBox = document.getElementById('users-alert');
  alertBox.textContent = mensaje;
  alertBox.className = `users-alert ${tipo}`;
}

function escapeHtml(value) {
  return String(value).replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#039;', '"': '&quot;' }[character]));
}
