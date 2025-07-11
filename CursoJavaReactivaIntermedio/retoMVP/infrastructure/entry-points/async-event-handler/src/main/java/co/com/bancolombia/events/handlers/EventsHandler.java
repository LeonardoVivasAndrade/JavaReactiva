package co.com.bancolombia.events.handlers;

import lombok.AllArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import lombok.extern.java.Log;
import java.util.logging.Level;
@Log
@AllArgsConstructor
@EnableEventListeners
public class EventsHandler {

    public Mono<Void> handleEventA(DomainEvent<Object> event) {
        log.log(Level.INFO, "Event received: {0} -> {1}", new Object[]{event.getName(), event.getData()});
        return Mono.empty();
    }
}
