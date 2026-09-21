/* ==========================================================
  NESS SOFT - NUEVOS USUARIOS CONTROLLER (js/nuevos_usuarios.js)
   ========================================================== */

const userForm = document.getElementById('form-nuevos-usuarios');

userForm.addEventListener('submit', guardarNuevoUsuario);

function guardarNuevoUsuario(event) {
  event.preventDefault();

  const nombres = document.getElementById('usr-nombres').value.trim();
  const username = document.getElementById('usr-username').value.trim().toLowerCase();
  const email = document.getElementById('usr-email').value.trim().toLowerCase();
  const idRol = Number(document.getElementById('usr-rol').value);
  const telefono = document.getElementById('usr-telefono').value.trim();
  const celular = document.getElementById('usr-celular').value.trim();
  const zonaHoraria = document.getElementById('usr-zona-horaria').value;
  const pass = document.getElementById('usr-pass').value;
  const passConfirm = document.getElementById('usr-pass-confirm').value;
  const submitButton = userForm.querySelector('button[type="submit"]');

  if (pass.length < 6) {
    mostrarAlertaUsuario('La contraseña debe tener al menos 6 caracteres.', 'error');
    document.getElementById('usr-pass').focus();
    return;
  }

  if (pass !== passConfirm) {
    mostrarAlertaUsuario('Las contraseñas no coinciden.', 'error');
    document.getElementById('usr-pass-confirm').focus();
    return;
  }

  const usuario = {
    idUsuario: Date.now(),
    idRol,
    nombres,
    username,
    email,
    telefono,
    celular,
    zonaHoraria,
    estado: 'ACTIVO',
    activo2FA: false,
    emailVerificado: false,
    requiereCambioPassword: true,
    fechaCreacion: new Date().toISOString()
  };

  const usuariosGuardados = JSON.parse(localStorage.getItem('nesssoft.usuarios') || '[]');
  usuariosGuardados.push(usuario);
  localStorage.setItem('nesssoft.usuarios', JSON.stringify(usuariosGuardados));

  submitButton.classList.add('btn-loading');
  submitButton.disabled = true;
  mostrarAlertaUsuario('Usuario preparado correctamente. Redirigiendo al directorio...', 'success');

  setTimeout(() => {
    window.location.href = 'Usuarios.html';
  }, 1200);
}

function mostrarAlertaUsuario(mensaje, tipo) {
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