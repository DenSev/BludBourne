package com.packtpub.libgdx.bludbourne.observers;

import com.packtpub.libgdx.bludbourne.entity.EntityConfig;

/**
 * Created on 17.07.2017.
 */

public interface EntityObserver {
    enum ComponentEvent {
        LOAD_CONVERSATION,
        SHOW_CONVERSATION,
        HIDE_CONVERSATION
    }

    void onNotify(final EntityConfig config, ComponentEvent event);
}
