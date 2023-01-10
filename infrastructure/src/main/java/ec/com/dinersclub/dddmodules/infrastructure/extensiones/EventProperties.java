package ec.com.dinersclub.dddmodules.infrastructure.extensiones;

import ec.com.dinersclub.excepciones.MicroservicioError;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class EventProperties {
    @ConfigProperty(name = "event.events")
    List<String> events;
    @ConfigProperty(name = "event.identifications")
    List<String> identifications;

    public String findIdentificationByEvent(String event){
        Map<String, String> eventIdentification = IntStream.range(0, events.size()).boxed().collect(Collectors.toMap(events::get, identifications::get));
        if(!eventIdentification.containsKey(event)){
            throw new MicroservicioError("0001","Evento no registrado.","Evento no registrado");
        }
        return eventIdentification.get(event);
    }
}
