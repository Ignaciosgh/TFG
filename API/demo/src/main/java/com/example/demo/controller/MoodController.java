package com.example.demo.controller;

import com.example.demo.Enum.MoodState;
import com.example.demo.model.Mood;
import com.example.demo.model.MoodDTO;
import com.example.demo.model.Users;
import com.example.demo.service.MoodService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mood")
public class MoodController {

    private final MoodService moodService;

    private final UserService userService;

    public MoodController(MoodService moodService, UserService userService) {
        this.moodService = moodService;
        this.userService = userService;
    }

    @GetMapping
    public List<Mood> getAllMoods() {
        return moodService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mood> getMoodById(@PathVariable Long id) {
        Mood mood = moodService.findById(id);
        return mood != null ? ResponseEntity.ok(mood) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Mood createMood(@RequestBody Mood mood) {
        return moodService.save(mood);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Mood>> getMoodsByUser(@PathVariable Long userId) {
        List<Mood> moods = moodService.findMoodsByUserId(userId);

        if (moods.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 si no hay moods
        }

        return ResponseEntity.ok(moods); // HTTP 200 con la lista de moods
    }

    @PostMapping("/createDTO")
    public Mood createMood(@RequestBody MoodDTO moodDTO) {
        Optional<Users> usuarioOptional = Optional.ofNullable(userService.findById(moodDTO.getUsuario_id()));

        if (usuarioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        Users usuario = usuarioOptional.get();

        Mood mood = new Mood();
        mood.setUsuario(usuario);
        mood.setFecha(LocalDate.parse(moodDTO.getFecha().toString()));
        mood.setEstado(MoodState.valueOf(moodDTO.getEstado()));
        mood.setNota(moodDTO.getNota());

        return moodService.save(mood);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Mood> updateMood(@PathVariable Long id, @RequestBody Mood updatedMood) {

        // Find the existing mood entry by ID
        Mood existingMood = moodService.findById(id);
        if (existingMood == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado de ánimo no encontrado");
        }

        // If the user changes, fetch and assign the full user object
        if (updatedMood.getUsuario() != null &&
                !updatedMood.getUsuario().getId().equals(existingMood.getUsuario().getId())) {
            Users user = userService.findById(updatedMood.getUsuario().getId());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }
            existingMood.setUsuario(user);
        }

        // Update the fields of the mood
        existingMood.setFecha(updatedMood.getFecha());
        existingMood.setNota(updatedMood.getNota());
        existingMood.setEstado(updatedMood.getEstado());

        // Save changes to the database
        Mood updatedMoodEntry = moodService.save(existingMood);

        return ResponseEntity.ok(updatedMoodEntry);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Long id) {
        boolean deleted = moodService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // Returns 204 if successfully deleted
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estado de ánimo no encontrado"); // Throws 404 if not found
        }
    }
}
