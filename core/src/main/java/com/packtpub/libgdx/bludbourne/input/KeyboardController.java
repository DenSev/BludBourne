package com.packtpub.libgdx.bludbourne.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created on 16.04.2017.
 */
public enum KeyboardController {
    INSTANCE;

    private final static Logger LOGGER = LoggerFactory.getLogger(KeyboardController.class);
    private final static InputKey[] inputKeys;

    static {
        inputKeys = new InputKey[]{
                new InputKey("LEFT", new int[]{Input.Keys.A, Input.Keys.LEFT}, (entity, delta) -> {
                    LOGGER.trace("LEFT");
                    entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.WALKING);
                    entity.sendMessage(Component.MESSAGE.CURRENT_DIRECTION, Direction.LEFT);
                }),
                new InputKey("RIGHT", new int[]{Input.Keys.RIGHT, Input.Keys.D}, (entity, delta) -> {
                    LOGGER.trace("RIGHT");
                    entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.WALKING);
                    entity.sendMessage(Component.MESSAGE.CURRENT_DIRECTION, Direction.RIGHT);
                }),
                new InputKey("UP", new int[]{Input.Keys.UP, Input.Keys.W}, (entity, delta) -> {
                    LOGGER.trace("UP");
                    entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.WALKING);
                    entity.sendMessage(Component.MESSAGE.CURRENT_DIRECTION, Direction.UP);
                }),
                new InputKey("DOWN", new int[]{Input.Keys.DOWN, Input.Keys.S}, (entity, delta) -> {
                    LOGGER.trace("DOWN");
                    entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.WALKING);
                    entity.sendMessage(Component.MESSAGE.CURRENT_DIRECTION, Direction.DOWN);
                }),
                new InputKey("QUIT", new int[]{Input.Keys.Q}, (player, delta) -> Gdx.app.exit())
        };
    }


    public boolean keyDown(int keycode) {
        Arrays.stream(inputKeys)
                .filter(key -> key.hasKeycode(keycode))
                .forEach(InputKey::pressed);
        return true;
    }

    public boolean keyUp(int keycode) {
        Arrays.stream(inputKeys)
                .filter(key -> key.hasKeycode(keycode))
                .forEach(InputKey::released);
        return true;
    }

    public void hide() {
        Arrays.stream(inputKeys).forEach(InputKey::released);
    }

    static boolean idle = true;
    public void processInput(Entity entity, float delta) {
        idle = true;
        Arrays.stream(inputKeys)
                .filter(InputKey::isPressed)
                .forEach(key -> {
                    idle = false;
                    key.keyDown(entity, delta);
                });

        if (idle) {
            entity.sendMessage(Component.MESSAGE.CURRENT_STATE, State.IDLE);
            //entity.sendMessage(Component.MESSAGE.CURRENT_DIRECTION, Direction.DOWN);
        }
    }
}
