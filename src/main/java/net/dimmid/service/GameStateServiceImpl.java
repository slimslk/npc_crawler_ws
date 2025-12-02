package net.dimmid.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.dimmid.MainAppContext;
import net.dimmid.entity.Location;
import net.dimmid.entity.Player;
import net.dimmid.entity.PlayerDTO;
import net.dimmid.entity.PlayerEvent;
import net.dimmid.util.JsonUtil;
import net.dimmid.util.PlayerMapper;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class GameStateServiceImpl implements IGameStateService {

    private final ConcurrentHashMap<String, Location> gameLocations = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Player> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, String>> locationUpdates = new ConcurrentHashMap<>();
    private static final String USER_JSON_MESSAGE = "{\"player_stats\":%s,\"location_updates\":%s,\"game_updates\":%s}";
    private static final Set<String> requestedMap = new HashSet<>();

    public Map<String, String> getUserMessages() throws JsonProcessingException {
        Map<String, String> userData = prepareDataToSend();
        if (!userData.isEmpty()) {
            locationUpdates.clear();
        }
        return userData;
    }

    @Override
    public void addUser(String userId) {
        users.put(userId, new Player());
    }

    @Override
    public void removeUser(String userId) {
        users.remove(userId);
    }

    public void updatePlayersData(String username, String playerData) {
        try {
            Player player = users.get(username);
            if (player == null) {
                return;
            }
            PlayerDTO playerDTO = JsonUtil.jsonToPlayer(playerData);
            if (!playerDTO.locationId().equals(player.getLocationId())) {
                player.setHasMap(false);
            }
            PlayerMapper.updatePlayerData(player, playerDTO);
            users.put(username, player);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void updateLocation(String location_id, String data) {
        try {
            Location location = gameLocations.get(location_id);
            Map<String, String> updates = processLocationData(data);
            if (location != null) {
                location.map().putAll(updates);
            }
            if (!locationUpdates.containsKey(location_id)) {
                locationUpdates.put(location_id, updates);
            } else {
                Map<String, String> locUpdates = locationUpdates.get(location_id);
                locUpdates.putAll(updates);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //This method adds only the full game map to the cache, while other game updates are currently ignored.
    public void updateGameData(String locationId, String data) {
        try {
            Location location = JsonUtil.jsonToLocation(data);
            gameLocations.put(locationId, location);
            requestedMap.remove(locationId);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> processLocationData(String data) throws JsonProcessingException {
        return JsonUtil.jsonToLocationData(data);
    }

    private Map<String, String> prepareDataToSend() throws JsonProcessingException {

        Map<String, String> userMessages = new HashMap<>();
        Location location;
        String result;
        Player player;
        String playerData;
        String locationData = null;
        String gameUpdates = null;
        for (String userId : users.keySet()) {
            player = users.get(userId);
            playerData = JsonUtil.playerToJson(player);
            String locationId = player.getLocationId();
            if (locationId != null && !locationId.isBlank()) {
                if (!player.isHasMap()) {
                    location = gameLocations.get(locationId);
                    if (location == null) {
                        try {
                            if (requestedMap.contains(locationId)) {
                                continue;
                            }
                            requestedMap.add(locationId);
                            BlockingQueue<PlayerEvent> eventInputQueue =
                                    (BlockingQueue<PlayerEvent>) MainAppContext.getObject("eventInputQueue").orElseThrow();
                            PlayerEvent event = new PlayerEvent(userId, "get_full_map", List.of(locationId));
                            eventInputQueue.put(event);
                            continue;
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    gameUpdates = JsonUtil.locationToJson(location);
                    player.setHasMap(true);
                }
                locationData = JsonUtil.locationUpdateToJson(locationUpdates.get(locationId));
            }
            result = combinePlayerStatsAndLocationUpdates(playerData, locationData, gameUpdates);
            userMessages.put(userId, result);
        }
        return userMessages;
    }

    private String combinePlayerStatsAndLocationUpdates(String playerData, String locationData, String gameUpdates) {
        return String.format(USER_JSON_MESSAGE,
                playerData,
                locationData,
                gameUpdates
        );
    }
}
