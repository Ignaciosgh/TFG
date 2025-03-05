package com.example.demo.controller;

import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<Users> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public Users createUser(@RequestBody Users user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        Users updatedUser = userService.update(id, userDetails);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Clase auxiliar para recibir el nombre y la contraseña en el cuerpo de la solicitud
    public static class LoginRequest {
        public String correo;
        public String contraseña;
    }

    @PostMapping("/login")
    public ResponseEntity<Users> autenticarUsuario(@RequestBody LoginRequest loginData) {
        String correo = loginData.correo;;
        String contraseña = loginData.contraseña;

        // Autenticar al usuario en la base de datos
        Users usuario = userService.autenticarUsuario(correo, contraseña);

        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Users> getUserByCorreo(@PathVariable String correo) {
        return userService.getUserByCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        // Validate if the email already exists
        if (userService.doesEmailExist(user.getCorreo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: El correo ya está registrado.");
        }

        // Save the new user
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario registrado con éxito.");
    }
}
