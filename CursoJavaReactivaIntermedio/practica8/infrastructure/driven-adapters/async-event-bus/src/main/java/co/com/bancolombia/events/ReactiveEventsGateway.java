package co.com.bancolombia.events;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;

import static reactor.core.publisher.Mono.from;

@Log
@RequiredArgsConstructor
@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String BOX_EVENT_CREATED = "box.event.created";
    public static final String BOX_EVENT_NAME_UPDATED = "box.event.nameUpdated";
    public static final String BOX_EVENT_DELETED = "box.event.deleted";
    public static final String BOX_EVENT_REOPEN = "box.event.reopen";
    private final DomainEventBus domainEventBus;

    @Override
    public Mono<Void> boxCreatedEvent(Box box) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{BOX_EVENT_CREATED, box.toString()});
         return from(domainEventBus.emit(new DomainEvent<>(BOX_EVENT_CREATED, UUID.randomUUID().toString(), box)));
    }

    @Override
    public Mono<Void> boxNameUpdatedEvent(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{BOX_EVENT_NAME_UPDATED, event.toString()});
        return from(domainEventBus.emit(new DomainEvent<>(BOX_EVENT_NAME_UPDATED, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> boxDeletedEvent(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{BOX_EVENT_DELETED, event.toString()});
        return from(domainEventBus.emit(new DomainEvent<>(BOX_EVENT_DELETED, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> boxReopenedEvent(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}", new String[]{BOX_EVENT_REOPEN, event.toString()});
        return from(domainEventBus.emit(new DomainEvent<>(BOX_EVENT_REOPEN, UUID.randomUUID().toString(), event)));
    }
}
