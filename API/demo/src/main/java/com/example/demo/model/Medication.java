package com.example.demo.model;

import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Enum.Status;
import jakarta.persistence.*;

@Entity
@Table(name = "medications")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Users usuario;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dias;

    @Enumerated(EnumType.STRING)
    private Status status;
    private int comprimidos;
    private int veces_al_dia;
    private String horas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario_id) {
        this.usuario = usuario_id;
    }

    public DayOfWeek getDias() {
        return dias;
    }

    public void setDias(DayOfWeek dias) {
        this.dias = dias;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getComprimidos() {
        return comprimidos;
    }

    public void setComprimidos(int comprimidos) {
        this.comprimidos = comprimidos;
    }

    public int getVeces_al_dia() {
        return veces_al_dia;
    }

    public void setVeces_al_dia(int veces_al_dia) {
        this.veces_al_dia = veces_al_dia;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }
}
