package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.packtpub.libgdx.bludbourne.components.base.GraphicsComponent;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerGraphicsComponent extends GraphicsComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerGraphicsComponent.class);


    public PlayerGraphicsComponent() {

    }

    @Override
    public void update(Entity entity, MapManager mapMgr, Batch batch, float delta) {
        updateAnimations(delta);

        Camera camera = mapMgr.getCamera();
        camera.position.set(currentPosition.x, currentPosition.y, 0f);
        camera.update();

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
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

    @Override
    public void dispose() {
    }

}
