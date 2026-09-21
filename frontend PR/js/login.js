document.addEventListener("DOMContentLoaded", () => {
  // Referencias a los elementos del DOM
  const loginForm = document.getElementById("loginForm");
  const emailInput = document.getElementById("email");
  const passwordInput = document.getElementById("password");
  const togglePasswordBtn = document.getElementById("togglePassword");
  const eyeIcon = document.getElementById("eyeIcon");
  const emailError = document.getElementById("emailError");
  const passwordError = document.getElementById("passwordError");
  const submitBtn = document.getElementById("submitBtn");

  // 1. Mostrar / Ocultar Contraseña
  if (togglePasswordBtn && passwordInput && eyeIcon) {
    togglePasswordBtn.addEventListener("click", (e) => {
      e.preventDefault();

      // Alternar el tipo de input entre 'password' y 'text'
      const isPassword = passwordInput.getAttribute("type") === "password";
      passwordInput.setAttribute("type", isPassword ? "text" : "password");

      // Alternar los iconos de FontAwesome
      if (isPassword) {
        eyeIcon.classList.remove("fa-eye");
        eyeIcon.classList.add("fa-eye-slash");
      } else {
        eyeIcon.classList.remove("fa-eye-slash");
        eyeIcon.classList.add("fa-eye");
      }
    });
  }

  // 2. Control del envío del Formulario y Validaciones
  if (loginForm) {
    loginForm.addEventListener("submit", (e) => {
      e.preventDefault();

      let isValid = true;
      const emailValue = emailInput.value.trim();
      const passwordValue = passwordInput.value.trim();

      // Validar Correo Electrónico
      if (!validateEmail(emailValue)) {
        emailInput.classList.add("input-error");
        emailError.classList.remove("hidden");
        isValid = false;
      } else {
        emailInput.classList.remove("input-error");
        emailError.classList.add("hidden");
      }

      // Validar Contraseña
      if (passwordValue === "") {
        passwordInput.classList.add("input-error");
        passwordError.classList.remove("hidden");
        isValid = false;
      } else {
        passwordInput.classList.remove("input-error");
        passwordError.classList.add("hidden");
      }

      // Si las validaciones son correctas, se procesa la solicitud
      if (isValid) {
        submitForm(emailValue, passwordValue);
      }
    });
  }

  // Función auxiliar para validar formato de correo electrónico
  function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  // Flujo local de demostración: no realiza llamadas al backend.
  function submitForm(email) {
    submitBtn.classList.add("btn-loading");
    submitBtn.innerHTML =
      '<i class="fa-solid fa-circle-notch fa-spin text-sm"></i> Conectando...';

    setTimeout(() => {
      sessionStorage.setItem("nesssoft.frontendSession", JSON.stringify({ email }));
      window.location.href = "dashboard.html"; // redirige donde corresponda
    }, 500);

    setTimeout(() => {
      submitBtn.classList.remove("btn-loading");
      submitBtn.innerHTML =
        '<span id="btnText">Ingresar al Sistema</span><i class="fa-solid fa-arrow-right text-xs" id="btnIcon"></i>';
    }, 500);
  }
});
