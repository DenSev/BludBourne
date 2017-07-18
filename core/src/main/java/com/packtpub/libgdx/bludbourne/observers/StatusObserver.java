package com.packtpub.libgdx.bludbourne.observers;

/**
 * Created on 17.07.2017.
 */
public interface StatusObserver {
    enum StatusEvent {
        UPDATED_GP,
        UPDATED_LEVEL,
        UPDATED_HP,
        UPDATED_MP,
        UPDATED_XP
    }

    void onNotify(final int value, StatusEvent event);
}
