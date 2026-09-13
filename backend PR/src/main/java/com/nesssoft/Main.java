

package com.nesssoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot
 * NES MUP - Sistema de Gestión de Usuarios
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("✅ Aplicación NES MUP iniciada correctamente");
        System.out.println("📡 API disponible en: http://localhost:8080/api");
    }
}
