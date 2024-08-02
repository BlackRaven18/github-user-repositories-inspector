package pl.wolski.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pl.wolski.demo.model.RepositoryDetails;
import pl.wolski.demo.model.RepositoryOwner;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {
    private final JsonUtils jsonUtils = new JsonUtils();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void convertObjectToJSONString_Success() {
        RepositoryDetails repositoryDetails = new RepositoryDetails();
        repositoryDetails.setName("test-repo");
        repositoryDetails.setOwner(new RepositoryOwner("test-owner-login"));

        String jsonString = jsonUtils.convertObjectToJsonString(repositoryDetails);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"name\" : \"test-repo\""));
        assertTrue(jsonString.contains("\"login\" : \"test-owner-login\""));
        assertTrue(jsonString.contains("\"branches\" : null"));
    }

    @Test
    void convertJSONStringToObject_Success() {
        String jsonString = """
                {
                  "name" : "test-repo",
                  "owner" : {
                    "login" : "test-owner-login"
                  },
                  "branches" : null
                }""";
        RepositoryDetails repositoryDetails = jsonUtils.convertJsonStringToObject(jsonString, RepositoryDetails.class);

        assertNotNull(repositoryDetails);
        assertEquals("test-repo", repositoryDetails.getName());
        assertEquals("test-owner-login", repositoryDetails.getOwner().getLogin());
        assertNull(repositoryDetails.getBranches());
    }

    @Test
    void testConvertJSONStringToObject_NullJsonString() {
        String jsonString = null;

        assertThrows(IllegalArgumentException.class,
                () -> jsonUtils.convertJsonStringToObject(jsonString, RepositoryDetails.class)
        );
    }

    @Test
    void testConvertJsonNodeToObject_Success() throws Exception {
        String jsonString = """
                {
                  "name" : "test-repo",
                  "owner" : {
                    "login" : "test-owner-login"
                  },
                  "branches" : null
                }""";
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        Class<RepositoryDetails> clazz = RepositoryDetails.class;

        RepositoryDetails repositoryDetails = jsonUtils.convertJsonNodeToObject(jsonNode, clazz);

        assertNotNull(repositoryDetails);
        assertEquals("test-repo", repositoryDetails.getName());
        assertEquals("test-owner-login", repositoryDetails.getOwner().getLogin());
        assertNull(repositoryDetails.getBranches());
    }

    @Test
    void testConvertJsonNodeToObject_NullJsonNode() {
        JsonNode jsonNode = null;
        Class<RepositoryDetails> clazz = RepositoryDetails.class;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> jsonUtils.convertJsonNodeToObject(jsonNode, clazz)
        );
        assertEquals("JsonNode cannot be null", exception.getMessage());
    }

    @Test
    void testConvertJsonNodeToObject_InvalidJsonNode() throws Exception {
        String invalidJsonString = "";
        JsonNode jsonNode = objectMapper.readTree(invalidJsonString);
        Class<RepositoryDetails> clazz = RepositoryDetails.class;

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> jsonUtils.convertJsonNodeToObject(jsonNode, clazz)
        );

        assertTrue(exception.getMessage().contains("Convert JsonNode to object error"));
    }
}