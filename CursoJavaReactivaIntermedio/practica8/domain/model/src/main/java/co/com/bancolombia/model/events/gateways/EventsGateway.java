package co.com.bancolombia.model.events.gateways;

import co.com.bancolombia.model.box.Box;
import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> boxCreatedEvent(Box box);
    Mono<Void> boxNameUpdatedEvent(Object event);
    Mono<Void> boxDeletedEvent(Object event);
    Mono<Void> boxReopenedEvent(Object event);
}

