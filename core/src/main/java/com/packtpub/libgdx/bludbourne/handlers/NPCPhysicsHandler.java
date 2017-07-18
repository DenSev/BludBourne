package com.packtpub.libgdx.bludbourne.handlers;

import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityState;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.handlers.base.PhysicsHandler;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCPhysicsHandler extends PhysicsHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(NPCPhysicsHandler.class);
    private static final NPCPhysicsHandler instance = new NPCPhysicsHandler();

    public static PhysicsHandler instance() {
        return instance;
    }

    private NPCPhysicsHandler() {
        handlers.put(Message.INIT_BOUNDING_BOX, (entity, args) -> {
            entity.getEntityState().setBoundingBoxLocation(EntityState.BoundingBoxLocation.CENTER);
            entity.getEntityState().initBoundingBox(0.4f, 0.15f);
        });
    }

    @Override
    public void update(Entity entity, float delta) {
        entity.getEntityState().updateBoundingBoxPosition();

        if (isEntityFarFromPlayer(entity)) {
            entity.sendMessage(Message.ENTITY_DESELECTED);
        }

        if (entity.getEntityState().getCurrentState().equals(State.IMMOBILE)) return;

        if (!isCollisionWithMapLayer(entity)
            && !isCollisionWithMapEntities(entity)
            && entity.getEntityState().getCurrentState().equals(State.WALKING)) {

            setNextPositionToCurrent(entity);
        } else {
            entity.getEntityState().updateBoundingBoxPosition();
        }
        entity.getEntityState().calculateNextPosition(delta);
    }

    private boolean isEntityFarFromPlayer(Entity entity) {
        //Check distance
        selectionRay.set(
            MapManager.INSTANCE.getPlayer().getEntityState().getBoundingBox().x,
            MapManager.INSTANCE.getPlayer().getEntityState().getBoundingBox().y,
            0.0f,
            entity.getEntityState().getBoundingBox().x,
            entity.getEntityState().getBoundingBox().y,
            0.0f
        );
        float distance = selectionRay.origin.dst(selectionRay.direction);

        if (distance <= selectRayMaximumDistance) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected boolean isCollisionWithMapEntities(Entity entity) {
        //Test against player
        return isCollision(entity, MapManager.INSTANCE.getPlayer()) || super.isCollisionWithMapEntities(entity);
    }

    public void dispose() {
    }
}
