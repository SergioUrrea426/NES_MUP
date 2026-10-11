package com.nesssoft.comercial.service;

import com.nesssoft.comercial.model.Terceros;
import com.nesssoft.comercial.repository.TercerosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para operaciones CRUD  de terceros (clientes/proveedores) en el sistema.
 * Contiene métodos para registrar un nuevo tercero.
 */

@Service
public class TercerosService {
    
    @Autowired
    private TercerosRepository tercerosRepository;
    /**
     * Registrode de un nuevo  tercero (Cliente/Proveedor) en el sistema
     * @param tercero objeto del tercero a registrar
     * @return el tercero guardado
     */

    public  Terceros registrarTercero(Terceros tercero){
        tercero.setEstado(true);
        tercero.setFechaCreacion(LocalDateTime.now());
        try {
            return tercerosRepository.save(tercero);
        } catch (SQLException e) {
            throw new IllegalStateException("No fue posible registrar el tercero", e);
        }
    }


    /**
     * Obtiene todos los terceros registrados en el sistema
     * @param Terceros objeto de tercero consultado
     * @return lista de terceros
     */
    public List<Terceros> obtenerTodosTerceros() {
        return tercerosRepository.findAll();
    }


}
