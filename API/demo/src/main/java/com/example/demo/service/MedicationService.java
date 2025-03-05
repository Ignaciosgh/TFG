package com.example.demo.service;

import com.example.demo.Enum.Status;
import com.example.demo.model.Medication;
import com.example.demo.model.MedicationDTO;
import com.example.demo.repository.MedicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }
    public List<Medication> findByUserId(Long userId) {
        return medicationRepository.findByUsuario_Id(userId);
    }

    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    public Medication findById(Long id) {
        return medicationRepository.findById(id).orElse(null);
    }

    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }


    public Medication update(Long id, Medication medicationDetails) {
        return medicationRepository.findById(id).map(medication -> {
            // Actualización de los detalles del medicamento
            medication.setNombre(medicationDetails.getNombre());
            medication.setDescripcion(medicationDetails.getDescripcion());
            medication.setDias(medicationDetails.getDias());
            medication.setStatus(medicationDetails.getStatus());
            medication.setComprimidos(medicationDetails.getComprimidos());
            medication.setVeces_al_dia(medicationDetails.getVeces_al_dia());
            medication.setHoras(medicationDetails.getHoras());

            // Guardar el medicamento actualizado
            return medicationRepository.save(medication);
        }).orElseThrow(() -> new ResourceNotFoundException("Medication", "id", id)); // Lanza una excepción si no se encuentra el medicamento
    }

    public boolean delete(Long id) {
        Optional<Medication> medicationOptional = medicationRepository.findById(id);
        if (medicationOptional.isPresent()) {
            medicationRepository.deleteById(id);
            return true; // Confirmar eliminación
        } else {
            return false; // No encontrado
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String resource, String field, Object value) {
            super(String.format("%s not found with %s : '%s'", resource, field, value));
        }
    }

    public Medication updateMedicationStatus(Long id, String status) {
        Medication medication = medicationRepository.findById(id).orElseThrow();
        medication.setStatus(Status.valueOf(status.toUpperCase()));
        return medicationRepository.save(medication);
    }

}
