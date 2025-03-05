package com.example.demo.controller;

import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Enum.Status;
import com.example.demo.model.Medication;
import com.example.demo.model.MedicationDTO;
import com.example.demo.model.Users;
import com.example.demo.repository.MedicationRepository;
import com.example.demo.service.MedicationService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    private final UserService userService;
    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationService medicationService, UserService userService,
                                MedicationRepository medicationRepository) {
        this.medicationService = medicationService;
        this.userService = userService;
        this.medicationRepository = medicationRepository;
    }

    @GetMapping("/all")
    public List<Medication> getAllMedications() {
        return medicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        Medication medication = medicationService.findById(id);
        return medication != null ? ResponseEntity.ok(medication) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Medication>> getMedicationsByUserId(@PathVariable Long userId) {
        List<Medication> medications = medicationService.findByUserId(userId);
        return medications != null && !medications.isEmpty()
                ? ResponseEntity.ok(medications)
                : ResponseEntity.notFound().build();
    }


    @PostMapping("/createMedication")
    public Medication createMedication(@RequestBody Medication medication) {
        return medicationService.save(medication);
    }

    @PostMapping("/create")
    public Medication createMedicationM(@RequestBody MedicationDTO medicationDTO) {
        // Buscar el usuario usando el ID proporcionado
        Users usuario = userService.findById(medicationDTO.getUsuario_id());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        // Mapear el DTO al modelo `Medication`
        Medication medication = new Medication();
        medication.setUsuario(usuario);
        medication.setNombre(medicationDTO.getNombre());
        medication.setDescripcion(medicationDTO.getDescripcion());
        medication.setDias(medicationDTO.getDias());
        medication.setStatus(medicationDTO.getStatus());
        medication.setComprimidos(medicationDTO.getComprimidos());
        medication.setVeces_al_dia(medicationDTO.getVeces_al_dia());
        medication.setHoras(medicationDTO.getHoras());

        return medicationService.save(medication);
    }

    @PutMapping("/medications/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long id, @RequestBody MedicationDTO medicationDTO) {
        Medication medication = medicationService.findById(id);
        if (medication == null) {
            return ResponseEntity.notFound().build();
        }

        // Buscar el usuario por ID
        Users usuario = userService.findById(medicationDTO.getUsuario_id());
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        // Actualizar los datos
        medication.setNombre(medicationDTO.getNombre());
        medication.setDescripcion(medicationDTO.getDescripcion());
        medication.setUsuario(usuario); // Asignar el usuario encontrado
        medication.setDias(medicationDTO.getDias());
        medication.setStatus(medicationDTO.getStatus());
        medication.setComprimidos(medicationDTO.getComprimidos());
        medication.setVeces_al_dia(medicationDTO.getVeces_al_dia());
        medication.setHoras(medicationDTO.getHoras());

        // Guardar el medicamento
        Medication updatedMedication = medicationService.save(medication);
        return ResponseEntity.ok(updatedMedication);
    }



    @PostMapping("/createDTO")
    public Medication createMedication(@RequestBody MedicationDTO medicationDTO) {
        // Buscar el usuario en la base de datos por ID
        Optional<Users> usuarioOptional = Optional.ofNullable(userService.findById(medicationDTO.getUsuario_id()));

        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Users usuario = usuarioOptional.get();


        Medication medication = new Medication();
        medication.setNombre(medicationDTO.getNombre());
        medication.setDescripcion(medicationDTO.getDescripcion());
        medication.setUsuario(usuario);  // Asigna el usuario obtenido
        medication.setDias(medicationDTO.getDias());
        medication.setStatus(medicationDTO.getStatus());
        medication.setComprimidos(medicationDTO.getComprimidos());
        medication.setVeces_al_dia(medicationDTO.getVeces_al_dia());
        medication.setHoras(medicationDTO.getHoras());

        // Guardar el medicamento
        return medicationService.save(medication);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedicationE(@PathVariable Long id, @RequestBody Medication medicationDetails) {
        try {
            Medication updatedMedication = medicationService.update(id, medicationDetails);
            return ResponseEntity.ok(updatedMedication); // Devuelve el medicamento actualizado
        } catch (MedicationService.ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Responde con 404 si no se encuentra el medicamento
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        boolean deleted = medicationService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Retorna 204 si se elimina con éxito
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento no encontrado"); // Lanza error 404 si no existe
        }
    }


    @PutMapping("/updateDT/{id}")
    public Medication updateMedicationD(
            @PathVariable Long id,
            @RequestBody MedicationDTO updatedFields) {

        // Buscar el medicamento existente en la base de datos
        Optional<Medication> optionalMedication = Optional.ofNullable(medicationService.findById(id));
        if (optionalMedication.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento no encontrado");
        }

        Medication existingMedication = optionalMedication.get();

        // Actualizar los campos del medicamento
        existingMedication.setNombre(updatedFields.getNombre());
        existingMedication.setDescripcion(updatedFields.getDescripcion());

        // Buscar el usuario asociado al medicamento, si es necesario
        if (updatedFields.getUsuario_id() != null) {
            Optional<Users> optionalUsuario = Optional.ofNullable(userService.findById(updatedFields.getUsuario_id()));
            if (optionalUsuario.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            existingMedication.setUsuario(optionalUsuario.get());
        }

        // Actualizar días como enum
        try {
            existingMedication.setDias(DayOfWeek.valueOf(updatedFields.getDias().name())); // Asegúrate de usar el enum
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Día inválido");
        }

        // Actualizar status como enum
        try {
            existingMedication.setStatus(Status.valueOf(updatedFields.getStatus().name())); // Asegúrate de usar el enum
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido");
        }

        existingMedication.setComprimidos(updatedFields.getComprimidos());
        existingMedication.setVeces_al_dia(updatedFields.getVeces_al_dia());
        existingMedication.setHoras(updatedFields.getHoras());

        // Guardar el medicamento actualizado
        return medicationService.save(existingMedication);
    }

    @PutMapping("/updateD/{id}")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable Long id,
            @RequestBody Medication medication) {

        // Buscar el medicamento existente por ID
        Medication existingMedication = medicationService.findById(id);
        if (existingMedication == null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }


        // Actualizar los campos del medicamento
        existingMedication.setNombre(medication.getNombre());
        existingMedication.setDescripcion(medication.getDescripcion());
        existingMedication.setDias(medication.getDias());
        existingMedication.setStatus(medication.getStatus());
        existingMedication.setComprimidos(medication.getComprimidos());
        existingMedication.setVeces_al_dia(medication.getVeces_al_dia());
        existingMedication.setHoras(medication.getHoras());

        // Si el usuario cambia, asegúrate de buscar y asignar al objeto completo
        if (medication.getUsuario() != null && !medication.getUsuario().getId().equals(existingMedication.getUsuario().getId())) {
            Users user = userService.findById(medication.getUsuario().getId());
            if (user == null) {
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            existingMedication.setUsuario(user);
        }

        // Guardar cambios
        Medication updatedMedication = medicationService.save(existingMedication);

        return ResponseEntity.ok(updatedMedication);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateMedicationStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Medication> medicationOptional = medicationRepository.findById(id);
        if (medicationOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medication medication = medicationOptional.get();
        medication.setStatus(Status.valueOf(status));
        medicationRepository.save(medication);
        return ResponseEntity.ok().build();
    }

}
