package com.example.demo.service;

import com.example.demo.model.ActivityLogs;
import com.example.demo.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    public List<ActivityLogs> findAll() {
        return activityLogRepository.findAll();
    }

    public ActivityLogs findById(Long id) {
        return activityLogRepository.findById(id).orElse(null);
    }

    public ActivityLogs save(ActivityLogs activityLog) {
        return activityLogRepository.save(activityLog);
    }

    public ActivityLogs update(Long id, ActivityLogs activityLogDetails) {
        return activityLogRepository.findById(id).map(activityLog -> {
            activityLog.setAccion(activityLogDetails.getAccion());
            activityLog.setDescripcion(activityLogDetails.getDescripcion());
            activityLog.setFecha(activityLogDetails.getFecha());
            return activityLogRepository.save(activityLog);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return activityLogRepository.findById(id).map(activityLog -> {
            activityLogRepository.delete(activityLog);
            return true;
        }).orElse(false);
    }
}
