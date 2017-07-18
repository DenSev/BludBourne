package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.packtpub.libgdx.bludbourne.screens.LoadGameScreen;
import com.packtpub.libgdx.bludbourne.screens.MainGameScreen;
import com.packtpub.libgdx.bludbourne.screens.MainMenuScreen;
import com.packtpub.libgdx.bludbourne.screens.NewGameScreen;

/**
 * Created on 04/10/2017.
 */
public class BludBourne extends Game {

    private MainGameScreen mainGameScreen;
    private MainMenuScreen mainMenuScreen;
    private LoadGameScreen loadGameScreen;
    private NewGameScreen newGameScreen;

    public enum ScreenType {
        MainMenu,
        MainGame,
        LoadGame,
        NewGame
    }

    public Screen getScreenType(ScreenType screenType) {
        switch (screenType) {
            case MainMenu:
                return mainMenuScreen;
            case MainGame:
                return mainGameScreen;
            case LoadGame:
                return loadGameScreen;
            case NewGame:
                return newGameScreen;
            default:
                return mainMenuScreen;
        }

    }

    @Override
    public void create() {
        mainGameScreen = new MainGameScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        loadGameScreen = new LoadGameScreen(this);
        newGameScreen = new NewGameScreen(this);
        setScreen(mainMenuScreen);

    }

    @Override
    public void dispose() {
        mainGameScreen.dispose();
        mainMenuScreen.dispose();
        loadGameScreen.dispose();
        newGameScreen.dispose();
    }
}