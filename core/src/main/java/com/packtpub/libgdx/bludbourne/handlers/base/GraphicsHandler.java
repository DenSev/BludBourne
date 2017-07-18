package com.packtpub.libgdx.bludbourne.handlers.base;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.packtpub.libgdx.bludbourne.components.AnimationFactory;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;

public abstract class GraphicsHandler extends EntityMessageHandler {


    protected ShapeRenderer shapeRenderer;


    protected GraphicsHandler() {
        //init common handlers
        handlers.put(
            Message.CURRENT_POSITION,
            (entity, args) -> entity.getEntityState().setCurrentPosition((Vector2) args[0])
        );
        handlers.put(
            Message.INIT_START_POSITION,
            (entity, args) -> entity.getEntityState().setCurrentPosition((Vector2) args[0])
        );
        handlers.put(
            Message.CURRENT_STATE,
            (entity, args) -> entity.getEntityState().setCurrentState((State) args[0])
        );
        handlers.put(
            Message.CURRENT_DIRECTION,
            (entity, args) -> entity.getEntityState().setCurrentDirection((Direction) args[0])
        );
        handlers.put(Message.LOAD_ANIMATIONS, (entity, args) -> {
            AnimationFactory.INSTANCE.loadAnimations(entity);
        });
        //init animation factory


        shapeRenderer = new ShapeRenderer();
    }


    public abstract void update(Entity entity, Batch batch, float delta);

    protected void updateAnimations(Entity entity, float delta) {
        entity.getEntityState().setFrameTime((entity.getEntityState().getFrameTime() + delta) % 5);//Want to avoid overflow
        entity.getEntityState().setCurrentFrame(AnimationFactory.INSTANCE.getFrame(entity));
    }


}
