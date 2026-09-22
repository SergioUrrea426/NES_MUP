package com.nesssoft.seguridad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla de Usuarios
 * Mapeo con la tabla seguridad.usuarios
 */
@Entity
@Table(name = "usuarios", schema = "seguridad")
@Data
@AllArgsConstructor
public class Usuarios {

    // ======================
    // Identificación
    // ======================


    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;
    
    @Column(name = "id_rol", nullable = false)
    private int idRol;

    // ======================
    // Información personal
    // ======================
    @Column(name = "nombres", nullable = false)
    private String nombres;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "telefono")
    private String telefono;
    
    @Column(name = "celular")
    private String celular;
    
    @Column(name = "foto_perfil")
    private String fotoPerfil;
    
    @Column(name = "zona_horaria")
    private String zonaHoraria;

    // ======================
    // Seguridad
    // ======================
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "activo_2fa")
    private boolean activo2FA;
    
    @Column(name = "email_verificado")
    private boolean emailVerificado;

    // ======================
    // Auditoría
    // ======================
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "creado_por")
    private Integer creadoPor;
    
    @Column(name = "intentos_fallidos")
    private int intentosFallidos;
    
    @Column(name = "bloqueo_hasta")
    private LocalDateTime bloqueoHasta;
    
    @Column(name = "ultimo_cambio_password")
    private LocalDateTime ultimoCambioPassword;
    
    @Column(name = "requiere_cambio_password")
    private boolean requiereCambioPassword;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(name = "actualizado_por")
    private Integer actualizadoPor;

    // ======================
    // Constructor vacio para validación de login
    // ======================

    protected Usuarios(){}
    

    // ======================
    // Constructor comnpleto para la creación de usuarios y consultas
    // ======================

    public Usuarios(
            int idRol,
            String nombres,
            String username,
            String email,
            String passwordHash,
            String telefono,
            String celular,
            String fotoPerfil,
            String zonaHoraria) {

        this.idRol = idRol;
        this.nombres = nombres;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.telefono = telefono;
        this.celular = celular;
        this.fotoPerfil = fotoPerfil;
        this.zonaHoraria = zonaHoraria;
    }

    // ======================
    // Getters y Setters
    // ======================

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo2FA() {
        return activo2FA;
    }

    public void setActivo2FA(boolean activo2FA) {
        this.activo2FA = activo2FA;
    }

    public boolean isEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public LocalDateTime getBloqueoHasta() {
        return bloqueoHasta;
    }

    public void setBloqueoHasta(LocalDateTime bloqueoHasta) {
        this.bloqueoHasta = bloqueoHasta;
    }

    public LocalDateTime getUltimoCambioPassword() {
        return ultimoCambioPassword;
    }

    public void setUltimoCambioPassword(LocalDateTime ultimoCambioPassword) {
        this.ultimoCambioPassword = ultimoCambioPassword;
    }

    public boolean getRequiereCambioPassword() {
        return requiereCambioPassword;
    }

    public void setRequiereCambioPassword(boolean requiereCambioPassword) {
        this.requiereCambioPassword = requiereCambioPassword;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Integer getActualizadoPor() {
        return actualizadoPor;
    }

    public void setActualizadoPor(Integer actualizadoPor) {
        this.actualizadoPor = actualizadoPor;
    }

    // ======================
    // Métodos
    // ======================

    public void cambiarPassword(String passwordActual, String nuevaPassword) {

        if (!this.passwordHash.equals(passwordActual)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta.");
        }

        if (nuevaPassword.length() < 8) {
            throw new IllegalArgumentException("La nueva contraseña debe tener mínimo 8 caracteres.");
        }

        this.passwordHash = nuevaPassword;
    }
    
}