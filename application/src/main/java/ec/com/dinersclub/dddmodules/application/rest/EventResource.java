package ec.com.dinersclub.dddmodules.application.rest;

import ec.com.dinersclub.dddmodules.application.cqrs.queries.IEventCommandService;
import ec.com.dinersclub.dddmodules.domain.model.DinBodyRequestEvent;
import ec.com.dinersclub.dto.RequestMs;
import ec.com.dinersclub.dto.ResponseMs;
import ec.com.dinersclub.excepciones.ExcepcionMs;
import ec.com.dinersclub.excepciones.interfaces.AbstractMsExcepcion;
import ec.com.dinersclub.logs.anotaciones.DinLogger;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.extension.annotations.WithSpan;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/procesador/v1/openbanking/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

	@Inject
	IEventCommandService iEventCommandService;

	@WithSpan(kind = SpanKind.SERVER)
	@POST
	@PermitAll
	@Path("")
	@DinLogger
	public Response event(RequestMs<DinBodyRequestEvent> command) {

		ResponseMs<Void> response;
		Response respuesta;

		try {
			respuesta = Response.ok().entity(iEventCommandService.validationEvent(command)).build();
		} catch (AbstractMsExcepcion ex) {
			response = new ResponseMs<>(command.getDinHeader(), ex.getDinError());
			respuesta = Response.status(ex.getCodigoHttp()).entity(response).build();

		} catch (Exception ex) {
			response = new ResponseMs<>(command.getDinHeader(), ExcepcionMs.procesar(ex));
			respuesta = Response.serverError().entity(response).build();
		}

		return respuesta;
	}

}