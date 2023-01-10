package ec.com.dinersclub.dddmodules.infrastructure.providers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JsonMapper {
    public Map<String, Object> toMap(String object){
        return new Gson().fromJson(object, new TypeToken<HashMap<String, Object>>() {}.getType());
    }
}
