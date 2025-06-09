package com.example.webfluxintro.controller;

import com.example.webfluxintro.model.Tarea;
import com.example.webfluxintro.repository.TareaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tareas") // Ruta base para todos los endpoints de este controlador
public class TareaController {
    private final TareaRepository tareaRepository;

    // Inyección de dependencias vía constructor
    public TareaController(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created por defecto si tiene éxito
    public Mono<Tarea> crearTarea(@RequestBody Tarea tarea) {
        // El constructor de Tarea ya asigna un ID si no se provee uno en el cuerpo
        // Si el cuerpo ya trae un ID, se usa ese.
        // Para un POST real, a menudo se ignora el ID del cuerpo y siempre se genera uno nuevo.
        // Por simplicidad, si el JSON no tiene id, nuestro constructor Tarea() lo crea.
        // Si el JSON tiene un ID, se usa ese.
        if (tarea.getId() == null || tarea.getId().isBlank()) {
            // Si el cliente envía una tarea sin ID, creamos una nueva instancia que sí lo genere
            Tarea nuevaTarea = new Tarea(tarea.getDescripcion());
            nuevaTarea.setCompletada(tarea.isCompletada());
            return tareaRepository.save(nuevaTarea);
        }
        return tareaRepository.save(tarea);
    }

    @GetMapping
    public Flux<Tarea> obtenerTodasLasTareas() {
        return tareaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Tarea>> obtenerTareaPorId(@PathVariable String id) {
        return tareaRepository.findById(id)
                .map(ResponseEntity::ok) // Si se encuentra, envuelve en ResponseEntity OK (200)
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no, 404 Not Found
    }


    @PutMapping("/{id}")
    public Mono<ResponseEntity<Tarea>> actualizarTarea(@PathVariable String id, @RequestBody Tarea tarea) {
        return tareaRepository.update(id, tarea)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content si tiene éxito
    public Mono<ResponseEntity<Void>> eliminarTarea(@PathVariable String id) {
        // Podríamos verificar si existe primero y devolver 404 si no,
        // pero por simplicidad, deleteById simplemente intenta eliminar.
        // Para un comportamiento más RESTful, se podría hacer:
         return tareaRepository.findById(id)
              .flatMap(t -> tareaRepository.deleteById(t.getId()).then(Mono.just(ResponseEntity.noContent().<Void>build())))
              .defaultIfEmpty(ResponseEntity.notFound().build());
//        return tareaRepository.deleteById(id);
    }

}
