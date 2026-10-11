package com.nesssoft.comercial.repository;
import com.nesssoft.comercial.model.Terceros;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//=======================
// Repositorio para la entidad "Terceros", que maneja la persistencia de datos en la base de datos.
// Contiene métodos para crear, obtener todos y obtener por ID los registros de terceros.
//=======================

@Repository

public class TercerosRepository {
    
    //Crear tercero 
    public Terceros save(Terceros tercero) throws SQLException{
 
        String sql = """
                INSERT INTO comercial.terceros
                (tipo_tercero,tipo_persona, id_tipo_documento,
                numero_documento, nombres, apellidos, direccion, telefono,
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
            statement.setString(6, tercero.getApellidos());
            statement.setString(7, tercero.getDireccion());
            statement.setString(8, tercero.getTelefonoFijo());
            statement.setString(9, tercero.getCorreoPrincipal());

            statement.executeUpdate();

            try (java.sql.ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    tercero.setIdTercero(keys.getInt(1));
                }
            }
        }
        return tercero;
    }
    //Obtener todos los terceros
    public List<Terceros> findAll(){
        List<Terceros> tercerosList = new ArrayList<>();
        try (Connection connection = com.nesssoft.config.DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "select id_tercero, tipo_tercero, tipo_persona, id_tipo_documento, numero_documento, nombres, apellidos, direccion, telefono, correo_electronico " +
                             "from comercial.terceros");
             java.sql.ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idTercero = resultSet.getInt("id_tercero");
                String tipoTercero = resultSet.getString("tipo_tercero");
                String tipoPersona = resultSet.getString("tipo_persona");
                int idTipoDocumento = resultSet.getInt("id_tipo_documento");
                String numeroDocumento = resultSet.getString("numero_documento");
                String nombres = resultSet.getString("nombres");
                String apellidos = resultSet.getString("apellidos");
                String direccion = resultSet.getString("direccion");
                String telefono = resultSet.getString("telefono");
                String correoElectronico = resultSet.getString("correo_electronico");

                Terceros tercero = new Terceros(idTercero, tipoTercero, tipoPersona, idTipoDocumento,
                    numeroDocumento, nombres, apellidos, direccion, telefono, correoElectronico);
                tercerosList.add(tercero);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tercerosList;
    }
    //Obtener un tercero por su ID
    public Optional<Terceros> findById(int id) {
        String sql = "SELECT * FROM comercial.terceros WHERE id_tercero = ?";
        try (Connection connection = com.nesssoft.config.DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Terceros tercero = new Terceros(
                            resultSet.getInt("id_tercero"),
                            resultSet.getString("tipo_tercero"),
                            resultSet.getString("tipo_persona"),
                            resultSet.getInt("id_tipo_documento"),
                            resultSet.getString("numero_documento"),
                            resultSet.getString("nombres"),
                            resultSet.getString("apellidos"),
                            resultSet.getString("direccion"),
                            resultSet.getString("telefono"),
                            resultSet.getString("correo_electronico")
                    );
                    return Optional.of(tercero);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}

