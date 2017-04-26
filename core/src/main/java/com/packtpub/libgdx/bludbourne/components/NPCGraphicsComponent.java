package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.bludbourne.components.base.GraphicsComponent;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCGraphicsComponent extends GraphicsComponent {
    private final static Logger LOGGER = LoggerFactory.getLogger(NPCGraphicsComponent.class);

    private static boolean isSelected = false;

    public NPCGraphicsComponent() {
        super();
        handlers.put(MESSAGE.ENTITY_SELECTED, (Object... args) -> {
            LOGGER.debug("entity: {}", ((Entity) args[0]).getEntityConfig().getEntityID());
            ((Entity) args[0]).isSelected = true;
        });
        handlers.put(MESSAGE.ENTITY_DESELECTED, (Object... args) -> {
            LOGGER.debug("entity: {}", ((Entity) args[0]).getEntityConfig().getEntityID());
            ((Entity) args[0]).isSelected = false;
        });
    }

    @Override
    public void update(Entity entity, MapManager mapMgr, Batch batch, float delta) {

        updateAnimations(delta);

        if (entity.isSelected) {

            drawSelected(entity, mapMgr);
        }

        batch.begin();
        batch.draw(currentFrame, currentPosition.x, currentPosition.y, 1, 1);
        batch.end();

        //Used to graphically debug boundingboxes
        /*
        Rectangle rect = entity.getCurrentBoundingBox();
        Camera camera = mapMgr.getCamera();
        _shapeRenderer.setProjectionMatrix(camera.combined);
        _shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        _shapeRenderer.setColor(Color.BLACK);
        _shapeRenderer.rect(rect.getX() * Map.UNIT_SCALE, rect.getY() * Map.UNIT_SCALE, rect.getWidth() * Map.UNIT_SCALE, rect.getHeight() * Map.UNIT_SCALE);
        _shapeRenderer.end();
        */
    }

    private void drawSelected(Entity entity, MapManager mapMgr) {
        LOGGER.trace("drawSelected called for entityID {}", entity.getEntityConfig().getEntityID());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Camera camera = mapMgr.getCamera();
        Rectangle rect = entity.getCurrentBoundingBox();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.0f, 1.0f, 1.0f, 0.5f);

        float width = rect.getWidth() * Map.UNIT_SCALE * 2f;
        float height = rect.getHeight() * Map.UNIT_SCALE / 2f;
        float x = rect.x * Map.UNIT_SCALE - width / 4;
        float y = rect.y * Map.UNIT_SCALE - height / 2;

        shapeRenderer.ellipse(x, y, width, height);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    @Override
    public void dispose() {
    }
}
