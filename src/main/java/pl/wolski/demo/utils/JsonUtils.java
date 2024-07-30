package pl.wolski.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String convertObjectToJSONString(Object object) {
        String objectAsString;

        try {
            objectAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Convert object to json String error", e);
        }

        return objectAsString;
    }

    public <T> T convertJSONStringToObject(String jsonString, Class<T> clazz) {
        T object;

        if (jsonString == null) {
            throw new IllegalArgumentException("json String cannot be null");
        }

        try {
            object = objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Convert json String to object error", e);
        }

        return object;
    }
}
