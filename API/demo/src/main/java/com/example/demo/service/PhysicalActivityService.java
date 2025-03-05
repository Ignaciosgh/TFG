package com.example.demo.service;

import com.example.demo.model.PhysicalActivity;
import com.example.demo.repository.PhysicalActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhysicalActivityService {
    private final PhysicalActivityRepository physicalActivityRepository;

    public PhysicalActivityService(PhysicalActivityRepository physicalActivityRepository) {
        this.physicalActivityRepository = physicalActivityRepository;
    }

    public List<PhysicalActivity> findAll() {
        return physicalActivityRepository.findAll();
    }

    public PhysicalActivity findById(Long id) {
        return physicalActivityRepository.findById(id).orElse(null);
    }

    public PhysicalActivity save(PhysicalActivity activity) {
        return physicalActivityRepository.save(activity);
    }

    public PhysicalActivity update(Long id, PhysicalActivity activityDetails) {
        return physicalActivityRepository.findById(id).map(activity -> {
            activity.setFecha(activityDetails.getFecha());
            activity.setTipo(activityDetails.getTipo());
            activity.setDuracion(activityDetails.getDuracion());
            return physicalActivityRepository.save(activity);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return physicalActivityRepository.findById(id).map(activity -> {
            physicalActivityRepository.delete(activity);
            return true;
        }).orElse(false);
    }

    public List<PhysicalActivity> findActivitiesByUserId(Long userId) {
        return physicalActivityRepository.findByUsuarioId(userId);
    }
}
