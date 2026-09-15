package com.nesssoft.seguridad.service;

import com.nesssoft.seguridad.model.Usuarios;
import com.nesssoft.seguridad.repository.UsuariosRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Servicio para gestionar el login y autenticación de usuarios
 */
@Service
public class LoginService {
    
    @Autowired
    private UsuariosRepositoryJpa usuariosRepository;
    
    /**
     * Valida las credenciales del usuario y lo autentica
     * @param email email del usuario
     * @param password contraseña en texto plano
     * @return true si el login es exitoso, false en caso contrario
     */
    public boolean login(String email, String password) {
        try {
            Optional<Usuarios> usuarioOpt = usuariosRepository.findByEmail(email);
            
            if (usuarioOpt.isEmpty()) {
                return false;
            }
            
            Usuarios usuario = usuarioOpt.get();
            
            if (!"ACTIVO".equals(usuario.getEstado())) {
                System.out.println("El usuario no está activo. No se puede iniciar sesión.");
                return false;
            }
            
            // NOTA: En producción usar BCryptPasswordEncoder, no comparar texto plano
            if (!usuario.getPasswordHash().equals(password)) {
                usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                usuariosRepository.save(usuario);
                return false;
            }
            
            // Resetear intentos fallidos y actualizar último acceso
            usuario.setIntentosFallidos(0);
            usuario.setUltimoAcceso(java.time.LocalDateTime.now());
            usuariosRepository.save(usuario);
            
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
            return false;
        }
    }
}
