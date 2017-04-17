package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.components.base.GraphicsComponent;
import com.packtpub.libgdx.bludbourne.entity.AnimationConfig;
import com.packtpub.libgdx.bludbourne.entity.AnimationType;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityConfig;
import com.packtpub.libgdx.bludbourne.entity.State;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.function.Consumer;

public class PlayerGraphicsComponent extends GraphicsComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerGraphicsComponent.class);


    public PlayerGraphicsComponent() {
        handlers.put(MESSAGE.CURRENT_POSITION, (Object... args) -> currentPosition = (Vector2) args[0]);
        handlers.put(MESSAGE.INIT_START_POSITION, (Object... args) -> currentPosition = (Vector2) args[0]);
        handlers.put(MESSAGE.CURRENT_STATE, (Object... args) -> currentState = (State) args[0]);
        handlers.put(MESSAGE.CURRENT_DIRECTION, (Object... args) -> currentDirection = (Direction) args[0]);
        handlers.put(MESSAGE.LOAD_ANIMATIONS, (Object... args) -> {
            EntityConfig entityConfig = (EntityConfig) args[0];
            Array<AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

            for (AnimationConfig animationConfig : animationConfigs) {
                Array<String> textureNames = animationConfig.getTexturePaths();
                Array<GridPoint2> points = animationConfig.getGridPoints();
                AnimationType animationType = animationConfig.getAnimationType();
                float frameDuration = animationConfig.getFrameDuration();
                Animation<TextureRegion> animation = null;

                if (textureNames.size == 1) {
                    animation = loadAnimation(textureNames.get(0), points, frameDuration);
                } else if (textureNames.size == 2) {
                    animation = loadAnimation(textureNames.get(0), textureNames.get(1), points, frameDuration);
                }

                animations.put(animationType, animation);
            }
        });
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
