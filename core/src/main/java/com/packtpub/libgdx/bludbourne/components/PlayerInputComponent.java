package com.packtpub.libgdx.bludbourne.components;

import com.badlogic.gdx.Gdx;
import com.packtpub.libgdx.bludbourne.components.base.PlayerBehaviorComponent;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.input.KeyboardController;
import com.packtpub.libgdx.bludbourne.input.MouseController;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created on 04/10/2017.
 */
public class PlayerInputComponent extends PlayerBehaviorComponent {

    private final static Logger LOGGER = LoggerFactory.getLogger(PlayerInputComponent.class);

    public PlayerInputComponent() {
        handlers.put(MESSAGE.CURRENT_DIRECTION, (Object... args) -> currentDirection = (Direction) args[0]);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void update(Entity entity, float delta) {
        KeyboardController.INSTANCE.processInput(entity, delta);
        MouseController.INSTANCE.processInput(entity, delta);
    }

    public boolean keyDown(int keycode) {
        return KeyboardController.INSTANCE.keyDown(keycode);
    }

    public boolean keyUp(int keycode) {
        return KeyboardController.INSTANCE.keyUp(keycode);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return MouseController.INSTANCE.touchDown(screenX, screenY, pointer, button);
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return MouseController.INSTANCE.touchUp(screenX, screenY, pointer, button);
    }


    public boolean mouseMoved(int screenX, int screenY) {
        return MouseController.INSTANCE.mouseMoved(screenX, screenY);
    }

    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    public static void hide() {
        KeyboardController.INSTANCE.hide();
        MouseController.INSTANCE.hide();
    }


}