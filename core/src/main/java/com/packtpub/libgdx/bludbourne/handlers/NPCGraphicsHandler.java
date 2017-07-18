package com.packtpub.libgdx.bludbourne.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.handlers.base.GraphicsHandler;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import com.packtpub.libgdx.bludbourne.observers.EntityObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCGraphicsHandler extends GraphicsHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(NPCGraphicsHandler.class);
    private final static NPCGraphicsHandler instance = new NPCGraphicsHandler();

    public static GraphicsHandler instance() {
        return instance;
    }

    private NPCGraphicsHandler() {
        super();
        handlers.put(Message.ENTITY_SELECTED, (entity, args) -> {
            if (entity != null) {
                entity.isSelected = !entity.isSelected;
            }

        });
        handlers.put(Message.ENTITY_DESELECTED, (entity, args) -> {
            if (entity != null) {
                entity.isSelected = false;
            }
        });
    }

    @Override
    public void update(Entity entity, Batch batch, float delta) {
        updateAnimations(entity, delta);

        if (entity.isSelected) {
            drawSelected(entity);
            MapManager.INSTANCE.setCurrentSelectedMapEntity(entity);
            if (!entity.getEntityState().isSentShowConversationMessage()) {
                entity.notify(entity.getEntityConfig(), EntityObserver.ComponentEvent.SHOW_CONVERSATION);
                entity.getEntityState().setSentShowConversationMessage(true);
                entity.getEntityState().setSentHideCoversationMessage(false);
            }
        } else {
            if (!entity.getEntityState().isSentHideCoversationMessage()) {
                entity.notify(entity.getEntityConfig(), EntityObserver.ComponentEvent.HIDE_CONVERSATION);
                entity.getEntityState().setSentShowConversationMessage(false);
                entity.getEntityState().setSentHideCoversationMessage(true);
            }
        }

        batch.begin();
        batch.draw(
            entity.getEntityState().getCurrentFrame(),
            entity.getEntityState().getCurrentPosition().x,
            entity.getEntityState().getCurrentPosition().y,
            1, 1
        );
        batch.end();

        //Used to graphically debug boundingboxes

        Rectangle rect = entity.getEntityState().getBoundingBox();
        Camera camera = MapManager.INSTANCE.getCamera();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(rect.getX() * Map.UNIT_SCALE, rect.getY() * Map.UNIT_SCALE, rect.getWidth() * Map.UNIT_SCALE, rect.getHeight() * Map.UNIT_SCALE);
        shapeRenderer.end();
    }

    private void drawSelected(Entity entity) {
        LOGGER.trace("drawSelected called for entityID {}", entity.getEntityConfig().getEntityID());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Camera camera = MapManager.INSTANCE.getCamera();
        Rectangle rect = entity.getEntityState().getBoundingBox();
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

    public void dispose() {
    }
}
