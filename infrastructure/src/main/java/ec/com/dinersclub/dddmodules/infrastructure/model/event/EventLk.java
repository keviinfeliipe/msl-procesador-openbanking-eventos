package ec.com.dinersclub.dddmodules.infrastructure.model.event;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class EventLk implements Serializable {
    private static final long serialVersionUID = -6192954925806403772L;
    private String version;
    private String link;

    public static EventLk aEventLk() {
        return new EventLk();
    }

    public String getVersion() {
        return version;
    }
    public String getLink() {
        return link;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public EventLk withVersion(String version) {
        this.version = version;
        return this;
    }
    public EventLk withLink(String link) {
        this.link = link;
        return this;
    }
}
