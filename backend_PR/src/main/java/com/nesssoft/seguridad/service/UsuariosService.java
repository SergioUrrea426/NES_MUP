package com.nesssoft.seguridad.service;

import com.nesssoft.seguridad.model.Usuarios;
import com.nesssoft.seguridad.repository.UsuariosRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones CRUD de usuarios
 */
@Service
public class UsuariosService {
    
    @Autowired
    private UsuariosRepositoryJpa usuariosRepository;
    
    /**
     * Registra un nuevo usuario en el sistema
     * @param usuario objeto del usuario a registrar
     * @return el usuario guardado
     */
    public Usuarios registrarUsuario(Usuarios usuario) {
        usuario.setEstado("ACTIVO");
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setIntentosFallidos(0);
        usuario.setEmailVerificado(false);
        usuario.setActivo2FA(false);
        return usuariosRepository.save(usuario);
    }
    
    /**
     * Obtiene un usuario por su ID
     * @param idUsuario el ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuarios> obtenerUsuarioById(int idUsuario) {
        return usuariosRepository.findById(idUsuario);
    }
    
    /**
     * Obtiene un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<Usuarios> obtenerUsuarioByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }
    
    /**
     * Obtiene todos los usuarios
     * @return lista de todos los usuarios
     */
    public List<Usuarios> obtenerTodosUsuarios() {
        return usuariosRepository.findAll();
    }
    
    /**
     * Obtiene usuarios activos
     * @return lista de usuarios activos
     */
    public Iterable<Usuarios> obtenerUsuariosActivos() {
        return usuariosRepository.findByEstado("ACTIVO");
    }
    
    /**
     * Actualiza un usuario existente
     * @param usuario objeto del usuario con cambios
     * @return el usuario actualizado
     */
    public Usuarios actualizarUsuario(Usuarios usuario) {
        usuario.setFechaActualizacion(LocalDateTime.now());
        return usuariosRepository.save(usuario);
    }
    
    /**
     * Desactiva un usuario
     * @param idUsuario el ID del usuario a desactivar
     * @return true si se desactivó, false en caso contrario
     */
    public boolean desactivarUsuario(int idUsuario) {
        Optional<Usuarios> usuarioOpt = usuariosRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            Usuarios usuario = usuarioOpt.get();
            usuario.setEstado("INACTIVO");
            usuario.setFechaActualizacion(LocalDateTime.now());
            usuariosRepository.save(usuario);
            return true;
        }
        return false;
    }
    
    /**
     * Elimina un usuario (eliminación física)
     * @param idUsuario el ID del usuario a eliminar
     */
    public void eliminarUsuario(int idUsuario) {
        usuariosRepository.deleteById(idUsuario);
    }
}
