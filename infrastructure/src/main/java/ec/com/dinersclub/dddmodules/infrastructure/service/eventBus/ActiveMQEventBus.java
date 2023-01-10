package ec.com.dinersclub.dddmodules.infrastructure.service.eventBus;

import com.fasterxml.jackson.core.JsonProcessingException;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.ResponseEvent;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ActiveMQEventBus implements EventBus {

    @Inject
    @Channel("event-out")
    Emitter<String> emitter;
    @Inject
    EventSerializer eventSerializer;
    @Override
    public void publish(ResponseEvent event) {
        if (!emitter.hasRequests()) {
            throw new IllegalStateException("Error al publicar en el AMQP");
        }
        try {
            serializeEvent(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void serializeEvent(ResponseEvent event) throws JsonProcessingException {
        var eventSerialize = eventSerializer.serialize(event);
        emitterEvent(eventSerialize);
    }

    private void emitterEvent(String event) {
        emitter.send(event).whenComplete((success, failure) -> {
            if (failure != null) {
                throw new IllegalStateException("Error al publicar en el AMQP " + failure.getMessage());
            }
        });
    }

}
