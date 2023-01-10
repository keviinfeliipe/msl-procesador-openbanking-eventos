package ec.com.dinersclub.dddmodules.application.cqrs.queries;

import ec.com.dinersclub.dddmodules.domain.model.DinBodyRequestEvent;
import ec.com.dinersclub.dddmodules.domain.repository.ProcessEventRepository;
import ec.com.dinersclub.dto.RequestMs;
import ec.com.dinersclub.dto.ResponseMs;
import ec.com.dinersclub.excepciones.interfaces.AbstractMsExcepcion;
import ec.com.dinersclub.logs.anotaciones.DinLoggerException;
import ec.com.dinersclub.validador.violation.SimpleValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class EventCommandServiceImpl implements IEventCommandService {

	@Inject
	ProcessEventRepository processEventRepository;

	@Inject
	SimpleValidator simpleValidator;

	@Override
	@DinLoggerException
	public ResponseMs<Void> validationEvent(RequestMs<DinBodyRequestEvent> command)
			throws AbstractMsExcepcion, IllegalAccessException {

		simpleValidator.ejecutar(command);
		return processEventRepository.processEvent(command);

	}

}
