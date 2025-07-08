package co.com.bancolombia.usecase.reopenbox;

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
public class ReopenBoxUseCase {
    private final BoxRepository repository;
    private final EventsGateway gateway;
    private static final Logger log = Logger.getLogger(ReopenBoxUseCase.class.getName());

    public Mono<Box> reopen(String id, String responsible) {
        return repository.findById(id)
                .flatMap(existingBox -> repository.save(existingBox)
                        .flatMap(box -> gateway.boxReopenedEvent(Map.of(
                                        "box", box,
                                        "responsible", responsible,
                                        "reopen-at", LocalDateTime.now().toString()
                                ))
                                .doOnError(error -> log.log(Level.WARNING, "Error al emitir evento boxReopenedEvent: {0}", error.getMessage()))
                                .onErrorResume(error -> Mono.empty())
                                .thenReturn(box))
                )
                .switchIfEmpty(Mono.empty());
    }
}
