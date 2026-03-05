package net.dimmid.util;

import net.dimmid.entity.Player;
import net.dimmid.entity.PlayerDTO;

public class PlayerMapper {

    public static void updatePlayerData(Player player, PlayerDTO playerDTO) {
        player.setId(playerDTO.id());
        player.setName(playerDTO.name());
        player.setHealth(playerDTO.health());
        player.setPosition(playerDTO.position());
        player.setDead(player.isDead());
        player.setDirection(playerDTO.direction());
        player.setEnergy(playerDTO.energy());
        player.setHungry(playerDTO.hungry());
        player.setInventory(playerDTO.inventory());
        player.setLocationId(playerDTO.locationId());
        player.setAttackModifier(playerDTO.attackModifier());
        player.setAttackDamage(playerDTO.attackDamage());
        player.setDefence(playerDTO.defence());
        player.setSleep(playerDTO.isSleep());
    }
}
