package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.inventory.ItemTypeID;


public class EntityConfig {
    private Array<AnimationConfig> animationConfig;
    private State state = State.IDLE;
    private Direction direction = Direction.DOWN;
    private String entityID;
    Array<ItemTypeID> inventory;
    String conversationConfigPath;

    EntityConfig() {
        animationConfig = new Array<AnimationConfig>();
        inventory = new Array<ItemTypeID>();

    }

    public String getConversationConfigPath() {
        return conversationConfigPath;
    }

    public void setConversationConfigPath(String conversationConfigPath) {
        this.conversationConfigPath = conversationConfigPath;
    }

    public Array<ItemTypeID> getInventory() {
        return inventory;
    }

    public void setInventory(Array<ItemTypeID> inventory) {
        this.inventory = inventory;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Array<AnimationConfig> getAnimationConfig() {
        return animationConfig;
    }

    public void addAnimationConfig(AnimationConfig animationConfig) {
        this.animationConfig.add(animationConfig);
    }


}
