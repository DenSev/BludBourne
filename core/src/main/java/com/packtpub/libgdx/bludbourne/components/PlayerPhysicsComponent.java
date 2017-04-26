package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.components.base.PhysicsComponent;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapFactory;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class PlayerPhysicsComponent extends PhysicsComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerPhysicsComponent.class);

    private Vector3 mouseSelectCoordinates;
    private boolean isMouseSelectEnabled = false;
    private Ray selectionRay;
    private float selectRayMaximumDistance = 32.0f;

    public PlayerPhysicsComponent() {
        handlers.put(MESSAGE.INIT_SELECT_ENTITY, (Object... args) -> {
            mouseSelectCoordinates = (Vector3) args[0];
            isMouseSelectEnabled = true;
        });

        boundingBoxLocation = BoundingBoxLocation.BOTTOM_CENTER;
        initBoundingBox(0.3f, 0.5f);
        mouseSelectCoordinates = new Vector3(0, 0, 0);
        selectionRay = new Ray(new Vector3(), new Vector3());
    }

    @Override
    public void dispose() {
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, float delta) {
        //We want the hitbox to be at the feet for a better feel
        updateBoundingBoxPosition(nextEntityPosition);
        updatePortalLayerActivation(mapMgr);

        if (isMouseSelectEnabled) {
            selectMapEntityCandidate(entity, mapMgr);
            isMouseSelectEnabled = false;
        }

        if (!isCollisionWithMapLayer(entity, mapMgr) &&
            !isCollisionWithMapEntities(entity, mapMgr) &&
            state == State.WALKING) {
            setNextPositionToCurrent(entity);

            Camera camera = mapMgr.getCamera();
            camera.position.set(currentEntityPosition.x, currentEntityPosition.y, 0f);
            camera.update();
        } else {
            updateBoundingBoxPosition(currentEntityPosition);
        }

        calculateNextPosition(delta);
    }

    private void selectMapEntityCandidate(Entity player, MapManager mapMgr) {
        Array<Entity> currentEntities = mapMgr.getCurrentMapEntities();

        //Convert screen coordinates to world coordinates, then to unit scale coordinates
        mapMgr.getCamera().unproject(mouseSelectCoordinates);
        mouseSelectCoordinates.x /= Map.UNIT_SCALE;
        mouseSelectCoordinates.y /= Map.UNIT_SCALE;

        //Gdx.app.debug(TAG, "Mouse Coordinates " + "(" + mouseSelectCoordinates.x + "," + mouseSelectCoordinates.y + ")");

        Iterator<Entity> entityIterator = currentEntities.iterator();
        while (entityIterator.hasNext()) {
            Entity mapEntity = entityIterator.next();
            //Don't break, reset all entities
            mapEntity.sendMessage(MESSAGE.ENTITY_DESELECTED, mapEntity);
            Rectangle mapEntityBoundingBox = mapEntity.getCurrentBoundingBox();
            //Gdx.app.debug(TAG, "Entity Candidate Location " + "(" + mapEntityBoundingBox.x + "," + mapEntityBoundingBox.y + ")");
            if (mapEntity.getCurrentBoundingBox().contains(mouseSelectCoordinates.x, mouseSelectCoordinates.y)) {
                //Check distance
                selectionRay.set(boundingBox.x, boundingBox.y, 0.0f, mapEntityBoundingBox.x, mapEntityBoundingBox.y, 0.0f);
                float distance = selectionRay.origin.dst(selectionRay.direction);

                if (distance <= selectRayMaximumDistance) {
                    //We have a valid entity selection
                    //Picked/Selected
                    LOGGER.trace("Selected Entity! {}", mapEntity.getEntityConfig().getEntityID());

                    //TODO selection
                    /*Entity transferEntity = player;
                    transferEntity.registerComponents(new NPCBehaviorComponent(), new NPCGraphicsComponent(), new NPCPhysicsComponent());
                    player = mapEntity;
                    player.registerComponents(new PlayerInputComponent(), new PlayerGraphicsComponent(), new PlayerPhysicsComponent());
                    mapEntity = transferEntity;
                    entityIterator.remove();
                    currentEntities.add(mapEntity);*/
                    //player
                    mapEntity.sendMessage(MESSAGE.ENTITY_SELECTED, mapEntity);
                }
            }
        }
    }

    private boolean updatePortalLayerActivation(MapManager mapMgr) {
        MapLayer mapPortalLayer = mapMgr.getPortalLayer();

        if (mapPortalLayer == null) {
            LOGGER.debug("Portal Layer doesn't exist!");
            return false;
        }

        Rectangle rectangle = null;

        for (MapObject object : mapPortalLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject) object).getRectangle();

                if (boundingBox.overlaps(rectangle)) {
                    String mapName = object.getName();
                    if (mapName == null) {
                        return false;
                    }

                    mapMgr.setClosestStartPositionFromScaledUnits(currentEntityPosition);
                    mapMgr.loadMap(MapFactory.MapType.valueOf(mapName));

                    currentEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    currentEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;
                    nextEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    nextEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;

                    LOGGER.debug("Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }


}
