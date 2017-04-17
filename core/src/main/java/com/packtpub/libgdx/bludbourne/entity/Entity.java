package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.components.base.BehaviorComponent;
import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.components.base.GraphicsComponent;
import com.packtpub.libgdx.bludbourne.components.base.PhysicsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created on 04/10/2017.
 */
public class Entity {
    private static final Logger LOGGER = LoggerFactory.getLogger(Entity.class);

    public final static int FRAME_WIDTH = 16;
    public final static int FRAME_HEIGHT = 16;
    private EntityConfig entityConfig;

    public boolean isSelected = false;
    private BehaviorComponent behaviorComponent;
    private GraphicsComponent graphicsComponent;
    private PhysicsComponent physicsComponent;

    public Entity(EntityConfig entityConfig, BehaviorComponent behaviorComponent, GraphicsComponent graphicsComponent, PhysicsComponent physicsComponent) {
        LOGGER.debug("Entity created with entityID {}", entityConfig.getEntityID());
        this.entityConfig = entityConfig;
        this.behaviorComponent = behaviorComponent;
        this.graphicsComponent = graphicsComponent;
        this.physicsComponent = physicsComponent;
    }

    public Entity(BehaviorComponent behaviorComponent, GraphicsComponent graphicsComponent, PhysicsComponent physicsComponent) {
        LOGGER.debug("Entity created");
        this.behaviorComponent = behaviorComponent;
        this.graphicsComponent = graphicsComponent;
        this.physicsComponent = physicsComponent;
    }

    public void sendMessage(Component.MESSAGE message, Object... args) {
        LOGGER.trace("Message sent {}, with args {}", message, args);
        behaviorComponent.receiveMessage(message, args);
        graphicsComponent.receiveMessage(message, args);
        physicsComponent.receiveMessage(message, args);
    }

    public void update(MapManager mapMgr, Batch batch, float delta) {
        this.behaviorComponent.update(this, delta);
        this.physicsComponent.update(this, mapMgr, delta);
        this.graphicsComponent.update(this, mapMgr, batch, delta);

    }

    public void dispose() {
        this.behaviorComponent.dispose();
        this.physicsComponent.dispose();
        this.graphicsComponent.dispose();
    }

    public void registerComponents(BehaviorComponent behaviorComponent, GraphicsComponent graphicsComponent, PhysicsComponent physicsComponent) {
        LOGGER.debug("components registered for entityID {}", this.entityConfig.getEntityID());
        this.behaviorComponent = behaviorComponent;
        this.graphicsComponent = graphicsComponent;
        this.physicsComponent = physicsComponent;
    }

    public Rectangle getCurrentBoundingBox() {
        return physicsComponent.boundingBox;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }
}
