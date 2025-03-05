package com.example.demo.service;

import com.example.demo.model.UserSetting;
import com.example.demo.repository.UserSettingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSettingService {
    private final UserSettingRepository userSettingRepository;

    public UserSettingService(UserSettingRepository userSettingRepository) {
        this.userSettingRepository = userSettingRepository;
    }

    public List<UserSetting> findAll() {
        return userSettingRepository.findAll();
    }

    public UserSetting findById(Long id) {
        return userSettingRepository.findById(id).orElse(null);
    }

    public UserSetting save(UserSetting userSetting) {
        return userSettingRepository.save(userSetting);
    }

    public UserSetting update(Long id, UserSetting userSettingDetails) {
        return userSettingRepository.findById(id).map(userSetting -> {
            userSetting.setNotificaciones(userSettingDetails.isNotificaciones());
            userSetting.setSonido(userSettingDetails.getSonido());
            return userSettingRepository.save(userSetting);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return userSettingRepository.findById(id).map(userSetting -> {
            userSettingRepository.delete(userSetting);
            return true;
        }).orElse(false);
    }
}
