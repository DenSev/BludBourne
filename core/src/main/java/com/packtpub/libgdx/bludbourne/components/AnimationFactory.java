package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.google.common.collect.ImmutableMap;
import com.packtpub.libgdx.bludbourne.entity.AnimationConfig;
import com.packtpub.libgdx.bludbourne.entity.AnimationType;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import com.packtpub.libgdx.bludbourne.utility.Utility;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Animation factory, contains necessary methods to load animations for different entity {@link State}
 * <p>
 * Created on 05/06/2017.
 */
public enum AnimationFactory {

    INSTANCE;


    private final Map<State, BiFunction<Entity, AnimationType, TextureRegion>> animationFactory;

    /**
     * constructor, creates factory map with {@link State} as keys
     * and {@link BiFunction} as values
     */
    AnimationFactory() {
        animationFactory = ImmutableMap
            .<State, BiFunction<Entity, AnimationType, TextureRegion>>builder()
            .put(State.WALKING, (Entity entity, AnimationType animationType) -> {
                Animation<TextureRegion> animation = entity.getEntityState().getAnimations().get(animationType);
                if (animation == null) return null;
                return animation.getKeyFrame(entity.getEntityState().getFrameTime());
            })
            .put(State.IDLE, (Entity entity, AnimationType animationType) -> {
                Animation<TextureRegion> animation = entity.getEntityState().getAnimations().get(animationType);
                if (animation == null) return null;
                return animation.getKeyFrames()[0];
            })
            .put(State.IMMOBILE, (Entity entity, AnimationType animationType) -> {
                Animation<TextureRegion> animation = entity.getEntityState().getAnimations().get(AnimationType.IMMOBILE);
                if (animation == null) return null;
                return animation.getKeyFrame(entity.getEntityState().getFrameTime());
            })
            .build();
    }

    /**
     * Loads animation for specific entity, based on
     * {@link AnimationConfig} provided in
     * {@link com.packtpub.libgdx.bludbourne.entity.EntityConfig}
     *
     * @param entity - entity for which we load animations
     */
    public void loadAnimations(Entity entity) {
        Array<AnimationConfig> animationConfigs = entity.getEntityConfig().getAnimationConfig();

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

            entity.getEntityState().getAnimations().put(animationType, animation);
        }
    }

    /**
     * Method specific to two frame animations, where each frame is stored in a separate texture
     *
     * @param firstTexture  - texture of first frame
     * @param secondTexture - texture of second frame
     * @param points        - grid points where frames are located
     * @param frameDuration - frame duration
     * @return - resulting animation
     */
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

    /**
     * Loads animation from texture accessed from path provided with textureName parameter
     *
     * @param textureName   - path to texture
     * @param points        - grid points to indicate where frames are located
     * @param frameDuration - frame duration
     * @return - resulting animation
     */
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

    public TextureRegion getFrame(Entity entity) {
        return animationFactory
            .get(entity.getEntityState().getCurrentState())
            .apply(entity, entity.getEntityState().getCurrentDirection().getAnimationType());
    }
}
