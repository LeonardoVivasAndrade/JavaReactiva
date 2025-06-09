package com.example.webfluxintro.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Tarea {
    private String id;
    private String descripcion;
    private boolean completada;

    // Constructor para nuevas tareas (sin ID, se generará)
    public Tarea(String descripcion) {
        this.id = UUID.randomUUID().toString(); // Genera un ID único
        this.descripcion = descripcion;
        this.completada = false;
    }

    // Constructor para deserialización o creación con ID
    public Tarea(String id, String descripcion, boolean completada) {
        this.id = id;
        this.descripcion = descripcion;
        this.completada = completada;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", completada=" + completada +
                '}';
    }
}
