package net.dimmid.ws.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dimmid.ws.entity.User;

import java.util.Map;
import java.util.Optional;

public class WSJsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Optional<User> jsonToUser(String json) {
        try {
            return Optional.of(MAPPER.readValue(json, User.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static Map<String, Object> fromJson(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    public static Optional<String> toJsonObj(Map<String, Object> data) {
        try {
            return Optional.of(MAPPER.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> toJson(Map<String, String> data) {
        try {
            return Optional.of(MAPPER.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    public static String buildLogoutMessage(User user) {
        ObjectNode jsonNodes = new ObjectNode(JsonNodeFactory.instance);
        jsonNodes.put("user_id", user.userId());
        jsonNodes.put("action", "logout");
        return jsonNodes.toString();
    }
}
