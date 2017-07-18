package com.packtpub.libgdx.bludbourne.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.entity.Entity;

public class TopWorldMap extends Map {
    private static String mapPath = "maps/topworld.tmx";

    TopWorldMap() {
        super(MapFactory.MapType.TOP_WORLD, mapPath);
    }

    @Override
    public void updateMapEntities(Batch batch, float delta) {
        for (Entity entity : mapEntities) {
            entity.update(batch, delta);
        }
    }
}
