package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification update(Long id, Notification notificationDetails) {
        return notificationRepository.findById(id).map(notification -> {
            notification.setTipo(notificationDetails.getTipo());
            notification.setMensaje(notificationDetails.getMensaje());
            notification.setFecha(notificationDetails.getFecha());
            return notificationRepository.save(notification);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return notificationRepository.findById(id).map(notification -> {
            notificationRepository.delete(notification);
            return true;
        }).orElse(false);
    }
}
