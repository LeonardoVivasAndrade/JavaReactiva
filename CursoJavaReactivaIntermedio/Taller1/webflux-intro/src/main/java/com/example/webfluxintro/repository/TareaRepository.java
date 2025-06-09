package com.example.webfluxintro.repository;

import com.example.webfluxintro.model.Tarea;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository // Anotación para que Spring pueda detectarlo y gestionarlo

public class TareaRepository {
    private final Map<String, Tarea> tareasEnMemoria = new ConcurrentHashMap<>();

    public Mono<Tarea> findById(String id) {
        return Mono.justOrEmpty(tareasEnMemoria.get(id));
    }

    public Flux<Tarea> findAll() {
        return Flux.fromIterable(tareasEnMemoria.values());
    }

    public Mono<Tarea> save(Tarea tarea) {
        if (tarea.getId() == null || tarea.getId().isEmpty()) {
            // Si es una tarea nueva sin ID explícito en el DTO (aunque nuestro constructor lo asigna)
            // Esto es más para cuando el DTO de entrada no tiene ID
            tarea.setId(java.util.UUID.randomUUID().toString());
        }
        tareasEnMemoria.put(tarea.getId(), tarea);
        return Mono.just(tarea);
    }

    public Mono<Tarea> update(String id, Tarea tareaActualizada) {
        return findById(id)
                .flatMap(existente -> {
                    existente.setDescripcion(tareaActualizada.getDescripcion());
                    existente.setCompletada(tareaActualizada.isCompletada());
                    tareasEnMemoria.put(id, existente);
                    return Mono.just(existente);
                });
    }

    public Mono<Void> deleteById(String id) {
        tareasEnMemoria.remove(id);
        return Mono.empty(); // Indica que la operación se completó sin emitir valor
    }
}