package com.packtpub.libgdx.bludbourne.handlers.base;

import com.badlogic.gdx.InputProcessor;
import com.packtpub.libgdx.bludbourne.entity.Entity;

/**
 * Created on 16.04.2017.
 */
public abstract class BehaviorHandler extends EntityMessageHandler implements InputProcessor {

    public abstract void update(Entity entity, float delta);

    protected BehaviorHandler() {
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
