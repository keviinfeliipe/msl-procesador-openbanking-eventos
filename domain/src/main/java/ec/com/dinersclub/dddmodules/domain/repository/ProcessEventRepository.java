package ec.com.dinersclub.dddmodules.domain.repository;

import ec.com.dinersclub.dddmodules.domain.model.DinBodyRequestEvent;
import ec.com.dinersclub.dto.RequestMs;
import ec.com.dinersclub.dto.ResponseMs;
import ec.com.dinersclub.excepciones.interfaces.AbstractMsExcepcion;

public interface ProcessEventRepository {
    ResponseMs<Void> processEvent(RequestMs<DinBodyRequestEvent> event) throws AbstractMsExcepcion, IllegalAccessException;
}
