package com.packtpub.libgdx.bludbourne.handlers;

import com.badlogic.gdx.Gdx;
import com.packtpub.libgdx.bludbourne.entity.Direction;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.handlers.base.BehaviorHandler;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.input.KeyboardController;
import com.packtpub.libgdx.bludbourne.input.MouseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public class PlayerInputHandler extends BehaviorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerInputHandler.class);
    private static final PlayerInputHandler instance = new PlayerInputHandler();

    public static BehaviorHandler instance() {
        return instance;
    }

    private PlayerInputHandler() {
        handlers.put(
            Message.CURRENT_DIRECTION,
            (Entity entity, Object... args) -> entity.getEntityState().setCurrentDirection((Direction) args[0])
        );
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