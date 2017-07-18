package com.packtpub.libgdx.bludbourne.handlers.base;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PhysicsHandler extends EntityMessageHandler {


    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicsHandler.class);

    public abstract void update(Entity entity, float delta);

    protected Ray selectionRay;
    protected final float selectRayMaximumDistance = 32.0f;

    public PhysicsHandler() {

        //init common handlers
        handlers.put(
            Message.CURRENT_STATE,
            (entity, args) -> entity.getEntityState().setCurrentState((State) args[0])
        );
        handlers.put(
            Message.CURRENT_DIRECTION,
            (entity, args) -> entity.getEntityState().setCurrentDirection((Direction) args[0])
        );
        handlers.put(Message.INIT_START_POSITION, (entity, args) -> {
            entity.getEntityState().setCurrentPosition((Vector2) args[0]);
            entity.getEntityState().setNextPosition((Vector2) args[0]);
        });

        selectionRay = new Ray(new Vector3(), new Vector3());
    }


    protected boolean isCollisionWithMapEntities(Entity entity) {
        Array<Entity> entities = MapManager.INSTANCE.getCurrentMapEntities();
        boolean isCollisionWithMapEntities = false;

        for (Entity mapEntity : entities) {
            //Check for testing against self
            if (mapEntity.equals(entity)) {
                continue;
            }

            Rectangle targetRect = mapEntity.getEntityState().getBoundingBox();
            if (entity.getEntityState().getBoundingBox().overlaps(targetRect)) {
                //Collision
                entity.sendMessage(Message.COLLISION_WITH_ENTITY);
                isCollisionWithMapEntities = true;
                break;
            }
        }
        return isCollisionWithMapEntities;
    }

    protected boolean isCollision(Entity entitySource, Entity entityTarget) {
        boolean isCollisionWithMapEntities = false;

        if (entitySource.equals(entityTarget)) {
            return false;
        }

        if (entitySource.getEntityState().getBoundingBox().overlaps(entityTarget.getEntityState().getBoundingBox())) {
            //Collision
            entitySource.sendMessage(Message.COLLISION_WITH_ENTITY);
            isCollisionWithMapEntities = true;
        }

        return isCollisionWithMapEntities;
    }

    protected boolean isCollisionWithMapLayer(Entity entity) {
        MapLayer mapCollisionLayer = MapManager.INSTANCE.getCollisionLayer();

        if (mapCollisionLayer == null) {
            return false;
        }

        Rectangle rectangle = null;

        for (MapObject object : mapCollisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();
                if (entity.getEntityState().getBoundingBox().overlaps(rectangle)) {
                    //Collision
                    entity.sendMessage(Message.COLLISION_WITH_MAP);
                    return true;
                }
            }
        }

        return false;
    }

    //TODO what?
    protected void setNextPositionToCurrent(Entity entity) {
        entity.getEntityState().setCurrentPosition(entity.getEntityState().getNextPosition());
        LOGGER.trace("SETTING Current Position {}: ({},{})", entity.getEntityConfig().getEntityID(), entity.getEntityState().getCurrentPosition());
        entity.sendMessage(Message.CURRENT_POSITION, entity.getEntityState().getCurrentPosition());
    }


}
