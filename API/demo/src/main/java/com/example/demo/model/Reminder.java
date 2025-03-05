package com.example.demo.model;

import com.example.demo.Enum.ReminderFrequency;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    private Medication medicamento_id;

    private LocalTime hora;
    @Enumerated(EnumType.STRING)
    private ReminderFrequency frecuencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medication getMedicamento_id() {
        return medicamento_id;
    }

    public void setMedicamento_id(Medication medicamento_id) {
        this.medicamento_id = medicamento_id;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public ReminderFrequency getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(ReminderFrequency frecuencia) {
        this.frecuencia = frecuencia;
    }
}
