package com.nesssoft.seguridad.controller;

import com.nesssoft.seguridad.model.Roles;
import com.nesssoft.seguridad.repository.RolesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Controlador REST para gestionar roles
    Endpoints: POST, GET, PUT, DELETE
*/

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesRepository rolesRepository;

    /*
     * Crear roles
     * POST /roles/crear
     */
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearRol(@RequestBody Roles role) {
        try {
            Roles rolCreado = rolesRepository.save(role);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Rol creado exitosamente");
            response.put("rol", rolCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear rol: " + e.getMessage()));
        }
    }

    /*
     * Consultar todos los roles
     * GET /roles/consultar
     */
    @GetMapping("/consultar")
    public ResponseEntity<List<Roles>> consultarRoles() {
        List<Roles> roles = rolesRepository.findALL();
        return ResponseEntity.ok(roles);
    }

}