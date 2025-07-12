package co.com.bancolombia.events;

import co.com.bancolombia.events.handlers.EventsHandler;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerRegistryConfiguration {

    // see more at: https://reactivecommons.org/reactive-commons-java/#_handlerregistry_2
    @Bean
    public HandlerRegistry handlerRegistry(EventsHandler events) {
        return HandlerRegistry.register()
                .listenEvent("box.event.created", events::handleEventA, Object.class)
                .listenEvent("box.event.nameUpdated", events::handleEventA, Object.class)
                .listenEvent("box.event.deleted", events::handleEventA, Object.class)
                .listenEvent("box.event.reopen", events::handleEventA, Object.class)
                .listenEvent("movements.event.upload", events::handleEventA, Object.class);
    }
}
