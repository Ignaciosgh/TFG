package com.example.demo.service;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MedicationReminderService {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final NotifiService notifiService;

    public MedicationReminderService(NotifiService notifiService) {
        this.notifiService = notifiService;
    }

    public void startReminderService() {
        scheduler.scheduleAtFixedRate(() -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8089/medmonitordb", "mysql", "mysql");
                 Statement stmt = conn.createStatement()){

                String query = "SELECT r.*, m.usuario_id FROM Reminders r JOIN Medications m ON r.medicamento_id = m.id WHERE r.hora = NOW()";

                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    Long userId = rs.getLong("usuario_id");
                    Long reminderId = rs.getLong("id");
                    String message = "Es hora de tomar tu medicamento";
                    notifiService.sendNotification(userId, reminderId, message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0,1, TimeUnit.MINUTES); // Verifica cada minuto
    }
}
