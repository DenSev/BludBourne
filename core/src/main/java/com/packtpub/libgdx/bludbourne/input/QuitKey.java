package com.packtpub.libgdx.bludbourne.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created on 04/27/2017.
 */
public class QuitKey extends InputKey {

    //private BiConsumer<Entity, Float> action = ;

    public QuitKey() {
        super("QUIT", new int[]{Input.Keys.Q}, (entity, aFloat) -> {
            //released();
            Gdx.app.exit();
        });
    }

}
