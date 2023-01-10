package ec.com.dinersclub.dddmodules.infrastructure.service.generic;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@RegisterForReflection
public class EventUrl implements Serializable {
    private static final long serialVersionUID = -4044519974624622742L;
    private final URL url;
    public EventUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getDomain(){
        return url.getHost()+"/";
    }
    public String getProtocol(){
        return url.getProtocol()+"://";
    }

}
