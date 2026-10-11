package com.nesssoft.seguridad.repository;

import com.nesssoft.seguridad.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

//=======================
// Repositorio JPA para la entidad Usuarios, proporciona operaciones CRUD automáticas y consultas personalizadas.
//=======================

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

    interface UsuarioLoginProjection {
        Integer getIdUsuario();
        String getEmail();
        String getUsername();
        String getEstado();
        String getPasswordHash();
    }

    /**
     * Busca un usuario por su email y estado
     * @param email el email del usuario
     * @param estado el estado del usuario
     * @return Optional con el usuario si existe
     */
    Optional<UsuarioLoginProjection> findByEmailAndEstado(String email, String estado);

    @Modifying
    @Query("UPDATE Usuarios u SET u.intentosFallidos = COALESCE(u.intentosFallidos, 0) + 1 WHERE u.idUsuario = :idUsuario")
    int incrementarIntentosFallidos(@Param("idUsuario") Integer idUsuario);

    @Modifying
    @Query("UPDATE Usuarios u SET u.intentosFallidos = 0, u.ultimoAcceso = :ultimoAcceso WHERE u.idUsuario = :idUsuario")
    int registrarAccesoExitoso(
            @Param("idUsuario") Integer idUsuario,
            @Param("ultimoAcceso") LocalDateTime ultimoAcceso);

}
