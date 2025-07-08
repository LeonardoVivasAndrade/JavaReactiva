package co.com.bancolombia.usecase.createbox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class CreateboxUseCase {
    private final BoxRepository repository;
    private final EventsGateway gateway;
    private static final Logger log = Logger.getLogger(CreateboxUseCase.class.getName());

    public Mono<Box> create(String id, String name, String createdBy) {
        return repository.findById(id)
                .flatMap(existe -> Mono.<Box>error(new IllegalArgumentException("La caja ya existe")))
                .switchIfEmpty(repository.save(Box.builder()
                                .id(id)
                                .name(name)
                                .status(BoxStatus.CLOSED)
                                .currentBalance(BigDecimal.ZERO)
                                .createdAt(LocalDateTime.now().toString())
                                .createdBy(createdBy)
                                .build())
                        .flatMap(box -> gateway.boxCreatedEvent(box)
                                .doOnError(error -> log.log(Level.WARNING, "Error al emitir evento boxCreatedEvent: {0}", error.getMessage()))
                                .onErrorResume(error -> Mono.empty())
                                .thenReturn(box))
                );
    }
}
