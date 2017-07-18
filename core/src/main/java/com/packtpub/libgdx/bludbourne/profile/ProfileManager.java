package com.packtpub.libgdx.bludbourne.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.packtpub.libgdx.bludbourne.observers.ProfileObserver;
import com.packtpub.libgdx.bludbourne.observers.ProfileSubject;
import com.packtpub.libgdx.bludbourne.utility.MapperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 04/27/2017.
 */
public enum ProfileManager implements ProfileSubject {

    INSTANCE;

    private final static Logger LOGGER = LoggerFactory.getLogger(ProfileManager.class);

    private Map<String, FileHandle> profiles = null;
    private ObjectMap<String, Object> profileProperties = new ObjectMap<>();
    private String profileName;

    private static final String SAVEGAME_SUFFIX = ".sav";
    public static final String DEFAULT_PROFILE = "default";

    ProfileManager() {
        profiles = new HashMap<>();
        profiles.clear();
        profileName = DEFAULT_PROFILE;
        storeAllProfiles();
    }

    public Array<String> getProfileList() {
        Array<String> profiles = new Array<>();
        for (String e : this.profiles.keySet()) {
            profiles.add(e);
        }
        return profiles;
    }

    public FileHandle getProfileFile(String profile) {
        if (!doesProfileExist(profile)) {
            return null;
        }
        return profiles.get(profile);
    }

    public void storeAllProfiles() {
        if (Gdx.files.isLocalStorageAvailable()) {
            FileHandle[] files = Gdx.files.local(".").list(SAVEGAME_SUFFIX);

            for (FileHandle file : files) {
                profiles.put(file.nameWithoutExtension(), file);
            }
        } else {
            //TODO: try external directory here
            return;
        }
    }

    public boolean doesProfileExist(String profileName) {
        return profiles.containsKey(profileName);
    }

    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite) {
        String fullFilename = profileName + SAVEGAME_SUFFIX;

        boolean localFileExists = Gdx.files.internal(fullFilename).exists();

        //If we cannot overwrite and the file exists, exit
        if (localFileExists && !overwrite) {
            return;
        }

        FileHandle file = null;

        if (Gdx.files.isLocalStorageAvailable()) {
            file = Gdx.files.local(fullFilename);
            file.writeString(fileData, !overwrite);
        }

        profiles.put(profileName, file);
    }

    public void setProperty(String key, Object object) {
        profileProperties.put(key, object);
    }

    public <T extends Object> T getProperty(String key, Class<T> type) {
        T property = null;
        if (!profileProperties.containsKey(key)) {
            return property;
        }
        property = (T) profileProperties.get(key);
        return property;
    }

    public void saveProfile() {
        LOGGER.info("Saving profile: {}", this.profileName);
        notify(this, ProfileObserver.ProfileEvent.SAVING_PROFILE);
        String text = MapperProvider.INSTANCE.writeValueAsString(profileProperties);
        writeProfileToStorage(profileName, text, true);
    }

    public void loadProfile() {
        LOGGER.info("Loading profile: {}", this.profileName);
        String fullProfileFileName = profileName + SAVEGAME_SUFFIX;
        boolean doesProfileFileExist = Gdx.files.internal(fullProfileFileName).exists();

        if (!doesProfileFileExist) {
            LOGGER.warn("File doesn't exist!");
            return;
        }

        profileProperties = MapperProvider.INSTANCE.parse(ObjectMap.class, profiles.get(profileName));
        notify(this, ProfileObserver.ProfileEvent.PROFILE_LOADED);
    }

    public void setCurrentProfile(String profileName) {
        if (doesProfileExist(profileName)) {
            this.profileName = profileName;
        } else {
            this.profileName = DEFAULT_PROFILE;
        }
    }

}
