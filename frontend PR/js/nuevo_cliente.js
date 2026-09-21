/* NESS SOFT - NUEVO CLIENTE FRONTEND CONTROLLER */

document.addEventListener('DOMContentLoaded', () => {
  const campoFecha = document.getElementById('cli-fecha');
  const tipoPersona = document.getElementById('cli-tipo-persona');
  const formulario = document.getElementById('form-crear-cliente');

  if (campoFecha) {
    campoFecha.value = new Intl.DateTimeFormat('es-CO', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }).format(new Date());
  }

  if (tipoPersona) {
    tipoPersona.addEventListener('change', cambiarEtiquetaDocumento);
    cambiarEtiquetaDocumento();
  }

  if (formulario) {
    formulario.addEventListener('submit', guardarClienteFrontend);
  }
});

function cambiarEtiquetaDocumento() {
  const tipoPersona = document.getElementById('cli-tipo-persona').value;
  const lblDoc = document.getElementById('lbl-num-doc');
  const lblNombre = document.getElementById('lbl-razon-social');

  if (tipoPersona === 'NATURAL') {
    lblDoc.innerText = 'Cédula / Documento (Único) *';
    lblNombre.innerText = 'Nombre Completo *';
  } else {
    lblDoc.innerText = 'NIT / Documento (Único) *';
    lblNombre.innerText = 'Razón Social *';
  }
}

function guardarClienteFrontend(event) {
  event.preventDefault();

  const tipo_persona = document.getElementById('cli-tipo-persona').value;
  const num_documento = document.getElementById('cli-num-doc').value.trim();
  const razon_social = document.getElementById('cli-razon-social').value.trim();
  const email = document.getElementById('cli-email').value.trim().toLowerCase();
  const telefono = document.getElementById('cli-telefono').value.trim();
  const id_estado_cliente = parseInt(document.getElementById('cli-estado').value);
  const ciudad = document.getElementById('cli-ciudad').value.trim();
  const direccion = document.getElementById('cli-direccion').value.trim();

  const cliente = {
    tipo_persona,
    num_documento,
    razon_social,
    email,
    telefono,
    id_estado_cliente,
    ciudad,
    direccion: direccion || null,
    fecha_registro: new Date().toISOString()
  };

  const clientesGuardados = JSON.parse(localStorage.getItem('nesssoft.clientes') || '[]');
  clientesGuardados.push({ ...cliente, id_cliente: Date.now() });
  localStorage.setItem('nesssoft.clientes', JSON.stringify(clientesGuardados));

  const submitButton = document.querySelector('#form-crear-cliente button[type="submit"]');
  if (submitButton) {
    submitButton.classList.add('btn-loading');
    submitButton.disabled = true;
  }

  mostrarAlerta('Cliente guardado en la vista frontend. Redirigiendo...', 'success');

  setTimeout(() => {
    window.location.href = 'clientes.html';
  }, 1200);
}

function mostrarAlerta(mensaje, tipo) {
  const alertBox = document.getElementById('alert-box');
  alertBox.innerText = mensaje;
  alertBox.className = `alert-message alert-${tipo}`;
  alertBox.style.display = 'block';

  if (tipo === 'error') {
    setTimeout(() => {
      alertBox.style.display = 'none';
    }, 3500);
  }
}