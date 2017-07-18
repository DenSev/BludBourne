package com.packtpub.libgdx.bludbourne.handlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityState;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.handlers.base.PhysicsHandler;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapFactory;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import com.packtpub.libgdx.bludbourne.observers.EntityObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerPhysicsHandler extends PhysicsHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerPhysicsHandler.class);
    private static final PlayerPhysicsHandler instance = new PlayerPhysicsHandler();

    public static PhysicsHandler instance() {
        return instance;
    }

    private Vector3 mouseSelectCoordinates;
    private boolean isMouseSelectEnabled = false;

    private PlayerPhysicsHandler() {
        handlers.put(Message.INIT_SELECT_ENTITY, (entity, args) -> {
            mouseSelectCoordinates = (Vector3) args[0];
            isMouseSelectEnabled = true;
        });
        handlers.put(Message.INIT_BOUNDING_BOX, (entity, args) -> {
            entity.getEntityState().setBoundingBoxLocation(EntityState.BoundingBoxLocation.BOTTOM_CENTER);
            entity.getEntityState().initBoundingBox(0.3f, 0.5f);
        });

        mouseSelectCoordinates = new Vector3(0, 0, 0);
    }

    public void dispose() {
    }

    @Override
    public void update(Entity entity, float delta) {
        //We want the hitbox to be at the feet for a better feel
        entity.getEntityState().updateBoundingBoxPosition();
        updatePortalLayerActivation(entity);

        if (isMouseSelectEnabled) {
            selectMapEntityCandidate(entity);
            isMouseSelectEnabled = false;
        }

        if (!isCollisionWithMapLayer(entity)
            && !isCollisionWithMapEntities(entity)
            && entity.getEntityState().getCurrentState().equals(State.WALKING)) {
            setNextPositionToCurrent(entity);

            Camera camera = MapManager.INSTANCE.getCamera();
            camera.position.set(entity.getEntityState().getCurrentPosition(), 0f);
            camera.update();
        } else {
            entity.getEntityState().updateBoundingBoxPosition();
        }

        entity.getEntityState().calculateNextPosition(delta);
    }

    private void selectMapEntityCandidate(Entity entity) {
        Array<Entity> currentEntities = MapManager.INSTANCE.getCurrentMapEntities();

        //Convert screen coordinates to world coordinates, then to unit scale coordinates
        MapManager.INSTANCE.getCamera().unproject(mouseSelectCoordinates);
        mouseSelectCoordinates.x /= Map.UNIT_SCALE;
        mouseSelectCoordinates.y /= Map.UNIT_SCALE;

        //Gdx.app.debug(TAG, "Mouse Coordinates " + "(" + mouseSelectCoordinates.x + "," + mouseSelectCoordinates.y + ")");

        for (Entity mapEntity : currentEntities) {
            //Don't break, reset all entities
            mapEntity.sendMessage(Message.ENTITY_DESELECTED, mapEntity);
            Rectangle mapEntityBoundingBox = mapEntity.getEntityState().getBoundingBox();
            //Gdx.app.debug(TAG, "Entity Candidate Location " + "(" + mapEntityBoundingBox.x + "," + mapEntityBoundingBox.y + ")");
            if (mapEntity.getEntityState().getBoundingBox().contains(mouseSelectCoordinates.x, mouseSelectCoordinates.y)) {
                //Check distance
                selectionRay.set(entity.getEntityState().getBoundingBox().x, entity.getEntityState().getBoundingBox().y, 0.0f, mapEntityBoundingBox.x, mapEntityBoundingBox.y, 0.0f);
                float distance = selectionRay.origin.dst(selectionRay.direction);

                if (distance <= selectRayMaximumDistance) {
                    //We have a valid entity selection
                    //Picked/Selected
                    LOGGER.trace("Selected Entity! {}", mapEntity.getEntityConfig().getEntityID());

                    //TODO selection
                    /*Entity transferEntity = entity;
                    transferEntity.registerComponents(
                        NPCBehaviorHandler.instance(),
                        NPCGraphicsHandler.instance(),
                        NPCPhysicsHandler.instance());
                    entity = mapEntity;
                    entity.registerComponents(
                        PlayerInputHandler.instance(),
                        PlayerGraphicsHandler.instance(),
                        PlayerPhysicsHandler.instance());

                    MapManager.INSTANCE.setPlayer(entity);
                    mapEntity = transferEntity;
                    entityIterator.remove();
                    currentEntities.add(mapEntity);*/
                    //player
                    mapEntity.sendMessage(Message.ENTITY_SELECTED, mapEntity);
                    mapEntity.notify(mapEntity.getEntityConfig(), EntityObserver.ComponentEvent.LOAD_CONVERSATION);
                }
            }
        }
    }

    private boolean updatePortalLayerActivation(Entity entity) {
        MapLayer mapPortalLayer = MapManager.INSTANCE.getPortalLayer();

        if (mapPortalLayer == null) {
            LOGGER.debug("Portal Layer doesn't exist!");
            return false;
        }

        Rectangle rectangle = null;

        for (MapObject object : mapPortalLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();

                if (entity.getEntityState().getBoundingBox().overlaps(rectangle)) {
                    String mapName = object.getName();
                    if (mapName == null) {
                        return false;
                    }

                    MapManager.INSTANCE.setClosestStartPositionFromScaledUnits(entity.getEntityState().getCurrentPosition());
                    MapManager.INSTANCE.loadMap(MapFactory.MapType.valueOf(mapName));

                    entity.getEntityState().setCurrentPosition(MapManager.INSTANCE.getPlayerStartUnitScaled());
                    entity.getEntityState().setNextPosition(MapManager.INSTANCE.getPlayerStartUnitScaled());

                    LOGGER.debug("Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }

}
