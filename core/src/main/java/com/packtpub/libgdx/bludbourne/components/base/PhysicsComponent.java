package com.packtpub.libgdx.bludbourne.components.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapManager;

public abstract class PhysicsComponent extends Component {
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    public abstract void update(Entity entity, MapManager mapMgr, float delta);

    protected State state;
    protected Vector2 nextEntityPosition;
    protected Vector2 currentEntityPosition;
    protected Direction currentDirection;
    protected Json json;
    protected Vector2 velocity;

    public Rectangle boundingBox;
    protected BoundingBoxLocation boundingBoxLocation;

    public enum BoundingBoxLocation {
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER,
    }

    protected PhysicsComponent() {
        //init common handlers
        handlers.put(MESSAGE.CURRENT_STATE, (Object... args) -> state = (State) args[0]);
        handlers.put(MESSAGE.CURRENT_DIRECTION, (Object... args) -> currentDirection = (Direction) args[0]);
        handlers.put(MESSAGE.INIT_START_POSITION, (Object... args) -> {
            currentEntityPosition = (Vector2) args[0];
            nextEntityPosition.set(currentEntityPosition.x, currentEntityPosition.y);
        });

        this.nextEntityPosition = new Vector2(0, 0);
        this.currentEntityPosition = new Vector2(0, 0);
        this.velocity = new Vector2(4f, 4f);
        this.boundingBox = new Rectangle();
        this.json = new Json();
        boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
    }

    protected boolean isCollisionWithMapEntities(Entity entity, MapManager mapMgr) {
        Array<Entity> entities = mapMgr.getCurrentMapEntities();
        boolean isCollisionWithMapEntities = false;

        for (Entity mapEntity : entities) {
            //Check for testing against self
            if (mapEntity.equals(entity)) {
                continue;
            }

            Rectangle targetRect = mapEntity.getCurrentBoundingBox();
            if (boundingBox.overlaps(targetRect)) {
                //Collision
                entity.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
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

        if (entitySource.getCurrentBoundingBox().overlaps(entityTarget.getCurrentBoundingBox())) {
            //Collision
            entitySource.sendMessage(MESSAGE.COLLISION_WITH_ENTITY);
            isCollisionWithMapEntities = true;
        }

        return isCollisionWithMapEntities;
    }

    protected boolean isCollisionWithMapLayer(Entity entity, MapManager mapMgr) {
        MapLayer mapCollisionLayer = mapMgr.getCollisionLayer();

        if (mapCollisionLayer == null) {
            return false;
        }

        Rectangle rectangle = null;

        for (MapObject object : mapCollisionLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rectangle)) {
                    //Collision
                    entity.sendMessage(MESSAGE.COLLISION_WITH_MAP);
                    return true;
                }
            }
        }

        return false;
    }

    protected void setNextPositionToCurrent(Entity entity) {
        this.currentEntityPosition.x = nextEntityPosition.x;
        this.currentEntityPosition.y = nextEntityPosition.y;

        //Gdx.app.debug(TAG, "SETTING Current Position " + entity.getEntityConfig().getEntityID() + ": (" + currentEntityPosition.x + "," + currentEntityPosition.y + ")");
        entity.sendMessage(MESSAGE.CURRENT_POSITION, currentEntityPosition);
    }

    protected void calculateNextPosition(float deltaTime) {
        if (currentDirection == null) return;

        Vector2 point = new Vector2(currentEntityPosition);
        velocity.scl(deltaTime);
        nextEntityPosition = currentDirection.adjustPosition(point, velocity);
        //velocity
        velocity.scl(1 / deltaTime);
    }

    protected void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced) {
        //Update the current bounding box
        float width;
        float height;

        float origWidth = Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        if (widthReductionAmount > 0 && widthReductionAmount < 1) {
            width = Entity.FRAME_WIDTH * widthReductionAmount;
        } else {
            width = Entity.FRAME_WIDTH;
        }

        if (heightReductionAmount > 0 && heightReductionAmount < 1) {
            height = Entity.FRAME_HEIGHT * heightReductionAmount;
        } else {
            height = Entity.FRAME_HEIGHT;
        }

        if (width == 0 || height == 0) {
            Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
        }

        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;

        if (Map.UNIT_SCALE > 0) {
            minX = nextEntityPosition.x / Map.UNIT_SCALE;
            minY = nextEntityPosition.y / Map.UNIT_SCALE;
        } else {
            minX = nextEntityPosition.x;
            minY = nextEntityPosition.y;
        }

        boundingBox.setWidth(width);
        boundingBox.setHeight(height);

        switch (boundingBoxLocation) {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + origWidth / 2, minY + origHeight / 4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + origWidth / 2, minY + origHeight / 2);
                break;
        }

        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }

    protected void updateBoundingBoxPosition(Vector2 position) {
        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;

        if (Map.UNIT_SCALE > 0) {
            minX = position.x / Map.UNIT_SCALE;
            minY = position.y / Map.UNIT_SCALE;
        } else {
            minX = position.x;
            minY = position.y;
        }

        switch (boundingBoxLocation) {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, boundingBox.getWidth(), boundingBox.getHeight());
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH / 2, minY + Entity.FRAME_HEIGHT / 4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH / 2, minY + Entity.FRAME_HEIGHT / 2);
                break;
        }

        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }
}
