package pl.wolski.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String convertObjectToJsonString(Object object) {
        String objectAsString;

        try {
            objectAsString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Convert object to json String error", e);
        }

        return objectAsString;
    }

    public <T> T convertJsonStringToObject(String jsonString, Class<T> clazz) {
        T object;

        if (jsonString == null) {
            throw new IllegalArgumentException("Json String cannot be null");
        }

        try {
            object = objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Convert json String to object error", e);
        }

        return object;
    }

    public <T> T convertJsonNodeToObject(JsonNode jsonNode, Class<T> clazz) {
        T object;

        if (jsonNode == null) {
            throw new IllegalArgumentException("JsonNode cannot be null");
        }

        try {
            object = objectMapper.treeToValue(jsonNode, clazz);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Convert JsonNode to object error", e);
        }

        return object;
    }
}
