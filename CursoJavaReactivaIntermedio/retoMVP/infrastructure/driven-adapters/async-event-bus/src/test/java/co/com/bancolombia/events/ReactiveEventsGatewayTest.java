package co.com.bancolombia.events;

import co.com.bancolombia.model.box.Box;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import org.reactivecommons.api.domain.DomainEventBus;

import org.reactivecommons.api.domain.DomainEvent;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReactiveEventsGatewayTest {

    @Mock
    private DomainEventBus domainEventBus;

    private ReactiveEventsGateway reactiveEventsGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reactiveEventsGateway = new ReactiveEventsGateway(domainEventBus);
    }

    @Test
    void testEmitEventBoxCreatedLogsEventEvent() {
        Object event = new Object() {
            @Override
            public String toString() {
                return "testEvent";
            }
        };

        when(domainEventBus.emit(any(DomainEvent.class))).thenReturn(Mono.empty());

        reactiveEventsGateway.boxCreatedEvent((Box)event).block();

        verify(domainEventBus, times(1)).emit(any(DomainEvent.class));
    }


    @Test
    void testEmitEventBoxCreatedConstructsDomainEventEvent() {
        Object event = new Object() {
            @Override
            public String toString() {
                return "testEvent";
            }
        };

        when(domainEventBus.emit(any(DomainEvent.class))).thenReturn(Mono.empty());

        reactiveEventsGateway.boxCreatedEvent((Box)event).block();

        ArgumentCaptor<DomainEvent> eventCaptor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(domainEventBus, times(1)).emit(eventCaptor.capture());

        DomainEvent capturedEvent = eventCaptor.getValue();
        assertEquals(ReactiveEventsGateway.BOX_EVENT_CREATED, capturedEvent.getName());
        assertEquals(event.toString(), capturedEvent.getData().toString());
    }

}
