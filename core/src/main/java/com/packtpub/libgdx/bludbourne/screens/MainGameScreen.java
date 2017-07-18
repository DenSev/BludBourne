package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.BludBourne;
import com.packtpub.libgdx.bludbourne.UI.PlayerHUD;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityFactory;
import com.packtpub.libgdx.bludbourne.handlers.base.Message;
import com.packtpub.libgdx.bludbourne.map.Map;
import com.packtpub.libgdx.bludbourne.map.MapManager;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public class MainGameScreen extends ScreenAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainGameScreen.class);

    public static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    public enum GameState {
        RUNNING,
        PAUSED
    }

    private static GameState gameState;
    private BludBourne game;
    private InputMultiplexer multiplexer;
    private static PlayerHUD playerHUD;

    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;

    private static Entity player;

    public MainGameScreen(BludBourne game) {
        this.game = game;


        gameState = GameState.RUNNING;
        //_camera setup
        setupViewport(10, 10);

        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(MapManager.INSTANCE.getCurrentTiledMap(), Map.UNIT_SCALE);
        mapRenderer.setView(camera);
        MapManager.INSTANCE.setCamera(camera);

        LOGGER.debug("UnitScale value is: {}", mapRenderer.getUnitScale());

        player = EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);
        MapManager.INSTANCE.setPlayer(player);

        OrthographicCamera hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);

        playerHUD = new PlayerHUD(hudCamera, player);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(playerHUD.getStage());
        multiplexer.addProcessor(player.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

        //removed in ch5
        /*ProfileManager.INSTANCE.addObserver(playerHUD);
        ProfileManager.INSTANCE.addObserver(MapManager.INSTANCE);*/
    }

    @Override
    public void hide() {
        gameState = GameState.PAUSED;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void show() {
        gameState = GameState.RUNNING;
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void render(float delta) {
        if (gameState == GameState.PAUSED) {
            player.updateInput(delta);
            return;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());
        mapRenderer.setView(camera);

        //_mapRenderer.getBatch().enableBlending();
        //_mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (MapManager.INSTANCE.hasMapChanged()) {
            mapRenderer.setMap(MapManager.INSTANCE.getCurrentTiledMap());
            player.sendMessage(Message.INIT_START_POSITION, MapManager.INSTANCE.getPlayerStartUnitScaled());

            camera.position.set(MapManager.INSTANCE.getPlayerStartUnitScaled().x, MapManager.INSTANCE.getPlayerStartUnitScaled().y, 0f);
            camera.update();

            Array<Entity> entities = MapManager.INSTANCE.getCurrentMapEntities();
            for (Entity entity : entities) {
                entity.registerObserver(playerHUD);
            }

            MapManager.INSTANCE.setMapChanged(false);
        }

        mapRenderer.render();
        MapManager.INSTANCE.updateCurrentMapEntities(mapRenderer.getBatch(), delta);

        player.update(mapRenderer.getBatch(), delta);
        playerHUD.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        setupViewport(10, 10);
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        playerHUD.resize((int) VIEWPORT.physicalWidth, (int) VIEWPORT.physicalHeight);
    }

    @Override
    public void pause() {
        gameState = GameState.PAUSED;
        ProfileManager.INSTANCE.saveProfile();
    }

    @Override
    public void resume() {
        gameState = GameState.RUNNING;
        ProfileManager.INSTANCE.loadProfile();
    }

    public static void setGameState(GameState gameState) {
        switch (gameState) {
            case RUNNING:
                MainGameScreen.gameState = GameState.RUNNING;
                break;
            case PAUSED:
                if (MainGameScreen.gameState == GameState.PAUSED) {
                    MainGameScreen.gameState = GameState.RUNNING;
                    ProfileManager.INSTANCE.loadProfile();
                } else if (MainGameScreen.gameState == GameState.RUNNING) {
                    MainGameScreen.gameState = GameState.PAUSED;
                    ProfileManager.INSTANCE.saveProfile();
                }
                break;
            default:
                MainGameScreen.gameState = GameState.RUNNING;
                break;
        }
    }

    @Override
    public void dispose() {
        player.unregisterObservers();
        player.dispose();
        mapRenderer.dispose();
    }

    private void setupViewport(int width, int height) {
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;
        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();
        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);
        //update viewport if there could be skewing
        if (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio) {
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        } else {
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight / VIEWPORT.physicalWidth);
        }
        LOGGER.debug("WorldRenderer:virtual:({}, {})", VIEWPORT.virtualWidth, VIEWPORT.virtualHeight);
        LOGGER.debug("WorldRenderer:viewport:({}, {})", VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        LOGGER.debug("WorldRenderer:physical:({}, {})", VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
    }

}
