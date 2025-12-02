package net.dimmid.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlayerEvent(@JsonProperty("user_id") String userId, String action, List<String> params) {}
