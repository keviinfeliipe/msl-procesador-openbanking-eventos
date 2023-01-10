package ec.com.dinersclub.dddmodules.infrastructure.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;
import java.util.List;

@RegisterForReflection
public class EventSubject implements Serializable {
    private static final long serialVersionUID = -5540059590469699788L;
    @JsonProperty("subject_type")
    private String subjectType;
    @JsonProperty("http://openbanking.org.uk/rid")
    private String rid;
    @JsonProperty("http://openbanking.org.uk/rty")
    private String rty;
    @JsonProperty("http://openbanking.org.uk/rlk")
    private List<EventLk> rlk;

    public static EventSubject aEventSubject() {
        return new EventSubject();
    }

    public String getSubject_type() {
        return subjectType;
    }

    public String getRid() {
        return rid;
    }

    public String getRty() {
        return rty;
    }

    public List<EventLk> getRlk() {
        return rlk;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setRty(String rty) {
        this.rty = rty;
    }

    public void setRlk(List<EventLk> rlk) {
        this.rlk = rlk;
    }

    public EventSubject withSubjectType(String subject_type) {
        this.subjectType = subject_type;
        return this;
    }
    public EventSubject withRid(String rid) {
        this.rid = rid;
        return this;
    }
    public EventSubject withRty(String rty) {
        this.rty = rty;
        return this;
    }
    public EventSubject withRlk(List<EventLk> rlk) {
        this.rlk = rlk;
        return this;
    }
}
