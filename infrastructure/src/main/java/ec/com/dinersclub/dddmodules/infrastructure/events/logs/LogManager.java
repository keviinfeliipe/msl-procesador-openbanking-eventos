/**
 * 
 */
package ec.com.dinersclub.dddmodules.infrastructure.events.logs;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import ec.com.dinersclub.logs.entidades.MensajeLog;
import ec.com.dinersclub.logs.interfaces.ILogManager;

/**
 * @author Diners
 *
 */
@Singleton
public class LogManager implements ILogManager {

	@Inject
	@Channel("audit-out")
	Emitter<MensajeLog> logsEmisor;

	@Override
	public void publicar(MensajeLog mensajeLog) {

		if (!logsEmisor.hasRequests()) {
			throw new IllegalStateException("Error al publicar en el AMQP");
		}

		logsEmisor.send(mensajeLog).whenComplete((success, failure) -> {
			if (failure != null) {
				throw new IllegalStateException("Error al publicar en el AMQP " + failure.getMessage());
			}
		});

	}

}
