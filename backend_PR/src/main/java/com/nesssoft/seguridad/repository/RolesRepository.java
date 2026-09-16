package com.nesssoft.seguridad.repository;

import com.nesssoft.config.DatabaseConnection;
import com.nesssoft.seguridad.model.Roles;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class RolesRepository {
    public Roles save(Roles role) throws SQLException {

        String sql = """
             INSERT INTO seguridad.roles
             (codigo, nombre, descripcion, nivel_jerarquia, estado)
             VALUES (?, ?, ?, ?, ?)
             """;

     try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

        statement.setString(1, role.getCodigo());
        statement.setString(2, role.getNombre());
        statement.setString(3, role.getDescripcion());
        statement.setInt(4, role.getNivelJerarquia());
        statement.setBoolean(5, role.isEstado());

        statement.executeUpdate();

        try (java.sql.ResultSet keys = statement.getGeneratedKeys()) {
            if (keys.next()) {
                role.setIdRol(keys.getInt(1));
             }
         }
     }

        return role;
    }


    public List<Roles> findALL(){
        List<Roles> rolesList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "select id_rol, codigo, nombre, descripcion, nivel_jerarquia, estado, fecha_creacion " +
                 "from seguridad.roles");
             java.sql.ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
            int idRol = resultSet.getInt("id_rol");
                String codigo = resultSet.getString("codigo");
                String nombre = resultSet.getString("nombre");
            String descripcion = resultSet.getString("descripcion");
            Integer nivelJerarquia = resultSet.getInt("nivel_jerarquia");
            boolean estado = resultSet.getBoolean("estado");
            java.sql.Timestamp fechaCreacionTimestamp = resultSet.getTimestamp("fecha_creacion");
            java.time.LocalDateTime fechaCreacion = fechaCreacionTimestamp != null
                ? fechaCreacionTimestamp.toLocalDateTime()
                : null;

            Roles role = new Roles(
                idRol,
                codigo,
                nombre,
                descripcion,
                nivelJerarquia,
                estado,
                fechaCreacion);
                rolesList.add(role);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rolesList;
    }
}
