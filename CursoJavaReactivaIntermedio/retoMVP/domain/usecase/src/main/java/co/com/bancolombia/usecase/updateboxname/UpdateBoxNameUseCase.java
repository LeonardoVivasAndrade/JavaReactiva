package co.com.bancolombia.usecase.updateboxname;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UpdateBoxNameUseCase {
    private final BoxRepository repository;
    private final EventsGateway gateway;
    private static final Logger log = Logger.getLogger(UpdateBoxNameUseCase.class.getName());

    public Mono<Box> update(String id, String name) {
        return repository.findById(id)
                .flatMap(existingBox -> {
                    var oldName = existingBox.getName();
                    existingBox.setName(name);
                    return repository.save(existingBox)
                            .flatMap(box -> gateway.boxNameUpdatedEvent(Map.of(
                                                    "box-id", box.getId(),
                                                    "old-name", oldName,
                                                    "new-name", box.getName(),
                                                    "update-at", LocalDateTime.now().toString()
                                            ))
                                            .doOnError(error -> log.log(Level.WARNING, "Error al emitir evento boxNameUpdatedEvent: {0}", error.getMessage()))
                                            .onErrorResume(error -> Mono.empty())
                                            .thenReturn(box)
                            );
                })
                .switchIfEmpty(Mono.empty());
    }
}
