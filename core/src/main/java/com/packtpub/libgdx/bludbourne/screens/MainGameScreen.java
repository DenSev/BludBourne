package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Json;
import com.packtpub.libgdx.bludbourne.MapManager;
import com.packtpub.libgdx.bludbourne.components.base.Component;
import com.packtpub.libgdx.bludbourne.entity.Entity;
import com.packtpub.libgdx.bludbourne.entity.EntityFactory;
import com.packtpub.libgdx.bludbourne.map.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public class MainGameScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private OrthogonalTiledMapRenderer mapRenderer = null;
    private OrthographicCamera camera = null;
    private static MapManager mapMgr;
    private Json json;

    private static Entity player;

    public MainGameScreen() {
        mapMgr = new MapManager();
        json = new Json();
    }


    @Override
    public void show() {
        //_camera setup
        setupViewport(10, 10);
        //get the current size
        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(mapMgr.getCurrentTiledMap(), Map.UNIT_SCALE);
        mapRenderer.setView(camera);

        mapMgr.setCamera(camera);
        player = EntityFactory.getEntity(EntityFactory.EntityType.PLAYER);
        mapMgr.setPlayer(player);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());
        mapRenderer.setView(camera);

        //_mapRenderer.getBatch().enableBlending();
        //_mapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (mapMgr.hasMapChanged()) {
            mapRenderer.setMap(mapMgr.getCurrentTiledMap());
            player.sendMessage(Component.MESSAGE.INIT_START_POSITION, mapMgr.getPlayerStartUnitScaled());

            camera.position.set(mapMgr.getPlayerStartUnitScaled().x, mapMgr.getPlayerStartUnitScaled().y, 0f);
            camera.update();

            mapMgr.setMapChanged(false);
        }

        mapRenderer.render();
        mapMgr.updateCurrentMapEntities(mapMgr, mapRenderer.getBatch(), delta);

        player.update(mapMgr, mapRenderer.getBatch(), delta);
    }

    @Override
    public void dispose() {
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
        logger.debug("WorldRenderer:virtual:({}, {})", VIEWPORT.virtualWidth, VIEWPORT.virtualHeight);
        logger.debug("WorldRenderer:viewport:({}, {})", VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        logger.debug("WorldRenderer:physical:({}, {})", VIEWPORT.physicalWidth, VIEWPORT.physicalHeight);
    }

}
