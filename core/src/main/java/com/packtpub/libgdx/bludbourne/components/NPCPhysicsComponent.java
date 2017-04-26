package com.packtpub.libgdx.bludbourne.components;

import com.packtpub.libgdx.bludbourne.components.base.PhysicsComponent;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCPhysicsComponent extends PhysicsComponent {
    private final static Logger LOGGER = LoggerFactory.getLogger(NPCPhysicsComponent.class);

    public NPCPhysicsComponent() {
        boundingBoxLocation = BoundingBoxLocation.CENTER;
        initBoundingBox(0.4f, 0.15f);
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, float delta) {
        updateBoundingBoxPosition(nextEntityPosition);

        if (state == State.IMMOBILE) return;

        if (!isCollisionWithMapLayer(entity, mapMgr) &&
            !isCollisionWithMapEntities(entity, mapMgr) &&
            state == State.WALKING) {
            setNextPositionToCurrent(entity);
        } else {
            updateBoundingBoxPosition(currentEntityPosition);
        }
        calculateNextPosition(delta);
    }

    @Override
    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr) {
        //Test against player
        if (isCollision(entity, mapMgr.getPlayer())) {
            return true;
        }

        if (super.isCollisionWithMapEntities(entity, mapMgr)) {
            return true;
        }

        return false;
    }

    @Override
    public void dispose() {
    }
}
