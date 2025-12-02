package net.dimmid.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dimmid.entity.Location;
import net.dimmid.entity.Player;
import net.dimmid.entity.PlayerDTO;
import net.dimmid.entity.PlayerEvent;

import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String playerEventToJson(PlayerEvent playerEvent) throws JsonProcessingException {
        MAPPER.addMixIn(PlayerEvent.class, ClientEventMessageMixin.class);
        return MAPPER.writeValueAsString(playerEvent);
    }

    public static PlayerEvent jsonToPlayerEvent(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, PlayerEvent.class);
    }

    public static Location jsonToLocation(String data) throws JsonProcessingException {
        return MAPPER.readValue(data, Location.class);
    }

    public static String locationToJson(Location location) throws JsonProcessingException {
        return MAPPER.writeValueAsString(location);
    }

    public static Map<String, String> jsonToLocationData(String data) throws JsonProcessingException {
        return MAPPER.readValue(data, new TypeReference<>() {
                }
        );
    }

    public static String locationUpdateToJson(Map<String, String> location) throws JsonProcessingException {
        if (location == null) {
            return null;
        }
        return MAPPER.writeValueAsString(location);
    }

    public static PlayerDTO jsonToPlayer(String data) throws JsonProcessingException {
        return MAPPER.readValue(data, PlayerDTO.class);
    }

    public static String playerToJson(Player data) throws JsonProcessingException {
        return MAPPER.writeValueAsString(data);
    }
}