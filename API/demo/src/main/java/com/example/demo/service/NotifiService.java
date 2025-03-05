package com.example.demo.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class NotifiService {

    public void sendNotification(Long userId, Long reminderId, String message) {
        Message firebaseMessage = Message.builder()
                .putData("userId", userId.toString())
                .putData("reminderId", reminderId.toString())
                .putData("message", message)
                .setTopic("user_" + userId)
                .build();
        FirebaseMessaging.getInstance().sendAsync(firebaseMessage).addListener(() -> {
            System.out.println("Notificacion send to user: " + userId);
        }, Runnable::run);

    }

}
