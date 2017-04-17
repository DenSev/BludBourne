package com.packtpub.libgdx.bludbourne.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.packtpub.libgdx.bludbourne.components.*;
import com.packtpub.libgdx.bludbourne.components.base.Component;

import java.util.ArrayList;

public class EntityFactory {
    private static final Json JSON = new Json();
    public enum EntityType {
        PLAYER,
        DEMO_PLAYER,
        NPC
    }

    public static String PLAYER_CONFIG = "scripts/player.JSON";

    public static Entity getEntity(EntityType entityType) {
        Entity entity = null;
        switch (entityType) {
            case PLAYER:
                entity = new Entity(new PlayerInputComponent(), new PlayerGraphicsComponent(), new PlayerPhysicsComponent());
                entity.setEntityConfig(getEntityConfig(EntityFactory.PLAYER_CONFIG));
                entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, entity.getEntityConfig());
                return entity;
            case DEMO_PLAYER:
                entity = new Entity(new NPCBehaviorComponent(), new PlayerGraphicsComponent(), new PlayerPhysicsComponent());
                return entity;
            case NPC:
                entity = new Entity(new NPCBehaviorComponent(), new NPCGraphicsComponent(), new NPCPhysicsComponent());
                return entity;
            default:
                return null;
        }
    }

    public static Entity getEntity(EntityConfig entityConfig, EntityType entityType){
        Entity entity = null;
        switch (entityType) {
            case PLAYER:
                entity = new Entity(entityConfig, new PlayerInputComponent(), new PlayerGraphicsComponent(), new PlayerPhysicsComponent());
                entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, entity.getEntityConfig());
                break;
            case DEMO_PLAYER:
                entity = new Entity(entityConfig, new NPCBehaviorComponent(), new PlayerGraphicsComponent(), new PlayerPhysicsComponent());
                break;
            case NPC:
                entity = new Entity(entityConfig, new NPCBehaviorComponent(), new NPCGraphicsComponent(), new NPCPhysicsComponent());
                break;
            default:
                break;
        }
        return entity;
    }

    public static EntityConfig getEntityConfig(String configFilePath) {
        return JSON.fromJson(EntityConfig.class, Gdx.files.internal(configFilePath));
    }

    public static Array<EntityConfig> getEntityConfigs(String configFilePath) {
        Array<EntityConfig> configs = new Array<EntityConfig>();

        ArrayList<JsonValue> list = JSON.fromJson(ArrayList.class, Gdx.files.internal(configFilePath));

        for (JsonValue jsonVal : list) {
            configs.add(JSON.readValue(EntityConfig.class, jsonVal));
        }

        return configs;
    }
}
