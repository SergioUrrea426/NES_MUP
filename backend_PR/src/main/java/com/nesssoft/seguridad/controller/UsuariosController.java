package com.nesssoft.seguridad.controller;


import com.nesssoft.seguridad.model.Usuarios;
import com.nesssoft.seguridad.service.UsuariosService;
import com.nesssoft.seguridad.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST para gestionar usuarios
 * Endpoints: POST, GET, PUT, DELETE
 */
@RestController
@RequestMapping("/usuarios")
public class UsuariosController {
    
    @Autowired
    private UsuariosService usuariosService;
    
    @Autowired
    private LoginService loginService;
    
    /**
     * Registra un nuevo usuario
     * POST /api/usuarios/registrar
     */
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@RequestBody Usuarios usuario) {
        try {
            // Validar que el usuario no exista
            Optional<Usuarios> usuarioExistente = usuariosService.obtenerUsuarioByEmail(usuario.getEmail());
            if (usuarioExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El usuario con este email ya existe"));
            }
            
            Usuarios usuarioRegistrado = usuariosService.registrarUsuario(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario registrado exitosamente");
            response.put("usuario", usuarioRegistrado);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al registrar usuario: " + e.getMessage()));
        }
    }
    
    /**
     * Inicia sesión de usuario
     * POST /api/usuarios/login
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            boolean loginExitoso = loginService.login(email, password);
            
            if (loginExitoso) {
                Optional<Usuarios> usuario = usuariosService.obtenerUsuarioByEmail(email);
                
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Login exitoso");
                response.put("usuario", usuario.orElse(null));
                response.put("token", "token_placeholder_" + System.currentTimeMillis());
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error en el login: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene todos los usuarios
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosUsuarios() {
        try {
            List<Usuarios> usuarios = usuariosService.obtenerTodosUsuarios();
            
            Map<String, Object> response = new HashMap<>();
            response.put("total", usuarios.size());
            response.put("usuarios", usuarios);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener usuarios: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene usuarios activos
     * GET /api/usuarios/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<Map<String, Object>> obtenerUsuariosActivos() {
        try {
            Iterable<Usuarios> usuarios = usuariosService.obtenerUsuariosActivos();
            
            Map<String, Object> response = new HashMap<>();
            response.put("usuarios", usuarios);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener usuarios activos: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene un usuario por ID
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerUsuarioById(@PathVariable int id) {
        try {
            Optional<Usuarios> usuario = usuariosService.obtenerUsuarioById(id);
            
            if (usuario.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("usuario", usuario.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener usuario: " + e.getMessage()));
        }
    }
    
    /**
     * Actualiza un usuario
     * PUT /api/usuarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarUsuario(
            @PathVariable int id,
            @RequestBody Usuarios usuarioActualizado) {
        try {
            Optional<Usuarios> usuarioExistente = usuariosService.obtenerUsuarioById(id);
            
            if (usuarioExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }
            
            Usuarios usuario = usuarioExistente.get();
            usuario.setNombres(usuarioActualizado.getNombres());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setCelular(usuarioActualizado.getCelular());
            usuario.setFotoPerfil(usuarioActualizado.getFotoPerfil());
            usuario.setZonaHoraria(usuarioActualizado.getZonaHoraria());
            
            Usuarios usuarioGuardado = usuariosService.actualizarUsuario(usuario);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Usuario actualizado exitosamente");
            response.put("usuario", usuarioGuardado);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar usuario: " + e.getMessage()));
        }
    }
    
    /**
     * Desactiva un usuario
     * PUT /api/usuarios/{id}/desactivar
     */
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, Object>> desactivarUsuario(@PathVariable int id) {
        try {
            boolean desactivado = usuariosService.desactivarUsuario(id);
            
            if (desactivado) {
                return ResponseEntity.ok(Map.of("mensaje", "Usuario desactivado exitosamente"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al desactivar usuario: " + e.getMessage()));
        }
    }
    
    /**
     * Elimina un usuario
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable int id) {
        try {
            Optional<Usuarios> usuario = usuariosService.obtenerUsuarioById(id);
            
            if (usuario.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
            }
            
            usuariosService.eliminarUsuario(id);
            
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar usuario: " + e.getMessage()));
        }
    }



}
