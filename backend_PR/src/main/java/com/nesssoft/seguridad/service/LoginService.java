package com.nesssoft.seguridad.service;

import com.nesssoft.seguridad.repository.UsuariosRepositoryJpa;
import com.nesssoft.seguridad.repository.UsuariosRepositoryJpa.UsuarioLoginProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

//=======================
// Servicio para gestionar el login y autenticación de usuarios
//=======================

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
     * @return usuario autenticado, o vacío si las credenciales no son válidas
     */
    @Transactional
    public Optional<UsuarioLoginProjection> login(String email, String password) {
        Optional<UsuarioLoginProjection> usuarioOpt =
                usuariosRepository.findByEmailAndEstado(email, "ACTIVO");

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        UsuarioLoginProjection usuario = usuarioOpt.get();

        // NOTA: En producción usar BCryptPasswordEncoder, no comparar texto plano
        if (!usuario.getPasswordHash().equals(password)) {
            usuariosRepository.incrementarIntentosFallidos(usuario.getIdUsuario());
            return Optional.empty();
        }

        usuariosRepository.registrarAccesoExitoso(usuario.getIdUsuario(), LocalDateTime.now());
        return usuarioOpt;
    }
}
