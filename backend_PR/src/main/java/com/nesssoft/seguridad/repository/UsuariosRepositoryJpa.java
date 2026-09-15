package com.nesssoft.seguridad.repository;

import com.nesssoft.seguridad.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuarios
 * Proporciona operaciones CRUD automáticas
 */
@Repository
public interface UsuariosRepositoryJpa extends JpaRepository<Usuarios, Integer> {
    
    /**
     * Busca un usuario por su email
     * @param email el email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuarios> findByEmail(String email);
    
    /**
     * Busca un usuario por su username
     * @param username el nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuarios> findByUsername(String username);
    
    /**
     * Busca usuarios activos
     * @return lista de usuarios activos
     */
    Iterable<Usuarios> findByEstado(String estado);
}
