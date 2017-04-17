package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Game;
import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;

/**
 * Created on 04/10/2017.
 */
public class BludBourne extends Game {

    public static final MainGameScreen mainGameScreen = new MainGameScreen();

    @Override
    public void create() {
        setScreen(mainGameScreen);
    }

    @Override
    public void dispose() {
        mainGameScreen.dispose();
    }
}