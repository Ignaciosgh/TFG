package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Users save(Users user) {
        return userRepository.save(user);
    }

    public Users update(Long id, Users userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setNombre(userDetails.getNombre());
            user.setCorreo(userDetails.getCorreo());
            user.setContraseña(userDetails.getContraseña());
            return userRepository.save(user);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public Users autenticarUsuario(String correo, String contraseña) {
        Users usuario = userRepository.findByCorreoAndContraseña(correo, contraseña);
        // Si el usuario existe y la contraseña coincide
        if (usuario != null && usuario.getContraseña().equals(contraseña)) {
            return usuario;  // Devuelve el usuario completo
        } else {
            return null;  // Si el usuario no es encontrado o la contraseña es incorrecta
        }
    }

    public Optional<Users> getUserByCorreo(String correo) {
        return userRepository.findByCorreo(correo);
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByCorreo(email);
    }

    // Check if the email exists in the database
    public boolean doesEmailExist(String correo) {
        return userRepository.findByCorreo(correo).isPresent();
    }

}
