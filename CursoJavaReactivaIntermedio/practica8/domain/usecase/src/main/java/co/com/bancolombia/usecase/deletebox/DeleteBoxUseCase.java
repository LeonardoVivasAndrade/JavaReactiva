package co.com.bancolombia.usecase.deletebox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class DeleteBoxUseCase {
    private final BoxRepository repository;
    private final EventsGateway gateway;
    private static final Logger log = Logger.getLogger(DeleteBoxUseCase.class.getName());

    public Mono<Box> delete(String id, String responsible) {
        return repository.findById(id)
                .flatMap(existingBox -> {
                    existingBox.setStatus(BoxStatus.DELETED);
                    return repository.save(existingBox)
                            .flatMap(box -> gateway.boxDeletedEvent(Map.of(
                                                    "box-id", box.getId(),
                                                    "responsible", responsible,
                                                    "deleted-at", LocalDateTime.now().toString()
                                            ))
                                            .doOnError(error -> log.log(Level.WARNING, "Error al emitir evento boxDeletedEvent: {0}", error.getMessage()))
                                            .onErrorResume(error -> Mono.empty())
                                            .thenReturn(box)
                            );
                })
                .switchIfEmpty(Mono.empty());
    }
}
