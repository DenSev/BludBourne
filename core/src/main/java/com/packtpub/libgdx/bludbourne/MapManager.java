package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public class MapManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapManager.class);
    private Camera camera;
    private boolean mapChanged = false;
    private Map currentMap;
    private Entity player;

    public MapManager() {
    }

    public void loadMap(MapFactory.MapType mapType) {
        Map map = MapFactory.getMap(mapType);
        if (map == null) {
            LOGGER.debug("Map does not exist!  ");
            return;
        }
        currentMap = map;
        mapChanged = true;
        LOGGER.debug("Player Start: ({},{})", currentMap.getPlayerStart().x, currentMap.getPlayerStart().y);
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

    public void updateCurrentMapEntities(MapManager mapMgr, Batch batch, float delta) {
        currentMap.updateMapEntities(mapMgr, batch, delta);
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
