package com.packtpub.libgdx.bludbourne.input;

import com.packtpub.libgdx.bludbourne.entity.Entity;

import java.util.function.BiConsumer;

/**
 * Created on 10.04.2017.
 */
public class InputKey extends AbstractKey {

    private final BiConsumer<Entity, Float> action;

    public InputKey(final String uid, final int[] keycodes, final BiConsumer<Entity, Float> action) {
        super(uid, keycodes);
        this.action = action;
    }

    public void keyDown(Entity entity, Float delta) {
        action.accept(entity, delta);
    }

    public BiConsumer<Entity, Float> getAction() {
        return action;
    }
}
