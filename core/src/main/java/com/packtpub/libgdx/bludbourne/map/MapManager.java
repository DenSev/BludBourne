package com.packtpub.libgdx.bludbourne.map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.observers.ProfileObserver;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public enum MapManager implements ProfileObserver {

    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);
    private Camera camera;
    private boolean mapChanged = false;
    private Map currentMap;
    private Entity player;
    private Entity currentSelectedEntity = null;

    MapManager() {
        ProfileManager.INSTANCE.addObserver(this);
    }

    public void loadMap(MapFactory.MapType mapType) {
        Map map = MapFactory.INSTANCE.getMap(mapType);
        if (map == null) {
            LOGGER.debug("Map does not exist!  ");
            return;
        }

        //Unregister observers
        if (currentMap != null) {
            Array<Entity> entities = currentMap.getMapEntities();
            for (Entity entity : entities) {
                entity.unregisterObservers();
            }
        }


        currentMap = map;
        mapChanged = true;
        clearCurrentSelectedMapEntity();
        LOGGER.debug("Player Start: ({},{})", currentMap.getPlayerStart().x, currentMap.getPlayerStart().y);
    }

    @Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch (event) {
            case PROFILE_LOADED:
                String currentMap = profileManager.getProperty("currentMapType", String.class);
                MapFactory.MapType mapType;
                if (currentMap == null || currentMap.isEmpty()) {
                    mapType = MapFactory.MapType.TOWN;
                } else {
                    mapType = MapFactory.MapType.valueOf(currentMap);
                }
                loadMap(mapType);

                Vector2 topWorldMapStartPosition = profileManager.getProperty("topWorldMapStartPosition", Vector2.class);
                if (topWorldMapStartPosition != null) {
                    MapFactory.INSTANCE.getMap(MapFactory.MapType.TOP_WORLD).setPlayerStart(topWorldMapStartPosition);
                }

                Vector2 castleOfDoomMapStartPosition = profileManager.getProperty("castleOfDoomMapStartPosition", Vector2.class);
                if (castleOfDoomMapStartPosition != null) {
                    MapFactory.INSTANCE.getMap(MapFactory.MapType.CASTLE_OF_DOOM).setPlayerStart(castleOfDoomMapStartPosition);
                }

                Vector2 townMapStartPosition = profileManager.getProperty("townMapStartPosition", Vector2.class);
                if (townMapStartPosition != null) {
                    MapFactory.INSTANCE.getMap(MapFactory.MapType.TOWN).setPlayerStart(townMapStartPosition);
                }

                break;
            case SAVING_PROFILE:
                profileManager.setProperty("currentMapType", this.currentMap.currentMapType.toString());
                profileManager.setProperty("topWorldMapStartPosition", MapFactory.INSTANCE.getMap(MapFactory.MapType.TOP_WORLD).getPlayerStart());
                profileManager.setProperty("castleOfDoomMapStartPosition", MapFactory.INSTANCE.getMap(MapFactory.MapType.CASTLE_OF_DOOM).getPlayerStart());
                profileManager.setProperty("townMapStartPosition", MapFactory.INSTANCE.getMap(MapFactory.MapType.TOWN).getPlayerStart());
                break;
            default:
                break;
        }
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public MapLayer getCollisionLayer() {
        return currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer() {
        return currentMap.getPortalLayer();
    }

    public Vector2 getPlayerStartUnitScaled() {
        return currentMap.getPlayerStartUnitScaled();
    }

    public TiledMap getCurrentTiledMap() {
        if (currentMap == null) {
            loadMap(MapFactory.MapType.TOWN);
        }
        return currentMap.getCurrentTiledMap();
    }

    public Entity getCurrentSelectedMapEntity() {
        return currentSelectedEntity;
    }

    public void setCurrentSelectedMapEntity(Entity currentSelectedEntity) {
        this.currentSelectedEntity = currentSelectedEntity;
    }

    public void clearCurrentSelectedMapEntity() {
        if (currentSelectedEntity == null) return;
        currentSelectedEntity.sendMessage(Message.ENTITY_DESELECTED);
        currentSelectedEntity = null;
    }

    public void updateCurrentMapEntities(Batch batch, float delta) {
        currentMap.updateMapEntities(batch, delta);
    }

    public final Array<Entity> getCurrentMapEntities() {
        return currentMap.getMapEntities();
    }

    public void setPlayer(Entity entity) {
        this.player = entity;
    }

    public Entity getPlayer() {
        return this.player;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean hasMapChanged() {
        return mapChanged;
    }

    public void setMapChanged(boolean hasMapChanged) {
        this.mapChanged = hasMapChanged;
    }

}
