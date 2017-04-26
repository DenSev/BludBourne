package com.packtpub.libgdx.bludbourne.components.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.ImmutableMap;
import com.packtpub.libgdx.bludbourne.entity.AnimationConfig;
import com.packtpub.libgdx.bludbourne.entity.AnimationType;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityConfig;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import com.packtpub.libgdx.bludbourne.utility.Utility;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class GraphicsComponent extends Component {
    protected TextureRegion currentFrame = null;
    protected float frameTime = 0f;
    protected State currentState;
    protected Direction currentDirection;
    protected Vector2 currentPosition;
    protected final Map<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    ;
    protected ShapeRenderer shapeRenderer;
    private final Map<State, Function<AnimationType, TextureRegion>> animationFactory;

    protected GraphicsComponent() {
        //init common handlers
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
        //init animation factory
        animationFactory = ImmutableMap
            .<State, Function<AnimationType, TextureRegion>>builder()
            .put(State.WALKING, (animationType -> {
                Animation<TextureRegion> animation = animations.get(animationType);
                if (animation == null) return null;
                return animation.getKeyFrame(frameTime);
            }))
            .put(State.IDLE, (animationType -> {
                Animation<TextureRegion> animation = animations.get(animationType);
                if (animation == null) return null;
                return animation.getKeyFrames()[0];
            }))
            .put(State.IMMOBILE, (animationType -> {
                Animation<TextureRegion> animation = animations.get(AnimationType.IMMOBILE);
                if (animation == null) return null;
                return animation.getKeyFrame(frameTime);
            }))
            .build();

        currentPosition = new Vector2(0, 0);
        currentState = State.WALKING;
        currentDirection = Direction.DOWN;
        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(Entity entity, MapManager mapManager, Batch batch, float delta);

    protected void updateAnimations(float delta) {
        frameTime = (frameTime + delta) % 5; //Want to avoid overflow
        currentFrame = animationFactory.get(currentState).apply(currentDirection.getAnimationType());
    }

    //Specific to two frame animations where each frame is stored in a separate texture
    private Animation<TextureRegion> loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration) {
        Utility.loadTextureAsset(firstTexture);
        Texture texture1 = Utility.getTextureAsset(firstTexture);

        Utility.loadTextureAsset(secondTexture);
        Texture texture2 = Utility.getTextureAsset(secondTexture);

        TextureRegion[][] texture1Frames = TextureRegion.split(texture1, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(2);

        GridPoint2 point = points.first();

        animationKeyFrames.add(texture1Frames[point.x][point.y]);
        animationKeyFrames.add(texture2Frames[point.x][point.y]);

        return new Animation<>(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    private Animation<TextureRegion> loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration) {
        Utility.loadTextureAsset(textureName);
        Texture texture = Utility.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture, Entity.FRAME_WIDTH, Entity.FRAME_HEIGHT);

        TextureRegion[] animationKeyFrames = new TextureRegion[points.size];

        for (int i = 0; i < points.size; i++) {
            animationKeyFrames[i] = textureFrames[points.get(i).x][points.get(i).y];
        }

        Animation<TextureRegion> animation = new Animation<TextureRegion>(frameDuration, animationKeyFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        return animation;
    }
}
