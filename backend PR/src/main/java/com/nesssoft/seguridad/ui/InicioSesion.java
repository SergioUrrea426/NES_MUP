package com.nesssoft.seguridad.ui;

import com.nesssoft.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * DEPRECATED: Esta clase es parte del código CLI antiguo.
 * Se recomienda usar los endpoints REST en su lugar:
 * POST /api/usuarios/login?email=...&password=...
 */
public class InicioSesion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Correo: ");
        String email = sc.nextLine();

        System.out.print("Contraseña: ");
        String password = sc.nextLine();

        try {
            // Usar JDBC directamente para compatibilidad con código CLI
            if (loginDirecto(email, password)) {
                System.out.println("✅ Inicio de sesión exitoso.");
            } else {
                System.out.println("❌ Error al iniciar sesión. Verifica tus credenciales.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en la base de datos: " + e.getMessage());
        }

        sc.close();
    }

    /**
     * Valida login usando JDBC directo (compatible con código CLI)
     */
    private static boolean loginDirecto(String email, String password) throws SQLException {
        String sql = "SELECT * FROM seguridad.usuarios WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }

            String estado = resultSet.getString("estado");
            if (!"ACTIVO".equals(estado)) {
                System.out.println("⚠️  El usuario no está activo.");
                return false;
            }

            String passwordHash = resultSet.getString("password_hash");
            if (!passwordHash.equals(password)) {
                return false;
            }

            return true;
        }
    }
}
