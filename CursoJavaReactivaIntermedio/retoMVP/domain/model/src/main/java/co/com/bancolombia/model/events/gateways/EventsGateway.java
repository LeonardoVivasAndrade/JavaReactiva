package co.com.bancolombia.model.events.gateways;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.movement.MovementUploadReport;
import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> boxCreatedEvent(Box box);
    Mono<Void> boxNameUpdatedEvent(Object event);
    Mono<Void> boxDeletedEvent(Object event);
    Mono<Void> boxReopenedEvent(Object event);
    Mono<Void> boxMovementsUploadedEvent(MovementUploadReport movementUploadReport);
}

