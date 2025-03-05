package com.example.demo.repository;

import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByCorreo(String correo);

    Optional<Users> findByNombreAndContraseña(String nombre, String contrasena);

    Users findByCorreoAndContraseña(String correo, String contraseña);




}
