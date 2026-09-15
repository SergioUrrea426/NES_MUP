package com.nesssoft.seguridad.ui;

import com.nesssoft.seguridad.model.Usuarios;
import com.nesssoft.seguridad.repository.UsuariosRepository;
import java.sql.SQLException;
import java.util.Scanner;

public class RegistroUsuarios {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("ID del Rol: ");
        int idRol = sc.nextInt();
        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Correo: ");
        String email = sc.nextLine();

        System.out.print("Contraseña: ");
        String passwordHash = sc.nextLine();

        System.out.print("Telefono: ");
        String telefono = sc.nextLine();

        System.out.print("Celular: ");
        String celular = sc.nextLine();

        System.out.print("Foto de perfil: ");
        String fotoPerfil = sc.nextLine();

        System.out.print("Zona horaria: ");
        String zonaHoraria = sc.nextLine();


        Usuarios user = new Usuarios(
                idRol,
                nombre,
                username,
                email,
                passwordHash,
                telefono,
                celular,
                fotoPerfil,
                zonaHoraria
        );

        UsuariosRepository repository = new UsuariosRepository();

        try {

            repository.save(user);

            System.out.println("Usuario registrado correctamente.");

        } catch (SQLException e) {

            System.out.println("Error al registrar el usuario.");

            e.printStackTrace();

        }
        sc.close();
    }

}