package com.packtpub.libgdx.bludbourne.components.base;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.Utility;
import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.entity.AnimationType;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;

import java.util.HashMap;

public abstract class GraphicsComponent extends Component {
    protected TextureRegion currentFrame = null;
    protected float frameTime = 0f;
    protected State currentState;
    protected Direction currentDirection;
    protected Vector2 currentPosition;
    protected HashMap<AnimationType, Animation<TextureRegion>> animations;
    protected ShapeRenderer shapeRenderer;

    protected GraphicsComponent() {
        currentPosition = new Vector2(0, 0);
        currentState = State.WALKING;
        currentDirection = Direction.DOWN;
        animations = new HashMap<AnimationType, Animation<TextureRegion>>();
        shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(Entity entity, MapManager mapManager, Batch batch, float delta);

    protected void updateAnimations(float delta) {
        frameTime = (frameTime + delta) % 5; //Want to avoid overflow

        //Look into the appropriate variable when changing position
        switch (currentDirection) {
            case DOWN:
                if (currentState == State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_DOWN);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_DOWN);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.IMMOBILE);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;
            case LEFT:
                if (currentState == State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_LEFT);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_LEFT);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.IMMOBILE);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;
            case UP:
                if (currentState == State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_UP);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_UP);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.IMMOBILE);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;
            case RIGHT:
                if (currentState == State.WALKING) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_RIGHT);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                } else if (currentState == State.IDLE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.WALK_RIGHT);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrames()[0];
                } else if (currentState == State.IMMOBILE) {
                    Animation<TextureRegion> animation = animations.get(AnimationType.IMMOBILE);
                    if (animation == null) return;
                    currentFrame = animation.getKeyFrame(frameTime);
                }
                break;
            default:
                break;
        }
    }

    //Specific to two frame animations where each frame is stored in a separate texture
    protected Animation<TextureRegion> loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration) {
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

        return new Animation<TextureRegion>(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    protected Animation<TextureRegion> loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration) {
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
