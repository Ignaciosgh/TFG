package com.example.demo.controller;

import com.example.demo.model.PhysicalActivity;
import com.example.demo.model.PhysicalActivityDTO;
import com.example.demo.model.Users;
import com.example.demo.service.PhysicalActivityService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activities")
public class PhysicalActivityController {

    private final PhysicalActivityService physicalActivityService;

    private final UserService userService;

    public PhysicalActivityController(PhysicalActivityService physicalActivityService, UserService userService) {
        this.physicalActivityService = physicalActivityService;
        this.userService = userService;
    }

    @GetMapping()
    public List<PhysicalActivity> getAllActivities() {
        return physicalActivityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhysicalActivity> getActivityById(@PathVariable Long id) {
        PhysicalActivity activity = physicalActivityService.findById(id);
        return activity != null ? ResponseEntity.ok(activity) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public PhysicalActivity createActivity(@RequestBody PhysicalActivity activity) {
        return physicalActivityService.save(activity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhysicalActivity> updateActivity(@PathVariable Long id, @RequestBody PhysicalActivity activityDetails) {
        PhysicalActivity updatedActivity = physicalActivityService.update(id, activityDetails);
        return updatedActivity != null ? ResponseEntity.ok(updatedActivity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        boolean deleted = physicalActivityService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PhysicalActivity>> getActivitiesByUser(@PathVariable Long userId) {
        List<PhysicalActivity> activities = physicalActivityService.findActivitiesByUserId(userId);

        if (activities.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 si no hay actividades
        }

        return ResponseEntity.ok(activities); // HTTP 200 con la lista de actividades
    }

    @PostMapping("/createDTO")
    public PhysicalActivity createPhysicalActivity(@RequestBody PhysicalActivityDTO physicalActivityDTO) {
        // Buscar el usuario en la base de datos por ID
        Optional<Users> usuarioOptional = Optional.ofNullable(userService.findById(physicalActivityDTO.getUsuario_id()));

        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Users usuario = usuarioOptional.get();

        // Crear el objeto PhysicalActivity
        PhysicalActivity physicalActivity = new PhysicalActivity();
        physicalActivity.setUsuario(usuario);  // Asigna el usuario obtenido
        physicalActivity.setTipo(physicalActivityDTO.getTipo()); // Tipo de actividad física (suponiendo un String o Enum)
        physicalActivity.setDuracion(physicalActivityDTO.getDuracion()); // Duración (en minutos o formato de tiempo)
        physicalActivity.setFecha(LocalDate.parse(physicalActivityDTO.getFecha().toString())); // Convierte el String a LocalDate

        // Guardar la actividad física
        return physicalActivityService.save(physicalActivity);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PhysicalActivity> updatePhysicalActivity(
            @PathVariable Long id,
            @RequestBody PhysicalActivity updatedActivity) {

        // Buscar la actividad física existente por ID
        PhysicalActivity existingActivity = physicalActivityService.findById(id);
        if (existingActivity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada");
        }

        // Si el usuario cambia, asegúrate de buscar y asignar al objeto completo
        if (updatedActivity.getUsuario() != null &&
                !updatedActivity.getUsuario().getId().equals(existingActivity.getUsuario().getId())) {
            Users user = userService.findById(updatedActivity.getUsuario().getId());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            existingActivity.setUsuario(user);
        }

        // Actualizar los campos de la actividad física
        existingActivity.setFecha(updatedActivity.getFecha());
        existingActivity.setTipo(updatedActivity.getTipo());
        existingActivity.setDuracion(updatedActivity.getDuracion());

        // Guardar los cambios en la base de datos
        PhysicalActivity updatedPhysicalActivity = physicalActivityService.save(existingActivity);

        return ResponseEntity.ok(updatedPhysicalActivity);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePhysicalActivity(@PathVariable Long id) {
        boolean deleted = physicalActivityService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Retorna 204 si se elimina con éxito
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad física no encontrada"); // Lanza error 404 si no existe
        }
    }


}
