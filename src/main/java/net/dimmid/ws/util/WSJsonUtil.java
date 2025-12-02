package net.dimmid.ws.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dimmid.ws.entity.User;

import java.util.Optional;

public class WSJsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Optional<User> jsonToUser(String json) {
        try {
            return Optional.of(mapper.readValue(json, User.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

}
