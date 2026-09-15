package com.nesssoft.seguridad.repository;

import com.nesssoft.config.DatabaseConnection;
import com.nesssoft.seguridad.model.Usuarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuariosRepository {

    public void save(Usuarios usuario) throws SQLException {

        String sql = """
                INSERT INTO seguridad.usuarios
                (
                    id_rol,
                    nombres,
                    username,
                    email,
                    password_hash,
                    telefono,
                    celular,
                    foto_perfil,
                    zona_horaria
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, usuario.getIdRol());
        statement.setString(2, usuario.getNombres());
        statement.setString(3, usuario.getUsername());
        statement.setString(4, usuario.getEmail());
        statement.setString(5, usuario.getPasswordHash());
        statement.setString(6, usuario.getTelefono());
        statement.setString(7, usuario.getCelular());
        statement.setString(8, usuario.getFotoPerfil());
        statement.setString(9, usuario.getZonaHoraria());

        statement.executeUpdate();

        statement.close();
        connection.close();
    }

    public Usuarios findByEmail(String email) throws SQLException {

        String sql = """
                SELECT *
                FROM seguridad.usuarios
                WHERE email = ?
                """;

        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);

        ResultSet resultSet = statement.executeQuery();

        Usuarios usuario = null;

        if (resultSet.next()) {

            usuario = new Usuarios(
                    resultSet.getInt("id_rol"),
                    resultSet.getString("nombres"),
                    resultSet.getString("username"),
                    resultSet.getString("email"),
                    resultSet.getString("password_hash"),
                    resultSet.getString("telefono"),
                    resultSet.getString("celular"),
                    resultSet.getString("foto_perfil"),
                    resultSet.getString("zona_horaria")
            );

            usuario.setIdUsuario(resultSet.getInt("id_usuario"));
            usuario.setEstado(resultSet.getString("estado"));
            usuario.setActivo2FA(resultSet.getBoolean("activo_2fa"));
            usuario.setEmailVerificado(resultSet.getBoolean("email_verificado"));

            if (resultSet.getTimestamp("ultimo_acceso") != null) {
                usuario.setUltimoAcceso(resultSet.getTimestamp("ultimo_acceso").toLocalDateTime());
            }

            if (resultSet.getTimestamp("fecha_creacion") != null) {
                usuario.setFechaCreacion(resultSet.getTimestamp("fecha_creacion").toLocalDateTime());
            }

            Object creadoPorObj = resultSet.getObject("creado_por");
            if (creadoPorObj != null) {
                usuario.setCreadoPor(((Number) creadoPorObj).intValue());
            }
            usuario.setIntentosFallidos(resultSet.getInt("intentos_fallidos"));

            if (resultSet.getTimestamp("bloqueo_hasta") != null) {
                usuario.setBloqueoHasta(resultSet.getTimestamp("bloqueo_hasta").toLocalDateTime());
            }

            if (resultSet.getTimestamp("ultimo_cambio_password") != null) {
                usuario.setUltimoCambioPassword(resultSet.getTimestamp("ultimo_cambio_password").toLocalDateTime());
            }

            usuario.setRequiereCambioPassword(resultSet.getBoolean("requiere_cambio_password"));

            if (resultSet.getTimestamp("fecha_actualizacion") != null) {
                usuario.setFechaActualizacion(resultSet.getTimestamp("fecha_actualizacion").toLocalDateTime());
            }

            Object actualizadoPorObj = resultSet.getObject("actualizado_por");
            if (actualizadoPorObj != null) {
                usuario.setActualizadoPor(((Number) actualizadoPorObj).intValue());
            }
        }

        resultSet.close();
        statement.close();
        connection.close();

        return usuario;
    }

}