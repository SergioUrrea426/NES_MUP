package com.nesssoft.seguridad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad JPA para la tabla de Bitácora de Eventos
 * Registra todas las acciones y eventos del sistema
 */
@Entity
@Table(name = "bitacora_eventos", schema = "seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bitacora_eventos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id_evento;
    
    @Column(name = "tipo_evento")
    private String tipo_evento;
    
    @Column(name = "id_usuario")
    private int id_usuario;
    
    @Column(name = "modulo")
    private String modulo;
    
    @Column(name = "entidad")
    private String entidad;
    
    @Column(name = "id_entidad")
    private int id_entidad;
    
    @Column(name = "accion")
    private String accion;
    
    @Column(name = "estado_anterior")
    private String estado_anterior;
    
    @Column(name = "estado_nuevo")
    private String estado_nuevo;
    
    @Column(name = "direccion_ip")
    private String direccion_ip;
    
    @Column(name = "user_agent")
    private String user_agent;
    
    @Column(name = "sistema_operativo")
    private String sistema_operativo;
    
    @Column(name = "navegador")
    private String navegador;
    
    @Column(name = "dispositivo")
    private String dispositivo;
    
    @Column(name = "sesion")
    private UUID sesion;
    
    @Column(name = "token_sesion")
    private UUID token_sesion;
    
    @Column(name = "severidad")
    private String severidad;
    
    @Column(name = "hash_integridad")
    private String hash_integridad;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "fecha_evento")
    private LocalDateTime fecha_evento;
}
