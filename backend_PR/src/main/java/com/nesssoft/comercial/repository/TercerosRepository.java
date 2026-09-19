package com.nesssoft.comercial.repository;
import com.nesssoft.comercial.model.Terceros;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository

public class TercerosRepository {
    public Terceros save(Terceros tercero) throws SQLException{
 
        String sql = """
                INSERT INTO comercial.terceros
                (tipo_tercero,tipo_persona, id_tipo_documento,
                numero_documento, nombre, direccion, telefono,
                 correo_electronico)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try(Connection connection = com.nesssoft.config.DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, tercero.getTipoTercero());
            statement.setString(2, tercero.getTipoPersona());
            statement.setInt(3, tercero.getIdTipoDocumento());
            statement.setString(4, tercero.getNumeroDocumento());
            statement.setString(5, tercero.getNombres());
            statement.setString(6, tercero.getDireccion());
            statement.setString(7, tercero.getTelefonoFijo());
            statement.setString(8, tercero.getCorreoPrincipal());

            statement.executeUpdate();

            try (java.sql.ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    tercero.setIdTercero(keys.getInt(1));
                }
            }
        }
        return tercero;
    }

    public List<Terceros> findAll(){
        List<Terceros> tercerosList = new ArrayList<>();
        try (Connection connection = com.nesssoft.config.DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select id_tercero, tipo_tercero, tipo_persona, id_tipo_documento, numero_documento, nombre, direccion, telefono, correo_electronico " +
                             "from comercial.terceros");
             java.sql.ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idTercero = resultSet.getInt("id_tercero");
                String tipoTercero = resultSet.getString("tipo_tercero");
                String tipoPersona = resultSet.getString("tipo_persona");
                int idTipoDocumento = resultSet.getInt("id_tipo_documento");
                String numeroDocumento = resultSet.getString("numero_documento");
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                String correoElectronico = resultSet.getString("correo_electronico");

                Terceros tercero = new Terceros(idTercero, tipoTercero, tipoPersona, idTipoDocumento,
                    numeroDocumento, nombre, direccion, telefono, correoElectronico);
                tercerosList.add(tercero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tercerosList;
    }

}

