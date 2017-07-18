package com.packtpub.libgdx.bludbourne.observers;

import com.packtpub.libgdx.bludbourne.profile.ProfileManager;

/**
 * Created on 04/27/2017.
 */
public interface ProfileObserver {
    enum ProfileEvent {
        PROFILE_LOADED,
        SAVING_PROFILE
    }

    void onNotify(final ProfileManager profileManager, ProfileEvent event);
}

