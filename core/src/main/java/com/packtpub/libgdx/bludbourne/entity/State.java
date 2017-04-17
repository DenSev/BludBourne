package com.packtpub.libgdx.bludbourne.entity;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created on 16.04.2017.
 */
public enum State {
    IDLE,
    WALKING,
    IMMOBILE;

    static public State getRandomNext() {
        //Ignore IMMOBILE which should be last state
        return State.values()[MathUtils.random(State.values().length - 2)];
    }
}
