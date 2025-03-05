package com.example.demo.service;

import com.example.demo.model.Mood;
import com.example.demo.repository.MoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {
    private final MoodRepository moodRepository;

    public MoodService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    public List<Mood> findAll() {
        return moodRepository.findAll();
    }

    public Mood findById(Long id) {
        return moodRepository.findById(id).orElse(null);
    }

    public Mood save(Mood mood) {
        return moodRepository.save(mood);
    }

    public Mood update(Long id, Mood moodDetails) {
        return moodRepository.findById(id).map(mood -> {
            mood.setFecha(moodDetails.getFecha());
            mood.setEstado(moodDetails.getEstado());
            mood.setNota(moodDetails.getNota());
            return moodRepository.save(mood);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (moodRepository.existsById(id)) {
            moodRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Mood> findMoodsByUserId(Long userId) {
        return moodRepository.findByUsuarioId(userId);
    }
}
