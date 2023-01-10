package ec.com.dinersclub.dddmodules.infrastructure.service.generic;

public interface EventIdentificationRepository {

    String findEventIdentification(String object, String event);
}
