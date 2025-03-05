package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.model.AppointmentDTO;
import com.example.demo.model.Users;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    private final UserService userService;

    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.findById(id);
        return appointment != null ? ResponseEntity.ok(appointment) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.save(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
        Appointment updatedAppointment = appointmentService.update(id, appointmentDetails);
        return updatedAppointment != null ? ResponseEntity.ok(updatedAppointment) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getConsultationsByUser(@PathVariable Long userId) {
        List<Appointment> appointments = appointmentService.findAppointmentsByUserId(userId);

        if (appointments.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 si no hay citas
        }

        return ResponseEntity.ok(appointments); // HTTP 200 con la lista de citas
    }

    @PostMapping("/createDTO")
    public Appointment createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        // Buscar el usuario en la base de datos por ID
        Optional<Users> usuarioOptional = Optional.ofNullable(userService.findById(appointmentDTO.getUsuario_id()));

        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Users usuario = usuarioOptional.get();

        // Crear el objeto Appointment
        Appointment appointment = new Appointment();
        appointment.setUsuario(usuario);  // Asigna el usuario obtenido
        appointment.setFecha(LocalDate.parse(appointmentDTO.getFecha()));  // Convierte el String a LocalDate
        appointment.setHora(LocalTime.parse(appointmentDTO.getHora()));  // Convierte el String a LocalTime
        appointment.setDescripcion(appointmentDTO.getDescripcion());

        // Guardar la consulta médica
        return appointmentService.save(appointment);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        boolean deleted = appointmentService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment no encontrado");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        Appointment existingAppointment = appointmentService.findById(id);
        if (existingAppointment == null) {
            return ResponseEntity.notFound().build();
        }

        Users usuario = userService.findById(appointmentDTO.getUsuario_id());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        existingAppointment.setUsuario(usuario);
        existingAppointment.setFecha(LocalDate.parse(appointmentDTO.getFecha()));
        existingAppointment.setHora(LocalTime.parse(appointmentDTO.getHora()));
        existingAppointment.setDescripcion(appointmentDTO.getDescripcion());

        return ResponseEntity.ok(appointmentService.save(existingAppointment));
    }

}
