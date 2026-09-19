package com.nesssoft.comercial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad JPA para la tabla de terceros
 * Mapeo con la tabla comercial.terceros
 */
@Entity
@Table(name = "terceros", schema = "comercial")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Terceros {


    //=======================
    // Identificación
    //=======================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tercero")
    private int idTercero;

    //=======================
    // Datos Tercero
    //=======================

    @Column(name = "tipo_tercero", nullable = false)
    private String tipoTercero;

    @Column(name = "tipo_persona", nullable = false)
    private String tipoPersona;

    @Column(name = "id_tipo_documento", nullable = false)
    private int idTipoDocumento;

    @Column(name = "numero_documento", nullable = false, unique = true)
    private String numeroDocumento;

    @Column(name = "digito_verificacion", nullable = true)
    private String digitoVerificacion;

    @Column(name = "nombres", nullable = true)
    private String nombres;

    @Column(name = "apellidos", nullable = true)
    private String apellidos;

    @Column(name = "razon_social", nullable = true)
    private String razonSocial;

    

}
