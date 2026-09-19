package com.nesssoft.comercial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    // Datos del tercero
    //=======================

    @Column(name = "tipo_tercero", nullable = false)
    private String tipoTercero;

    @Column(name = "tipo_persona", nullable = false)
    private String tipoPersona;

    @Column(name = "id_tipo_documento", nullable = false)
    private int idTipoDocumento;

    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @Column(name = "digito_verificacion")
    private String digitoVerificacion;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "nombre_comercial")
    private String nombreComercial;

    @Column(name = "correo_principal", nullable = false)
    private String correoPrincipal;

    @Column(name = "correo_facturacion")
    private String correoFacturacion;

    @Column(name = "telefono_fijo")
    private String telefonoFijo;

    @Column(name = "celular")
    private String celular;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(name = "regimen_tributario")
    private String regimenTributario;

    @Column(name = "responsabilidad_fiscal")
    private String responsabilidadFiscal;

    @Column(name = "responsable_iva")
    private Boolean responsableIva;

    @Column(name = "sitio_web")
    private String sitioWeb;

    @Column(name = "limite_credito")
    private BigDecimal limiteCredito;

    @Column(name = "dias_credito_cliente")
    private Integer diasCreditoCliente;

    @Column(name = "saldo_actual")
    private BigDecimal saldoActual;

    @Column(name = "descuento_general")
    private BigDecimal descuentoGeneral;

    @Column(name = "ecommerce_client")
    private Boolean ecommerceClient;

    @Column(name = "acepta_habeas_data")
    private Boolean aceptaHabeasData;

    @Column(name = "fecha_aceptacion_habeas_data")
    private LocalDateTime fechaAceptacionHabeasData;

    @Column(name = "cupo_credito")
    private BigDecimal cupoCredito;

    @Column(name = "dias_credito_proveedor")
    private Integer diasCreditoProveedor;

    @Column(name = "fecha_vinculacion")
    private LocalDateTime fechaVinculacion;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "observaciones")
    private String observaciones;

    //=======================
    // Auditoría
    //=======================

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por")
    private Integer creadoPor;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "actualizado_por")
    private Integer actualizadoPor;

    //=======================
    //Constructores
    //=======================

    public Terceros(
        String tipoTercero, 
        String tipoPersona, 
        int idTipoDocumento, 
        String numeroDocumento, 
        String correoPrincipal,
        String nombres,
        String apellidos,
        String razonSocial,
        String nombreComercial,
        String telefonoFijo,
        String celular,
        String direccion,
        Integer idUbicacion,
        String regimenTributario,
        String responsabilidadFiscal,
        Boolean responsableIva,
        String sitioWeb,
        BigDecimal limiteCredito,
        Integer diasCreditoCliente,
        BigDecimal saldoActual,
        BigDecimal descuentoGeneral,
        Boolean ecommerceClient,
        Boolean aceptaHabeasData,
        LocalDateTime fechaAceptacionHabeasData,
        BigDecimal cupoCredito,
        Integer diasCreditoProveedor,
        LocalDateTime fechaVinculacion,
        Boolean estado,
        String observaciones,
        String correoFacturacion,
        LocalDateTime fechaCreacion,
        Integer creadoPor,
        LocalDateTime fechaActualizacion,
        Integer actualizadoPor
    ) {
            
        this.tipoTercero = tipoTercero;
        this.tipoPersona = tipoPersona;
        this.idTipoDocumento = idTipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.correoPrincipal = correoPrincipal;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.telefonoFijo = telefonoFijo;
        this.celular = celular;
        this.direccion = direccion;
        this.idUbicacion = idUbicacion;
        this.regimenTributario = regimenTributario;
        this.responsabilidadFiscal = responsabilidadFiscal;
        this.responsableIva = responsableIva;
        this.sitioWeb = sitioWeb;
        this.limiteCredito = limiteCredito;
        this.diasCreditoCliente = diasCreditoCliente;
        this.saldoActual = saldoActual;
        this.descuentoGeneral = descuentoGeneral;
        this.ecommerceClient = ecommerceClient;
        this.aceptaHabeasData = aceptaHabeasData;
        this.fechaAceptacionHabeasData = fechaAceptacionHabeasData;
        this.cupoCredito = cupoCredito;
        this.diasCreditoProveedor = diasCreditoProveedor;
        this.fechaVinculacion = fechaVinculacion;
        this.estado = estado;
        this.observaciones = observaciones;
        this.correoFacturacion = correoFacturacion;
        this.fechaCreacion = fechaCreacion;
        this.creadoPor = creadoPor;
        this.fechaActualizacion = fechaActualizacion;
        this.actualizadoPor = actualizadoPor;
    }

    //=======================
    // Getters y Setters
    //=======================

    public int getIdTercero() {
        return idTercero;
    }
    public void setIdTercero(int idTercero) {
        this.idTercero = idTercero;
    }
    public String getTipoTercero() {
        return tipoTercero;
    }
    public void setTipoTercero(String tipoTercero) {
        this.tipoTercero = tipoTercero;
    }
    public String getTipoPersona() {
        return tipoPersona;
    }
    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }
    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    public String getDigitoVerificacion() {
        return digitoVerificacion;
    }
    public void setDigitoVerificacion(String digitoVerificacion) {
        this.digitoVerificacion = digitoVerificacion;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getRazonSocial() {
        return razonSocial;
    }
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
    public String getNombreComercial() {
        return nombreComercial;
    }
    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }
    public String getCorreoPrincipal() {
        return correoPrincipal;
    }
    public void setCorreoPrincipal(String correoPrincipal) {
        this.correoPrincipal = correoPrincipal;
    }
    public String getCorreoFacturacion() {
        return correoFacturacion;
    }
    public void setCorreoFacturacion(String correoFacturacion) {
        this.correoFacturacion = correoFacturacion;
    }
    public String getTelefonoFijo() {
        return telefonoFijo;
    }
    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }
    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public Integer getIdUbicacion() {
        return idUbicacion;
    }
    public void setIdUbicacion(Integer idUbicacion) {
        this.idUbicacion = idUbicacion;
    }
    public String getRegimenTributario() {
        return regimenTributario;
    }
    public void setRegimenTributario(String regimenTributario) {
        this.regimenTributario = regimenTributario;
    }
    public String getResponsabilidadFiscal() {
        return responsabilidadFiscal;
    }
    public void setResponsabilidadFiscal(String responsabilidadFiscal) {
        this.responsabilidadFiscal = responsabilidadFiscal;
    }
    public Boolean getResponsableIva() {
        return responsableIva;
    }
    public void setResponsableIva(Boolean responsableIva) {
        this.responsableIva = responsableIva;
    }
    public String getSitioWeb() {
        return sitioWeb;
    }
    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }
    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }
    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
    public Integer getDiasCreditoCliente() {
        return diasCreditoCliente;
    }
    public void setDiasCreditoCliente(Integer diasCreditoCliente) {
        this.diasCreditoCliente = diasCreditoCliente;
    }
    public BigDecimal getSaldoActual() {
        return saldoActual;
    }
    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }
    public BigDecimal getDescuentoGeneral() {
        return descuentoGeneral;
    }
    public void setDescuentoGeneral(BigDecimal descuentoGeneral) {
        this.descuentoGeneral = descuentoGeneral;
    }
    public Boolean getEcommerceClient() {
        return ecommerceClient;
    }
    public void setEcommerceClient(Boolean ecommerceClient) {
        this.ecommerceClient = ecommerceClient;
    }
    public Boolean getAceptaHabeasData() {
        return aceptaHabeasData;
    }
    public void setAceptaHabeasData(Boolean aceptaHabeasData) {
        this.aceptaHabeasData = aceptaHabeasData;
    }
    public LocalDateTime getFechaAceptacionHabeasData() {
        return fechaAceptacionHabeasData;
    }
    public void setFechaAceptacionHabeasData(LocalDateTime fechaAceptacionHabeasData) {
        this.fechaAceptacionHabeasData = fechaAceptacionHabeasData;
    }
    public BigDecimal getCupoCredito() {
        return cupoCredito;
    }
    public void setCupoCredito(BigDecimal cupoCredito) {
        this.cupoCredito = cupoCredito;
    }
    public Integer getDiasCreditoProveedor() {
        return diasCreditoProveedor;
    }
    public void setDiasCreditoProveedor(Integer diasCreditoProveedor) {
        this.diasCreditoProveedor = diasCreditoProveedor;
    }
    public LocalDateTime getFechaVinculacion() {
        return fechaVinculacion;
    }
    public void setFechaVinculacion(LocalDateTime fechaVinculacion) {
        this.fechaVinculacion = fechaVinculacion;
    }
    public Boolean getEstado() {
        return estado;
    }
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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


    //=======================
    // Constructor adicional para la creación de un tercero con los campos esenciales
    //=======================
    public Terceros(int idTercero, String tipoTercero, String tipoPersona, int idTipoDocumento, String numeroDocumento, String nombre, String direccion, String telefono, String correoElectronico) {
        this.idTercero = idTercero;
        this.tipoTercero = tipoTercero;
        this.tipoPersona = tipoPersona;
        this.idTipoDocumento = idTipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombre;
        this.direccion = direccion;
        this.telefonoFijo = telefono;
        this.correoPrincipal = correoElectronico;
    }
}