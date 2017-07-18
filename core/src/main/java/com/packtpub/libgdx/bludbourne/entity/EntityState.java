package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.packtpub.libgdx.bludbourne.map.Map;

import java.util.HashMap;

/**
 * Created on 04/26/2017.
 */
public class EntityState {

    private float frameTime = 0f;
    private TextureRegion currentFrame = null;
    private Direction currentDirection = Direction.DOWN;
    private State currentState = State.WALKING;
    private Vector2 currentPosition = new Vector2(0, 0);
    private final java.util.Map<AnimationType, Animation<TextureRegion>> animations = new HashMap<>();
    private Vector2 nextPosition = new Vector2(0, 0);
    private Rectangle boundingBox = new Rectangle();
    private Vector2 velocity = new Vector2(4f, 4f);
    private BoundingBoxLocation boundingBoxLocation = BoundingBoxLocation.BOTTOM_LEFT;
    private boolean sentShowConversationMessage = false;
    private boolean sentHideCoversationMessage = false;

    public EntityState() {
    }

    public enum BoundingBoxLocation {
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        CENTER
    }

    public float getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(float frameTime) {
        this.frameTime = frameTime;
    }

    public Vector2 getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(Vector2 nextPosition) {
        this.nextPosition = nextPosition;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public BoundingBoxLocation getBoundingBoxLocation() {
        return boundingBoxLocation;
    }

    public void setBoundingBoxLocation(BoundingBoxLocation boundingBoxLocation) {
        this.boundingBoxLocation = boundingBoxLocation;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(TextureRegion currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public Vector2 getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Vector2 currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void calculateNextPosition(float deltaTime) {
        if (currentDirection == null) return;

        Vector2 point = new Vector2(currentPosition);
        velocity.scl(deltaTime);
        nextPosition = currentDirection.adjustPosition(point, velocity);
        //velocity
        velocity.scl(1 / deltaTime);
    }

    public void initBoundingBox(float percentageWidthReduced, float percentageHeightReduced) {
        //Update the current bounding box
        float width;
        float height;

        float origWidth = Entity.FRAME_WIDTH;
        float origHeight = Entity.FRAME_HEIGHT;

        float widthReductionAmount = 1.0f - percentageWidthReduced; //.8f for 20% (1 - .20)
        float heightReductionAmount = 1.0f - percentageHeightReduced; //.8f for 20% (1 - .20)

        if (widthReductionAmount > 0 && widthReductionAmount < 1) {
            width = Entity.FRAME_WIDTH * widthReductionAmount;
        } else {
            width = Entity.FRAME_WIDTH;
        }

        if (heightReductionAmount > 0 && heightReductionAmount < 1) {
            height = Entity.FRAME_HEIGHT * heightReductionAmount;
        } else {
            height = Entity.FRAME_HEIGHT;
        }

        if (width == 0 || height == 0) {
            //LOGGER.trace("Width and Height are 0!! {}:{}", width, height);
        }

        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;

        if (Map.UNIT_SCALE > 0) {
            minX = nextPosition.x / Map.UNIT_SCALE;
            minY = nextPosition.y / Map.UNIT_SCALE;
        } else {
            minX = nextPosition.x;
            minY = nextPosition.y;
        }

        boundingBox.setWidth(width);
        boundingBox.setHeight(height);

        switch (boundingBoxLocation) {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, width, height);
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + origWidth / 2, minY + origHeight / 4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + origWidth / 2, minY + origHeight / 2);
                break;
        }

        //Gdx.app.debug(TAG, "SETTING Bounding Box for " + entity.getEntityConfig().getEntityID() + ": (" + minX + "," + minY + ")  width: " + width + " height: " + height);
    }

    public void updateBoundingBoxPosition() {
        Vector2 position = nextPosition;
        //Need to account for the unitscale, since the map coordinates will be in pixels
        float minX;
        float minY;

        if (Map.UNIT_SCALE > 0) {
            minX = position.x / Map.UNIT_SCALE;
            minY = position.y / Map.UNIT_SCALE;
        } else {
            minX = position.x;
            minY = position.y;
        }

        switch (boundingBoxLocation) {
            case BOTTOM_LEFT:
                boundingBox.set(minX, minY, boundingBox.getWidth(), boundingBox.getHeight());
                break;
            case BOTTOM_CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH / 2, minY + Entity.FRAME_HEIGHT / 4);
                break;
            case CENTER:
                boundingBox.setCenter(minX + Entity.FRAME_WIDTH / 2, minY + Entity.FRAME_HEIGHT / 2);
                break;
        }

        //LOGGER.trace("SETTING Bounding Box for x: ({},{})", minX, minY);
    }

    public java.util.Map<AnimationType, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public boolean isSentShowConversationMessage() {
        return sentShowConversationMessage;
    }

    public void setSentShowConversationMessage(boolean sentShowConversationMessage) {
        this.sentShowConversationMessage = sentShowConversationMessage;
    }

    public boolean isSentHideCoversationMessage() {
        return sentHideCoversationMessage;
    }

    public void setSentHideCoversationMessage(boolean sentHideCoversationMessage) {
        this.sentHideCoversationMessage = sentHideCoversationMessage;
    }
}
