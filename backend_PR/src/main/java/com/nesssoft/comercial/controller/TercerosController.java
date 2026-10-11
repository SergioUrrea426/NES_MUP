package com.nesssoft.comercial.controller;
import com.nesssoft.comercial.service.TercerosService;
import com.nesssoft.comercial.model.Terceros;
// import com.nesssoft.comercial.repository.TercerosRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar terceros
 * Endpoints: POST, GET, PUT, DELETE 
 */
@RestController
@RequestMapping("/Terceros")
public class TercerosController {
    @Autowired
    private TercerosService tercerosService;


    /*
    * Endpoint para registrar un nuevo tercero (Cliente/Proveedor)
    */

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarTercero(@RequestBody Terceros tercero){
        
        Map<String, Object> response = new HashMap<>();
        try {
            Terceros nuevoTercero = tercerosService.registrarTercero(tercero);
            response.put("message", "Tercero registrado exitosamente");
            response.put("tercero", nuevoTercero);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint para obtener todos los terceros
     * @return ResponseEntity con la lista de terceros y el estado HTTP correspondiente
     */

    @GetMapping("/obtenerTodosTerceros")
    public ResponseEntity<List<Terceros>> obtenerTodosTerceros(){
        List<Terceros> tercerosList = tercerosService.obtenerTodosTerceros();
        return new ResponseEntity<>(tercerosList, HttpStatus.OK);
    }
}
