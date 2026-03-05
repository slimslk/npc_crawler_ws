package net.dimmid.entity;

import java.util.List;

public class Player {
    private String id;
    private String name;
    private int health;
    private int energy;
    private int hungry;
    private List<Integer> position;
    private List<Integer> direction;
    private List<String> inventory;
    private String locationId;
    private int attackModifier;
    private int attackDamage;
    private int defence;
    private boolean isDead;
    private boolean isHasMap;
    private boolean isSleep;

    public Player(String id,
                  String name,
                  int health,
                  int energy,
                  int hungry,
                  List<Integer> position,
                  List<Integer> direction,
                  List<String> inventory,
                  String locationId,
                  int attackModifier,
                  int attackDamage,
                  int defence,
                  boolean isDead,
                  boolean isSleep,
                  boolean isHasMap) {
        this.id = id;
        this.name = name;
        this.health = health;
        this.energy = energy;
        this.hungry = hungry;
        this.position = position;
        this.direction = direction;
        this.inventory = inventory;
        this.locationId = locationId;
        this.isDead = isDead;
        this.attackModifier = attackModifier;
        this.attackDamage = attackDamage;
        this.defence = defence;
        this.isHasMap = isHasMap;
        this.isSleep = isSleep;
    }

    public Player() {
        this.locationId = "";
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public int getDefence() {
        return defence;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHungry() {
        return hungry;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public List<Integer> getDirection() {
        return direction;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public String getLocationId() {
        return locationId;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isSleep() {
        return isSleep;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setHungry(int hungry) {
        this.hungry = hungry;
    }

    public void setPosition(List<Integer> position) {
        this.position = position;
    }

    public void setDirection(List<Integer> direction) {
        this.direction = direction;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public boolean isHasMap() {
        return isHasMap;
    }

    public void setHasMap(boolean hasMap) {
        isHasMap = hasMap;
    }

    public void setSleep(boolean sleep) {
        this.isSleep = sleep;
    }
}
