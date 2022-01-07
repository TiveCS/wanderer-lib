package io.github.tivecs.wanderer.language;

import java.util.LinkedHashMap;
import java.util.Map;

public class Placeholder {

    private final LinkedHashMap<String, String> placeholders = new LinkedHashMap<>();

    public Placeholder(){}

    public void set(String placeholder, String value){
        getPlaceholders().put(placeholder, value);
    }

    public void delete(String placeholder){
        getPlaceholders().remove(placeholder);
    }

    public String useLinear(String text){
        for (Map.Entry<String, String> entry: getPlaceholders().entrySet()){
            StringBuilder path = new StringBuilder().append("%").append(entry.getKey()).append("%");
            text = text.replace(path, entry.getValue());
        }
        return text;
    }

    public LinkedHashMap<String, String> getPlaceholders() {
        return placeholders;
    }
}
