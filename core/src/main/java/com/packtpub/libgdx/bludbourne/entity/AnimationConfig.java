package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/**
 * Created on 16.04.2017.
 */
public class AnimationConfig {
    private float frameDuration = 1.0f;
    private AnimationType animationType;
    private Array<String> texturePaths;
    private Array<GridPoint2> gridPoints;

    public AnimationConfig() {
        animationType = AnimationType.IDLE;
        texturePaths = new Array<String>();
        gridPoints = new Array<GridPoint2>();
    }

    public float getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
    }

    public Array<String> getTexturePaths() {
        return texturePaths;
    }

    public void setTexturePaths(Array<String> texturePaths) {
        this.texturePaths = texturePaths;
    }

    public Array<GridPoint2> getGridPoints() {
        return gridPoints;
    }

    public void setGridPoints(Array<GridPoint2> gridPoints) {
        this.gridPoints = gridPoints;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }
}
