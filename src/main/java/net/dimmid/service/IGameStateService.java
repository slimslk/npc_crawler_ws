package net.dimmid.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface IGameStateService {
    Map<String, String> getUserMessages() throws JsonProcessingException;
    void addUser(String userId);
    void removeUser(String userId);

    void updatePlayersData(String userId, String value);

    void updateLocation(String locationId, String value);

    void updateGameData(String userId, String value);
}
