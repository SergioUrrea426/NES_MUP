package com.nesssoft.seguridad.model;

import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "roles", schema = "seguridad")
@Data
@NoArgsConstructor
public class Roles {

    // ======================
    // Atributos
    // ======================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int idRol;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "nivel_jerarquia")
    private String nivelJerarquia;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // ======================
    // Constructor
    // ======================
    
    public Roles(
            int idRol,
            String codigo,
            String nombre,
            String descripcion,
            String nivelJerarquia,
            boolean estado,
            LocalDateTime fechaCreacion) {

        this.idRol = idRol;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelJerarquia = nivelJerarquia;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // ======================
    // Getters y Setters
    // ======================

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNivelJerarquia() {
        return nivelJerarquia;
    }

    public void setNivelJerarquia(String nivelJerarquia) {
        this.nivelJerarquia = nivelJerarquia;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}