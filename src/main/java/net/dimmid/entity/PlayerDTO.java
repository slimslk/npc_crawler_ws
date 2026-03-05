package net.dimmid.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PlayerDTO(String id,
                        String name,
                        int health,
                        int energy,
                        int hungry,
                        List<Integer> position,
                        List<Integer> direction,
                        List<String> inventory,
                        @JsonProperty("location_id")
                        String locationId,
                        @JsonProperty("attack_modifier")
                        int attackModifier,
                        @JsonProperty("attack_damage")
                        int attackDamage,
                        int defence,
                        @JsonProperty("is_dead")
                        boolean isDead,
                        @JsonProperty("is_sleep")
                        boolean isSleep) {
}
