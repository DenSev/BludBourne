package com.packtpub.libgdx.bludbourne.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.entity.Entity;

public class TopWorldMap extends Map {
    private static String _mapPath = "maps/topworld.tmx";

    TopWorldMap() {
        super(MapFactory.MapType.TOP_WORLD, _mapPath);
    }

    @Override
    public void updateMapEntities(MapManager mapMgr, Batch batch, float delta) {
        for (Entity entity : mapEntities) {
            entity.update(mapMgr, batch, delta);
        }
    }
}
