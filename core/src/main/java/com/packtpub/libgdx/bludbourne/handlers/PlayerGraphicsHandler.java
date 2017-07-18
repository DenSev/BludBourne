package com.packtpub.libgdx.bludbourne.handlers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.handlers.base.GraphicsHandler;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerGraphicsHandler extends GraphicsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerGraphicsHandler.class);
    private static final PlayerGraphicsHandler instance = new PlayerGraphicsHandler();

    public static GraphicsHandler instance() {
        return instance;
    }

    @Override
    public void update(Entity entity, Batch batch, float delta) {
        updateAnimations(entity, delta);

        Camera camera = MapManager.INSTANCE.getCamera();
        camera.position.set(entity.getEntityState().getCurrentPosition(), 0f);
        camera.update();

        batch.begin();
        batch.draw(
            entity.getEntityState().getCurrentFrame(),
            entity.getEntityState().getCurrentPosition().x,
            entity.getEntityState().getCurrentPosition().y,
            1, 1
        );
        batch.end();

        //Used to graphically debug boundingboxes
        /*
        Rectangle rect = entity.getCurrentBoundingBox();
        _shapeRenderer.setProjectionMatrix(camera.combined);
        _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.RED);
        _shapeRenderer.rect(rect.getX() * Map.UNIT_SCALE , rect.getY() * Map.UNIT_SCALE, rect.getWidth() * Map.UNIT_SCALE, rect.getHeight()*Map.UNIT_SCALE);
        _shapeRenderer.end();
        */
    }

    public void dispose() {
    }

}
