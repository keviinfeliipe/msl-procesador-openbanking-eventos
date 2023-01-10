package ec.com.dinersclub.dddmodules.infrastructure.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class Event implements Serializable {
    private static final long serialVersionUID = -838962780739671909L;
    @JsonProperty("urn:uk:org:openbanking:events:resource-update")
    private EventUrn urn;

    public Event() {
        new EventLk();
    }

    public Event(EventUrn urn) {
        this.urn = urn;
    }

    public EventUrn getUrn() {
        return urn;
    }
    public void setUrn(EventUrn urn) {
        this.urn = urn;
    }
}
