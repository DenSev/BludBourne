package com.packtpub.libgdx.bludbourne.utility;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 04/10/2017.
 */
public class Utility {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);
    public static final AssetManager assetManager = new AssetManager();


    private static InternalFileHandleResolver filePathResolver = new InternalFileHandleResolver();

    public static void unloadAsset(String assetFilenamePath) {
        // once the asset manager is done loading
        if (assetManager.isLoaded(assetFilenamePath)) {
            assetManager.unload(assetFilenamePath);
        } else {
            LOGGER.warn("Asset is not loaded; Nothing to unload: {}", assetFilenamePath);
        }
    }

    public static float loadCompleted() {
        return assetManager.getProgress();
    }

    public static int numberAssetsQueued() {
        return assetManager.getQueuedAssets();
    }

    public static boolean updateAssetLoading() {
        return assetManager.update();
    }

    public static boolean isAssetLoaded(String fileName) {
        return assetManager.isLoaded(fileName);
    }

    public static void loadMapAsset(String mapFilenamePath) {
        if (mapFilenamePath == null || mapFilenamePath.isEmpty()) {
            return;
        }
        //load asset
        if (filePathResolver.resolve(mapFilenamePath).exists()) {
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(filePathResolver));
            assetManager.load(mapFilenamePath, TiledMap.class);
            //Until we add loading screen,
            //just block until we load the map
            assetManager.finishLoadingAsset(mapFilenamePath);
            LOGGER.debug("Map loaded!: {}", mapFilenamePath);
        } else {
            LOGGER.error("Map doesn’t exist!: {}", mapFilenamePath);
        }
    }

    public static TiledMap getMapAsset(String mapFilenamePath) {
        TiledMap map = null;
        // once the asset manager is done loading
        if (assetManager.isLoaded(mapFilenamePath)) {
            map = assetManager.get(mapFilenamePath, TiledMap.class);
        } else {
            LOGGER.debug("Map is not loaded: {}", mapFilenamePath);
        }
        return map;
    }

    public static void loadTextureAsset(String textureFilenamePath) {
        if (textureFilenamePath == null ||
                textureFilenamePath.isEmpty()) {
            return;
        }
        //load asset
        if (filePathResolver.resolve(textureFilenamePath).exists()) {
            assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
            assetManager.load(textureFilenamePath, Texture.class);
            //Until we add loading screen,
            //just block until we load the map
            assetManager.finishLoadingAsset(textureFilenamePath);
        } else {
            LOGGER.error("Texture doesn’t exist!: {}", textureFilenamePath);
        }
    }

    public static Texture getTextureAsset(String textureFilenamePath) {
        Texture texture = null;
        // once the asset manager is done loading
        if (assetManager.isLoaded(textureFilenamePath)) {
            texture = assetManager.get(textureFilenamePath, Texture.class);
        } else {
            LOGGER.error("Texture is not loaded: {}", textureFilenamePath);
        }
        return texture;
    }

}
