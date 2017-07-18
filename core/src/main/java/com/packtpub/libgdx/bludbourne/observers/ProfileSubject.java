package com.packtpub.libgdx.bludbourne.observers;

import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

/**
 * Created on 04/27/2017.
 */
public interface ProfileSubject {

    Array<ProfileObserver> observers = new Array<ProfileObserver>();

    default void addObserver(ProfileObserver profileObserver) {
        observers.add(profileObserver);
    }

    default void removeObserver(ProfileObserver profileObserver) {
        observers.removeValue(profileObserver, true);
    }

    default void notify(final ProfileManager profileManager, ProfileObserver.ProfileEvent event) {
        for (ProfileObserver observer : observers) {
            observer.onNotify(profileManager, event);
        }
    }

}