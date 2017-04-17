package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created on 04/14/2017.
 */
public enum Direction {
    UP(AnimationType.WALK_UP) {
        @Override
        public Vector2 adjustPosition(Vector2 initial, Vector2 delta) {
            initial.y += delta.y;
            return initial;
        }
    },
    RIGHT(AnimationType.WALK_RIGHT) {
        @Override
        public Vector2 adjustPosition(Vector2 initial, Vector2 delta) {
            initial.x += delta.x;
            return initial;
        }
    },
    DOWN(AnimationType.WALK_DOWN) {
        @Override
        public Vector2 adjustPosition(Vector2 initial, Vector2 delta) {
            initial.y -= delta.y;
            return initial;
        }
    },
    LEFT(AnimationType.WALK_LEFT) {
        @Override
        public Vector2 adjustPosition(Vector2 initial, Vector2 delta) {
            initial.x -= delta.x;
            return initial;
        }
    };

    private AnimationType animationType;

    Direction(AnimationType animationType) {
        this.animationType = animationType;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public abstract Vector2 adjustPosition(Vector2 initial, Vector2 delta);

    public static Direction getRandomNext() {
        return Direction.values()[MathUtils.random(Direction.values().length - 1)];
    }
}