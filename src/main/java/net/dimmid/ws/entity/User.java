package net.dimmid.ws.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User(@JsonProperty("user_id") String userId) {
}
