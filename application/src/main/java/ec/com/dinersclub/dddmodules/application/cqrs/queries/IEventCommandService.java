package ec.com.dinersclub.dddmodules.application.cqrs.queries;

import ec.com.dinersclub.dddmodules.domain.model.DinBodyRequestEvent;
import ec.com.dinersclub.dto.RequestMs;
import ec.com.dinersclub.dto.ResponseMs;
import ec.com.dinersclub.excepciones.interfaces.AbstractMsExcepcion;

/**
 * 
 * @author Oscar Morocho
 *
 */
public interface IEventCommandService {

	ResponseMs<Void> validationEvent(RequestMs<DinBodyRequestEvent> query)
			throws AbstractMsExcepcion, IllegalAccessException;

}
