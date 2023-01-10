package ec.com.dinersclub.dddmodules.infrastructure.service;

import ec.com.dinersclub.dddmodules.domain.model.DinBodyRequestEvent;
import ec.com.dinersclub.dddmodules.domain.repository.ProcessEventRepository;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.Event;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.EventLk;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.EventSubject;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.EventUrn;
import ec.com.dinersclub.dddmodules.infrastructure.model.event.ResponseEvent;
import ec.com.dinersclub.dddmodules.infrastructure.service.eventBus.EventBus;
import ec.com.dinersclub.dddmodules.infrastructure.service.generic.EventIdentificationRepository;
import ec.com.dinersclub.dddmodules.infrastructure.service.generic.EventUrl;
import ec.com.dinersclub.dto.RequestMs;
import ec.com.dinersclub.dto.ResponseMs;
import ec.com.dinersclub.excepciones.interfaces.AbstractMsExcepcion;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.function.Supplier;

@ApplicationScoped
public class ProcessEventServiceImpl implements ProcessEventRepository {
    private static final Logger LOGGER = Logger.getLogger(ProcessEventServiceImpl.class);
    @Inject
    EventBus eventBus;
    @Inject
    EventIdentificationRepository eventIdentificationRepository;
    public static final String SUBJECT_TYPE = "http://openbanking.org.uk/rid_http://openbanking.org.uk/rty";
    Supplier<String> getTime = ()->{
        long time = new Timestamp(System.currentTimeMillis()).getTime();
        return String.valueOf(time);
    };
    @Override
    public ResponseMs<Void> processEvent(RequestMs<DinBodyRequestEvent> eventRequest) throws AbstractMsExcepcion, IllegalAccessException {
        var body = eventRequest.getDinBody();
        var event = body.getEvento();
        var id  = eventIdentificationRepository.findEventIdentification(body.getResponse(), event);
        var url = new EventUrl(body.getUrl());
        var version = body.getVersion();
        var eventLk = EventLk.aEventLk()
                .withLink(url.getProtocol()+url.getDomain()+id)
                .withVersion(version);
        var eventSubject = EventSubject.aEventSubject()
                .withSubjectType(SUBJECT_TYPE)
                .withRlk(Collections.singletonList(eventLk))
                .withRty(event)
                .withRid(id);
        var events = new Event();
        events.setUrn(new EventUrn());
        events.getUrn().setSubject(eventSubject);
        var clientId=body.getClientId();
        var time = getTime.get();
        var eventResponse = ResponseEvent.aResponseEvent()
                .withIss(url.getProtocol()+url.getDomain())
                .withIat(time)
                .withSub(url.getProtocol()+url.getDomain()+id)
                .withEvents(events)
                .withToe(time)
                .withClientId(clientId);
        publishEvent(eventResponse);
        return new ResponseMs<>(eventRequest.getDinHeader());
    }
    private void publishEvent(ResponseEvent eventResponse) {
        eventBus.publish(eventResponse);
        LOGGER.info("Message written to the queue");
    }

}
