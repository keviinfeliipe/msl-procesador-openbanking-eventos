package ec.com.dinersclub.dddmodules.domain.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class DinBodyRequestEvent implements Serializable {
    private static final long serialVersionUID = 5132681053958513280L;
    private String url;
    private String clientId;
    private String evento;
    private String metodo;
    private String response;
    private String version;

    public String getUrl() {
        return url;
    }

    public String getClientId() {
        return clientId;
    }

    public String getEvento() {
        return evento;
    }

    public String getMetodo() {
        return metodo;
    }

    public String getResponse() {
        return response;
    }

    public String getVersion() {
        return version;
    }
}
