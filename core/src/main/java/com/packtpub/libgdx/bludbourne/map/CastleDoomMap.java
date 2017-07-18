package com.packtpub.libgdx.bludbourne.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.entity.Entity;

public class CastleDoomMap extends Map {
    private static String _mapPath = "maps/castle_of_doom.tmx";

    CastleDoomMap() {
        super(MapFactory.MapType.CASTLE_OF_DOOM, _mapPath);
    }

    @Override
    public void updateMapEntities(Batch batch, float delta) {
        for (Entity entity : mapEntities) {
            entity.update(batch, delta);
        }
    }

}
