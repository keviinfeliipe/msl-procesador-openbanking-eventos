package ec.com.dinersclub.dddmodules.infrastructure.model.event;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class ResponseEvent implements Serializable {
    private static final long serialVersionUID = 8817488298628190064L;
    private String iss;
    private String iat;
    private String sub;
    private Event events;
    private String toe;
    private String clientId;

    public static ResponseEvent aResponseEvent() {
        return new ResponseEvent();
    }

    public String getIss() {
        return iss;
    }

    public String getIat() {
        return iat;
    }
    public String getSub() {
        return sub;
    }
    public Event getEvents() {
        return events;
    }
    public String getToe() {
        return toe;
    }

    public String getClientId() {
        return clientId;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }
    public void setSub(String sub) {
        this.sub = sub;
    }
    public void setEvents(Event events) {
        this.events = events;
    }
    public void setToe(String toe) {
        this.toe = toe;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ResponseEvent withIss(String iss) {
        this.iss = iss;
        return this;
    }

    public ResponseEvent withIat(String iat) {
        this.iat = iat;
        return this;
    }

    public ResponseEvent withSub(String sub) {
        this.sub = sub;
        return this;
    }
    public ResponseEvent withEvents(Event events) {
        this.events = events;
        return this;
    }
    public ResponseEvent withToe(String toe) {
        this.toe = toe;
        return this;
    }
    public ResponseEvent withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }
}
