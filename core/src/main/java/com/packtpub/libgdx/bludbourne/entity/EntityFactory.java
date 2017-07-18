package com.packtpub.libgdx.bludbourne.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.packtpub.libgdx.bludbourne.handlers.NPCBehaviorHandler;
import com.packtpub.libgdx.bludbourne.handlers.NPCGraphicsHandler;
import com.packtpub.libgdx.bludbourne.handlers.NPCPhysicsHandler;
import com.packtpub.libgdx.bludbourne.handlers.PlayerGraphicsHandler;
import com.packtpub.libgdx.bludbourne.handlers.PlayerInputHandler;
import com.packtpub.libgdx.bludbourne.handlers.PlayerPhysicsHandler;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;

import java.util.ArrayList;

public class EntityFactory {
    public enum EntityType {
        PLAYER,
        DEMO_PLAYER,
        NPC
    }

    private static final String PLAYER_CONFIG = "scripts/player.JSON";

    public static Entity getEntity(EntityType entityType) {
        Entity entity = null;
        switch (entityType) {
            case PLAYER:
                entity = new Entity(PlayerInputHandler.instance(), PlayerGraphicsHandler.instance(), PlayerPhysicsHandler.instance());
                entity.setEntityConfig(getEntityConfig(EntityFactory.PLAYER_CONFIG));
                entity.sendMessage(Message.LOAD_ANIMATIONS, entity.getEntityConfig());
                return entity;
            case DEMO_PLAYER:
                entity = new Entity(NPCBehaviorHandler.instance(), PlayerGraphicsHandler.instance(), PlayerPhysicsHandler.instance());
                return entity;
            case NPC:
                entity = new Entity(NPCBehaviorHandler.instance(), NPCGraphicsHandler.instance(), NPCPhysicsHandler.instance());
                return entity;
            default:
                return null;
        }
    }

    public static Entity getEntity(EntityConfig entityConfig, EntityType entityType) {
        Entity entity = null;
        switch (entityType) {
            case PLAYER:
                entity = new Entity(entityConfig, PlayerInputHandler.instance(), PlayerGraphicsHandler.instance(), PlayerPhysicsHandler.instance());
                entity.sendMessage(Message.LOAD_ANIMATIONS, entity.getEntityConfig());
                break;
            case DEMO_PLAYER:
                entity = new Entity(entityConfig, NPCBehaviorHandler.instance(), PlayerGraphicsHandler.instance(), PlayerPhysicsHandler.instance());
                break;
            case NPC:
                entity = new Entity(entityConfig, NPCBehaviorHandler.instance(), NPCGraphicsHandler.instance(), NPCPhysicsHandler.instance());
                break;
            default:
                break;
        }
        return entity;
    }

    public static EntityConfig getEntityConfig(String configFilePath) {
        return MapperProvider.INSTANCE.parse(EntityConfig.class, Gdx.files.internal(configFilePath));
    }

    public static Array<EntityConfig> getEntityConfigs(String configFilePath) {
        Array<EntityConfig> configs = new Array<>();

        ArrayList<JsonValue> list = MapperProvider.INSTANCE.parse(ArrayList.class, Gdx.files.internal(configFilePath));

        for (JsonValue jsonVal : list) {
            configs.add(MapperProvider.INSTANCE.mapper().readValue(EntityConfig.class, jsonVal));
        }

        return configs;
    }
}
