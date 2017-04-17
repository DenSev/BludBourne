package com.packtpub.libgdx.bludbourne.components.base;

import com.badlogic.gdx.InputProcessor;
import com.packtpub.libgdx.bludbourne.components.base.BehaviorComponent;

/**
 * Based on {@link com.badlogic.gdx.InputAdapter}
 *
 * Created on 16.04.2017.
 */
public abstract class PlayerBehaviorComponent extends BehaviorComponent implements InputProcessor {

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
