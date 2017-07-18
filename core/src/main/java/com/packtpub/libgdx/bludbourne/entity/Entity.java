package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.handlers.base.BehaviorHandler;
import com.packtpub.libgdx.bludbourne.handlers.base.GraphicsHandler;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.handlers.base.PhysicsHandler;
import com.packtpub.libgdx.bludbourne.observers.EntityObserver;
import com.packtpub.libgdx.bludbourne.observers.EntitySubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public class Entity extends EntitySubject {
    private static final Logger LOGGER = LoggerFactory.getLogger(Entity.class);

    public final static int FRAME_WIDTH = 16;
    public final static int FRAME_HEIGHT = 16;

    private EntityConfig entityConfig;
    private EntityState entityState = new EntityState();

    public boolean isSelected = false;


    private BehaviorHandler behaviorHandler;
    private GraphicsHandler graphicsHandler;
    private PhysicsHandler physicsHandler;

    public void init() {
        //sendMessage(Message.INIT_STATE, State.IDLE);
        //sendMessage(Message.INIT_DIRECTION, Direction.DOWN);
        //sendMessage(Message.INIT_START_POSITION, );
        sendMessage(Message.INIT_BOUNDING_BOX);
    }

    public Entity(EntityConfig entityConfig,
                  BehaviorHandler behaviorHandler,
                  GraphicsHandler graphicsHandler,
                  PhysicsHandler physicsHandler) {

        LOGGER.debug("Entity created with entityID {}", entityConfig.getEntityID());
        this.entityConfig = entityConfig;
        this.behaviorHandler = behaviorHandler;
        this.graphicsHandler = graphicsHandler;
        this.physicsHandler = physicsHandler;
        init();
    }

    public Entity(BehaviorHandler behaviorHandler,
                  GraphicsHandler graphicsHandler,
                  PhysicsHandler physicsHandler) {
        LOGGER.debug("Entity created");
        this.behaviorHandler = behaviorHandler;
        this.graphicsHandler = graphicsHandler;
        this.physicsHandler = physicsHandler;
        init();
    }

    public void sendMessage(Message message, Object... args) {
        LOGGER.trace("Message sent {}, with args {}", message, args);
        behaviorHandler.handle(this, message, args);
        graphicsHandler.handle(this, message, args);
        physicsHandler.handle(this, message, args);
    }

    public void update(Batch batch, float delta) {
        this.behaviorHandler.update(this, delta);
        this.physicsHandler.update(this, delta);
        this.graphicsHandler.update(this, batch, delta);

    }

    public void dispose() {
        this.behaviorHandler.dispose();
        this.physicsHandler.dispose();
        this.graphicsHandler.dispose();
    }

    public void registerComponents(BehaviorHandler behaviorHandler, GraphicsHandler graphicsHandler, PhysicsHandler physicsHandler) {
        LOGGER.debug("components registered for entityID {}", this.entityConfig.getEntityID());
        this.behaviorHandler = behaviorHandler;
        this.graphicsHandler = graphicsHandler;
        this.physicsHandler = physicsHandler;
    }

    public void registerObserver(EntityObserver observer) {
        this.addObserver(observer);
    }

    public void unregisterObservers() {
        this.removeAllObservers();
    }


    public void updateInput(float delta) {
        this.behaviorHandler.update(this, delta);
    }

    public BehaviorHandler getInputProcessor() {
        return this.behaviorHandler;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public EntityState getEntityState() {
        return entityState;
    }

    public void setEntityState(EntityState entityState) {
        this.entityState = entityState;
    }
}
