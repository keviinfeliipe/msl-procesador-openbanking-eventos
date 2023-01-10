package ec.com.dinersclub.dddmodules.infrastructure.model.event;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class EventUrn implements Serializable {
    private static final long serialVersionUID = -4423478750451308048L;
    private EventSubject subject;

    public EventUrn() {
    }

    public EventUrn(EventSubject subject) {
        this.subject = subject;
    }

    public EventSubject getSubject() {
        return subject;
    }

    public void setSubject(EventSubject subject) {
        this.subject = subject;
    }

}
