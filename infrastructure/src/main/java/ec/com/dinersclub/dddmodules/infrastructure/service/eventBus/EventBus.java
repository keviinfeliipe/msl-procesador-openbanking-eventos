package ec.com.dinersclub.dddmodules.infrastructure.service.eventBus;

import ec.com.dinersclub.dddmodules.infrastructure.model.event.ResponseEvent;

public interface EventBus {
    void publish(ResponseEvent event);
}
