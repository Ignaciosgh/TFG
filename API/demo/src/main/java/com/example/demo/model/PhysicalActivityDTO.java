package com.example.demo.model;

import com.example.demo.Enum.PhysicalActivityTipe;

import java.time.LocalDate;

public class PhysicalActivityDTO {

    private Long id;
    private Long usuario_id;
    private LocalDate fecha;
    private PhysicalActivityTipe tipo;
    private int duracion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public PhysicalActivityTipe getTipo() {
        return tipo;
    }

    public void setTipo(PhysicalActivityTipe tipo) {
        this.tipo = tipo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
