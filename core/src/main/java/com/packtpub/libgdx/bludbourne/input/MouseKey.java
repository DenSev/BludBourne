package com.packtpub.libgdx.bludbourne.input;

import com.badlogic.gdx.math.Vector2;
import com.packtpub.libgdx.bludbourne.entity.Entity;

/**
 * Created on 04/11/2017.
 */
public class MouseKey extends AbstractKey {

    private Vector2 lastCoordinates = new Vector2();
    private final MousePressAction action;

    public MouseKey(final String uid, final int[] keycodes, final MousePressAction action) {
        super(uid, keycodes);
        this.action = action;
    }

    public Vector2 getLastCoordinates() {
        return lastCoordinates;
    }

    public void setLastCoordinates(int x, int y) {
        this.lastCoordinates.set(x, y);
    }

    public void keyDown(Entity entity, Float delta) {
        action.accept(lastCoordinates.x, lastCoordinates.y, entity, delta);
    }
}
