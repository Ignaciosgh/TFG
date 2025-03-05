package com.example.demo.controller;

import com.example.demo.model.UserSetting;
import com.example.demo.service.UserSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-settings")
public class UserSettingController {
    private final UserSettingService userSettingService;

    public UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @GetMapping
    public List<UserSetting> getAllUserSettings() {
        return userSettingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSetting> getUserSettingById(@PathVariable Long id) {
        UserSetting userSetting = userSettingService.findById(id);
        return userSetting != null ? ResponseEntity.ok(userSetting) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public UserSetting createUserSetting(@RequestBody UserSetting userSetting) {
        return userSettingService.save(userSetting);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSetting> updateUserSetting(@PathVariable Long id, @RequestBody UserSetting userSettingDetails) {
        UserSetting updatedUserSetting = userSettingService.update(id, userSettingDetails);
        return updatedUserSetting != null ? ResponseEntity.ok(updatedUserSetting) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSetting(@PathVariable Long id) {
        boolean deleted = userSettingService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
