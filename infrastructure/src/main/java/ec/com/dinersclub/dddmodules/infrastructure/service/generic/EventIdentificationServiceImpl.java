package ec.com.dinersclub.dddmodules.infrastructure.service.generic;

import ec.com.dinersclub.dddmodules.infrastructure.extensiones.EventProperties;
import ec.com.dinersclub.dddmodules.infrastructure.providers.JsonMapper;
import ec.com.dinersclub.excepciones.MicroservicioError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class EventIdentificationServiceImpl implements EventIdentificationRepository {
    @Inject
    EventProperties eventProperties;
    @Inject
    JsonMapper jsonMapper;
    @Override
    public String findEventIdentification(String object, String event) {
        var map = jsonMapper.toMap(object);
        var eventIdentification = eventProperties.findIdentificationByEvent(event);
        var identification = findValueMap(map, eventIdentification);
        if(identification.isEmpty()){
            throw new MicroservicioError("0002","No se encontró el identificador del evento.","No se encontró el identificador del evento.");
        }
        return identification;
    }
    private String findValueMap(Map<String, Object> map, String findValue){
        var response = "";
        if(map.containsKey(findValue)){
            return map.get(findValue).toString();
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                response = findValueMap((Map<String, Object>) value, findValue);
            }
        }
        return response;
    }
}
