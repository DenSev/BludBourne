package com.packtpub.libgdx.bludbourne.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.components.PlayerInputComponent;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created on 16.04.2017.
 */
public enum MouseController {
    INSTANCE;

    private final static Logger LOGGER = LoggerFactory.getLogger(PlayerInputComponent.class);
    private final static MouseKey[] mouseKeys;

    static {
        mouseKeys = new MouseKey[]{
                new MouseKey("SELECT", new int[]{Input.Buttons.LEFT}, (x, y, entity, delta) -> {
                    /*Vector2 speed = new Vector2(4f, 4f);
                    Vector2 movement = new Vector2();
                    Vector2 touch = new Vector2(x, y);
                    Vector2 dir = new Vector2();
                    dir.set(touch).sub(player.getCurrentPosition()).nor();
                    Vector2 velocity = new Vector2(dir).scl(speed);
                    //And on each frame, do something like this
                    movement.set(velocity).scl(delta);
                    player.getCurrentPosition().add(movement);*/
                    LOGGER.trace("SELECT pressed at: {}, {}", x, y);
                    entity.sendMessage(Component.MESSAGE.INIT_SELECT_ENTITY, new Vector3(x, y, 0));
                }),
                new MouseKey("DOACTION", new int[]{Input.Buttons.RIGHT}, (x, y, player, delta) -> {
                    LOGGER.trace("DOACTION pressed at: {}, {}", x, y);
                })
        };
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Arrays.stream(mouseKeys)
                .filter(key -> key.hasKeycode(button))
                .forEach(key -> {
                    key.setLastCoordinates(screenX, screenY);
                    key.pressed();
                });
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Arrays.stream(mouseKeys)
                .filter(key -> key.hasKeycode(button))
                .forEach(key -> {
                    key.setLastCoordinates(screenX, screenY);
                    key.released();
                });
        return true;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        //LOGGER.info("Angle: {}", entity.getCurrentPosition().angleRad(new Vector2(screenX, screenY)));
        return false;
    }

    public void hide() {
        Arrays.stream(mouseKeys).forEach(MouseKey::released);
    }

    public void processInput(Entity entity, float delta) {
        Arrays.stream(mouseKeys)
                .filter(MouseKey::isPressed)
                .forEach(key -> key.keyDown(entity, delta));
    }
}
