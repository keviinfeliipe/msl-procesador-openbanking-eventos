package ec.com.dinersclub.dddmodules.infrastructure.service.eventBus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.ResponseEvent;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventSerializer {
    public String serialize(ResponseEvent event) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(event);
    }
}
