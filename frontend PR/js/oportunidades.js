// Etapas comerciales demo para representar el pipeline en frontend.
const oportunidadesDemo = [
  { id: 1, nombre: 'Migracion CRM Industrias NESS', cliente: 'Industrias NESS S.A.S.', etapa: 'PROSPECCION', valor: 28000000 },
  { id: 2, nombre: 'Automatizacion comercial', cliente: 'Logistica del Caribe Lda.', etapa: 'NEGOCIACION', valor: 18500000 },
  { id: 3, nombre: 'Soporte anual CRM', cliente: 'Comercializadora Alfa', etapa: 'GANADA', valor: 9200000 }
];

// Calcula el resumen cuando la pantalla termina de cargar.
document.addEventListener('DOMContentLoaded', () => {
  actualizarResumen();
  document.getElementById('btn-nueva-oportunidad').addEventListener('click', () => {
    mostrarAlerta('El formulario de oportunidades quedara disponible en la siguiente etapa frontend.', 'info');
  });
});

// Cuenta oportunidades por etapa para alimentar los KPIs.
function actualizarResumen() {
  document.getElementById('kpi-prospeccion').textContent = oportunidadesDemo.filter(item => item.etapa === 'PROSPECCION').length;
  document.getElementById('kpi-negociacion').textContent = oportunidadesDemo.filter(item => item.etapa === 'NEGOCIACION').length;
  document.getElementById('kpi-ganadas').textContent = oportunidadesDemo.filter(item => item.etapa === 'GANADA').length;
}

// Informa que una acción aún pertenece al alcance futuro del frontend.
function mostrarAlerta(mensaje, tipo) {
  const alertBox = document.getElementById('opportunities-alert');
  alertBox.textContent = mensaje;
  alertBox.className = `opportunities-alert ${tipo}`;
}
